package com.group8.meetingall.service;

import com.group8.meetingall.dto.asr.*;
import com.group8.meetingall.entity.TranslateResultEntity;
import com.group8.meetingall.exception.ASRException;
import com.group8.meetingall.repository.TranslateResultRepository;
import com.group8.meetingall.utils.HttpUtil;
import com.group8.meetingall.utils.JsonUtils;
import com.group8.meetingall.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.TimeZone;

import static com.group8.meetingall.constant.ASRConstant.*;
import static com.group8.meetingall.utils.HttpUtil.REQUSET_METHOD_POST;

@Service
@Slf4j
public class CantoneseASRService {
    @Value("${TCASR.host}")
    private String host;
    @Value("${TCASR.url}")
    private String url;
    @Value("${TCASR.secretID}")
    private String secretID;
    @Value("${TCASR.secretKey}")
    private String secretKey;
    @Autowired
    TranslateResultRepository translateResultRepository;

    public String startConvert() throws IOException {
        try {
            CreateRecTaskRequestDTO req = CreateRecTaskRequestDTO.builder()
                    .engineModelType(ENGINE_MODLE_TYPE_16K_CA)
                    .channelNum(1L)
                    .resTextFormat(0L)
                    .sourceType(1L)
                    .build();
            processVideo(req);
            CreateRecTaskDTO createRecTaskDTO = CreateRecTask(req);
            String result = getProcessedResult(createRecTaskDTO.getData().getTaskId());
            TranslateResultEntity translateResultEntity = translateResultRepository.saveTranslateResult(result);
            return translateResultEntity.getUUID();
        } catch (ASRException e) {
            System.out.println(e.toString());
        }
        return null;
    }


