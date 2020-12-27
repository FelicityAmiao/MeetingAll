/*
 * Copyright (c) 2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.group8.meetingall.service.ASR;

import com.group8.meetingall.exception.ASRException;
import com.group8.meetingall.utils.HttpUtil;
import com.group8.meetingall.utils.JsonUtils;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
//@AllArgsConstructor
@Slf4j
public class AbstractClient {

    public static final String SDK_VERSION = "SDK_JAVA_3.1.184";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";
    public static final String SIGNED_HEADERS = "content-type;host";
    public static final String SECRET_ID = "AKID5wa9Xh6hSQxJhg78VE1TEHepwqWzdFIA";
    public static final String SECRET_KEY = "wc5MSKwJWUJvIn6JhzAGDz9T7VJysaOz";
    public static final String SIGN_TC3_256 = "TC3-HMAC-SHA256";
    public static final String END_POINT = "asr.tencentcloudapi.com";
    public static final String REGION = "ap-shanghai";
    private static String API_VERSION = "2019-06-14";

    public CreateRecTaskResponse CreateRecTask(CreateRecTaskRequest req) throws ASRException {
        String rspStr = "";
        byte[] requestPayload = JsonUtils.toJson(req).getBytes(StandardCharsets.UTF_8);
        ResponseEntity responseEntity = doRequestWithTC3(requestPayload, "CreateRecTask");
        rspStr = processResponseEntity(responseEntity);
        JsonResponseModel1 rsp = JsonUtils.fromJson(rspStr, JsonResponseModel1.class);
        return rsp.response;
    }

    public DescribeTaskStatusResponse DescribeTaskStatus(DescribeTaskStatusRequest req) throws ASRException {
        String rspStr = "";
        byte[] requestPayload = JsonUtils.toJson(req).getBytes(StandardCharsets.UTF_8);
        ResponseEntity responseEntity = doRequestWithTC3(requestPayload, "DescribeTaskStatus");
        rspStr = processResponseEntity(responseEntity);
        JsonResponseModel3 rsp = JsonUtils.fromJson(rspStr, JsonResponseModel3.class);
        return rsp.response;
    }

    private String processResponseEntity(ResponseEntity responseEntity) throws ASRException {
        if (responseEntity.getStatusCodeValue() != 200) {
            String msg = "response code is " + responseEntity.getStatusCodeValue() + ", not 200";
            log.info(msg);
            throw new ASRException(msg, "", "ServerSideError");
        }
        String strResp = JsonUtils.toJson(responseEntity.getBody());
        JsonResponseModel2 errResp = JsonUtils.fromJson(strResp, JsonResponseModel2.class);
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

        String url = "https://asr.tencentcloudapi.com/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", CONTENT_TYPE);
        headers.add("Host", END_POINT);
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
                + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
                + "SignedHeaders=" + SIGNED_HEADERS + ", "
                + "Signature=" + signature;
    }

    private String getDateStr(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(Long.parseLong(timestamp + "000")));
    }

    private String constructSignature(String date, String stringToSign) throws ASRException {
        byte[] secretDate = Sign.hmac256(("TC3" + SECRET_KEY).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = Sign.hmac256(secretDate, "asr");
        byte[] secretSigning = Sign.hmac256(secretService, "tc3_request");
        return DatatypeConverter.printHexBinary(Sign.hmac256(secretSigning, stringToSign)).toLowerCase();
    }

    private String constructStringToSign(String canonicalRequest, String timestamp, String date) throws ASRException {
        String credentialScope = date + "/asr/tc3_request";
        String hashedCanonicalRequest = Sign.sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        return SIGN_TC3_256 + "\n"
                + timestamp + "\n"
                + credentialScope + "\n"
                + hashedCanonicalRequest;
    }

    private String constructCanonicalRequestStr(byte[] requestPayload) throws ASRException {
        String requsetMethodPost = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:" + CONTENT_TYPE + "\nhost:" + END_POINT + "\n";

        String hashedRequestPayload = Sign.sha256Hex(requestPayload);
        return requsetMethodPost + "\n"
                + canonicalUri + "\n"
                + canonicalQueryString + "\n"
                + canonicalHeaders + "\n"
                + SIGNED_HEADERS + "\n"
                + hashedRequestPayload;
    }

}
