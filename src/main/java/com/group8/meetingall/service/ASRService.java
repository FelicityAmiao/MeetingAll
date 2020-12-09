package com.group8.meetingall.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.group8.meetingall.dto.ASRAPIResultDto;
import com.group8.meetingall.utils.EncryptUtil;
import com.group8.meetingall.utils.HttpUtil;
import com.group8.meetingall.utils.SliceIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ASRService {
    public static final int SLICE_SICE = 10485760;// 10M
    public static final String AUDIO_FILE_PATH = ASRService.class.getResource("/").getPath() + "/audio/test.mp3";
    @Value("${LFASR.appID}")
    private String appID;
    @Value("${LFASR.secretKey}")
    private String secretKey;
    @Value("${LFASR.host}")
    private String host;
    @Value("${LFASR.prepareURL}")
    private String prepareURL;
    @Value("${LFASR.uploadURL}")
    private String uploadURL;
    @Value("${LFASR.mergeURL}")
    private String mergeURL;
    @Value("${LFASR.getProgressURL}")
    private String getProgressURL;
    @Value("${LFASR.getResultURL}")
    private String getResultURL;

    public void startConvert() {
        try {
            File audio = new File(AUDIO_FILE_PATH);
            FileInputStream fis = new FileInputStream(audio);
            // 预处理
            String taskId = prepare(audio);
            // 分片上传文件
            int len = 0;
            byte[] slice = new byte[SLICE_SICE];
            SliceIdGenerator generator = new SliceIdGenerator();
            while ((len = fis.read(slice)) > 0) {
                if (fis.available() == 0) {
                    slice = Arrays.copyOfRange(slice, 0, len);
                }
                // 上传分片
                uploadSlice(taskId, generator.getNextSliceId(), slice);
            }
            // 合并文件
            merge(taskId);
            // 轮询获取任务结果
            while (true) {
                try {
                    System.out.println("sleep a while Zzz");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ASRAPIResultDto taskProgress = getProgress(taskId);
                if (taskProgress.getOk() == 0) {
                    if (taskProgress.getErr_no() != 0) {
                        throw new RuntimeException("任务失败：" + JSON.toJSONString(taskProgress));
                    }
                    String taskStatus = taskProgress.getData();
                    if (JSON.parseObject(taskStatus).getInteger("status") == 9) {
                        System.out.println("任务完成！");
                        break;
                    }
                    System.out.println("任务处理中：" + taskStatus);
                } else {
                    throw new RuntimeException("获取任务进度失败！");
                }
            }
            // 获取结果
            String result = getResult(taskId);
            JSONArray jsonArray = JSON.parseArray(result);
            StringBuilder resultStr = new StringBuilder();
            int length = jsonArray.toArray().length;
            for (int i = 0; i < length; i++) {
                resultStr.append(jsonArray.getJSONObject(i).getString("onebest"));
            }
            System.out.println("----------转换后的结果----------\n" + resultStr);
        } catch (SignatureException | IOException e) {
            e.printStackTrace();
        }
    }

    public MultiValueMap<String, String> getBaseAuthParam(String taskId) throws SignatureException {
        MultiValueMap<String, String> baseParam = new LinkedMultiValueMap<>();
        String ts = String.valueOf(System.currentTimeMillis() / 1000L);
        baseParam.put("app_id", Collections.singletonList(appID));
        baseParam.put("ts", Collections.singletonList(ts));
        baseParam.put("signa", Collections.singletonList(EncryptUtil.HmacSHA1Encrypt(EncryptUtil.MD5(appID + ts), secretKey)));
        if (taskId != null) {
            baseParam.put("task_id", Collections.singletonList(taskId));
        }
        return baseParam;
    }

    public Map<String, String> getBaseAuthParamForUpload(String taskId) throws SignatureException {
        Map<String, String> baseParam = new HashMap<String, String>();
        String ts = String.valueOf(System.currentTimeMillis() / 1000L);
        baseParam.put("app_id", appID);
        baseParam.put("ts", ts);
        baseParam.put("signa", EncryptUtil.HmacSHA1Encrypt(EncryptUtil.MD5(appID + ts), secretKey));
        if (taskId != null) {
            baseParam.put("task_id", taskId);
        }
        return baseParam;
    }

    public String prepare(File audio) throws SignatureException {
        MultiValueMap<String, String> prepareParam = getBaseAuthParam(null);
        long fileLenth = audio.length();
        prepareParam.put("file_len", Collections.singletonList(fileLenth + ""));
        prepareParam.put("file_name", Collections.singletonList(audio.getName()));
        prepareParam.put("slice_num", Collections.singletonList((fileLenth / SLICE_SICE) + (fileLenth % SLICE_SICE == 0 ? 0 : 1) + ""));
        String response = HttpUtil.post(host + prepareURL, prepareParam);
        if (response == null) {
            throw new RuntimeException("预处理接口请求失败！");
        }
        ASRAPIResultDto resultDto = JSON.parseObject(response, ASRAPIResultDto.class);
        String taskId = resultDto.getData();
        if (resultDto.getOk() != 0 || taskId == null) {
            throw new RuntimeException("预处理失败！" + response);
        }
        System.out.println("预处理成功, taskid：" + taskId);
        return taskId;
    }

    public void uploadSlice(String taskId, String sliceId, byte[] slice) throws SignatureException {
        Map<String, String> uploadParam = getBaseAuthParamForUpload(taskId);
        uploadParam.put("slice_id", sliceId);
        String response = HttpUtil.postMulti(host + uploadURL, uploadParam, slice);
        if (response == null) {
            throw new RuntimeException("分片上传接口请求失败！");
        }
        if (JSON.parseObject(response).getInteger("ok") == 0) {
            System.out.println("分片上传成功, sliceId: " + sliceId + ", sliceLen: " + slice.length);
            return;
        }
        System.out.println("params: " + JSON.toJSONString(uploadParam));
        throw new RuntimeException("分片上传失败！" + response + "|" + taskId);
    }


    public void merge(String taskId) throws SignatureException {
        String response = HttpUtil.post(host + mergeURL, getBaseAuthParam(taskId));
        if (response == null) {
            throw new RuntimeException("文件合并接口请求失败！");
        }
        if (JSON.parseObject(response).getInteger("ok") == 0) {
            System.out.println("文件合并成功, taskId: " + taskId);
            return;
        }
        throw new RuntimeException("文件合并失败！" + response);
    }

    public ASRAPIResultDto getProgress(String taskId) throws SignatureException {
        String response = HttpUtil.post(host + getProgressURL, getBaseAuthParam(taskId));
        if (response == null) {
            throw new RuntimeException("获取任务进度接口请求失败！");
        }
        return JSON.parseObject(response, ASRAPIResultDto.class);
    }

    public String getResult(String taskId) throws SignatureException {
        String responseStr = HttpUtil.post(host + getResultURL, getBaseAuthParam(taskId));
        if (responseStr == null) {
            throw new RuntimeException("获取结果接口请求失败！");
        }
        ASRAPIResultDto response = JSON.parseObject(responseStr, ASRAPIResultDto.class);
        if (response.getOk() != 0) {
            throw new RuntimeException("获取结果失败！" + responseStr);
        }
        return response.getData();
    }

}