    private String getProcessedResult(Long taskId) throws ASRException {
        DescribeTaskStatusRequestDTO describeTaskStatusRequestDTO = DescribeTaskStatusRequestDTO.builder()
                .taskId(taskId)
                .build();
        while (true) {
            try {
                System.out.println("sleep a while Zzz");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DescribeTaskStatusDTO describeTaskStatusDTO = DescribeTaskStatus(describeTaskStatusRequestDTO);
            TaskStatusDTO responseData = describeTaskStatusDTO.getData();
            Long describeTaskStatus = responseData.getStatus();
            if (describeTaskStatus == 2L) {
                System.out.println("process finished,translate result is:" + responseData.getResult());
                break;
            } else if (describeTaskStatus == 0L) {
                System.out.println("Task is waiting for process,please wait...");
            } else if (describeTaskStatus == 1L) {
                System.out.println("Task is processing,please wait...");
            } else if (describeTaskStatus == 3L) {
                System.out.println("Process failed!");
                throw new ASRException(responseData.getErrorMsg(), describeTaskStatusDTO.getRequestId(), responseData.getStatusStr());
            }
        }
        return DescribeTaskStatus(describeTaskStatusRequestDTO).getData().getResult();
    }

    public void processVideo(CreateRecTaskRequestDTO req) throws IOException {
        String audioFilePath = String.valueOf(new ClassPathResource("\\audio\\guangdonghua.mp3").getInputStream());
//        String audioFilePath = this.getClass().getResource("/").getPath() + "/audio/guangdonghua.mp3";
        File file = new File(audioFilePath);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        req.setDataLen(file.length());
        inputFile.read(buffer);
        inputFile.close();
        String encodeData = Base64.getEncoder().encodeToString(buffer);
        req.setData(encodeData);
    }

    public CreateRecTaskDTO CreateRecTask(CreateRecTaskRequestDTO req) throws ASRException {
        byte[] requestPayload = JsonUtils.toJson(req).getBytes(StandardCharsets.UTF_8);
        ResponseEntity responseEntity = doRequestWithTC3(requestPayload, ACTION_CREATE_REC_TASK);
        String rspStr = processResponseEntity(responseEntity);
        CreateRecTaskResponseDTO rsp = JsonUtils.fromJson(rspStr, CreateRecTaskResponseDTO.class);
        return rsp.response;
    }

    public DescribeTaskStatusDTO DescribeTaskStatus(DescribeTaskStatusRequestDTO req) throws ASRException {
        byte[] requestPayload = JsonUtils.toJson(req).getBytes(StandardCharsets.UTF_8);
        ResponseEntity responseEntity = doRequestWithTC3(requestPayload, ACTION_DESCRIBE_TASK_STATUS);
        String rspStr = processResponseEntity(responseEntity);
        DescribeTaskStatusResponseDTO rsp = JsonUtils.fromJson(rspStr, DescribeTaskStatusResponseDTO.class);
        return rsp.response;
    }

    private String processResponseEntity(ResponseEntity responseEntity) throws ASRException {
        if (responseEntity.getStatusCodeValue() != 200) {
            String msg = "response code is " + responseEntity.getStatusCodeValue() + ", not 200";
            log.info(msg);
            throw new ASRException(msg, "", "ServerSideError");
        }
        String strResp = JsonUtils.toJson(responseEntity.getBody());
        ASRErrResonpseDTO errResp = JsonUtils.fromJson(strResp, ASRErrResonpseDTO.class);
        if (errResp != null && errResp.response.error != null) {
            throw new ASRException(
                    errResp.response.error.message,
                    errResp.response.requestId,
                    errResp.response.error.code);
        }
        return strResp;
    }

    private ResponseEntity doRequestWithTC3(byte[] requestPayload, String action)
            throws ASRException {
        String canonicalRequest = constructCanonicalRequestStr(requestPayload);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String date = getDateStr(timestamp);

        String stringToSign = constructStringToSign(canonicalRequest, timestamp, date);
        String signature = constructSignature(date, stringToSign);
        String authorization = constructAuthorization(date, signature);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", CONTENT_TYPE);
        headers.add("Host", host);
        headers.add("Authorization", authorization);
        headers.add("X-TC-Action", action);
        headers.add("X-TC-Timestamp", timestamp);
        headers.add("X-TC-Version", API_VERSION);
        headers.add("X-TC-RequestClient", SDK_VERSION);
        headers.add("X-TC-Region", REGION);
        return HttpUtil.post(url, requestPayload, headers);
    }

    private String constructAuthorization(String date, String signature) {
        String credentialScope = date + "/asr/tc3_request";
        return SIGN_TC3_256 + " "
                + "Credential=" + secretID + "/" + credentialScope + ", "
                + "SignedHeaders=" + SIGNED_HEADERS + ", "
                + "Signature=" + signature;
    }

    private String getDateStr(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(Long.parseLong(timestamp + "000")));
    }

    private String constructSignature(String date, String stringToSign) throws ASRException {
        byte[] secretDate = SignUtil.hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = SignUtil.hmac256(secretDate, "asr");
        byte[] secretSigning = SignUtil.hmac256(secretService, "tc3_request");
        return DatatypeConverter.printHexBinary(SignUtil.hmac256(secretSigning, stringToSign)).toLowerCase();
    }

    private String constructStringToSign(String canonicalRequest, String timestamp, String date) throws ASRException {
        String credentialScope = date + "/asr/tc3_request";
        String hashedCanonicalRequest = SignUtil.sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        return SIGN_TC3_256 + "\n"
                + timestamp + "\n"
                + credentialScope + "\n"
                + hashedCanonicalRequest;
    }

    private String constructCanonicalRequestStr(byte[] requestPayload) throws ASRException {
        String canonicalHeaders = "content-type:" + CONTENT_TYPE + "\nhost:" + host + "\n";
        String hashedRequestPayload = SignUtil.sha256Hex(requestPayload);
        return REQUSET_METHOD_POST + "\n"
                + CANONICAL_URI + "\n"
                + CANONICAL_QUERY_STRING + "\n"
                + canonicalHeaders + "\n"
                + SIGNED_HEADERS + "\n"
                + hashedRequestPayload;
    }

}
