package com.group8.meetingall.service.ASR;

import com.group8.meetingall.utils.JsonUtils;
import com.group8.meetingall.exception.ASRException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;


public class GuangDongASRService {
    public static void main(String[] args) throws IOException {
        try {
            AbstractClient client = AbstractClient.builder().build();
            CreateRecTaskRequest req = CreateRecTaskRequest.builder()
                    .engineModelType("16k_ca")
                    .channelNum(1L)
                    .resTextFormat(0L)
                    .sourceType(1L)
                    .build();
            File file = new File("D:\\my_project\\MeetingAll_backend\\src\\main\\resources\\audio\\guangdonghua.mp3");
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            req.setDataLen(file.length());
            inputFile.read(buffer);
            inputFile.close();
            String encodeData = Base64.getEncoder().encodeToString(buffer);
            req.setData(encodeData);
            CreateRecTaskResponse resp = client.CreateRecTask(req);
            DescribeTaskStatusRequest describeTaskStatusRequest = DescribeTaskStatusRequest.builder()
                    .taskId(resp.getData().getTaskId())
                    .build();
            Thread.sleep(10000);
            DescribeTaskStatusResponse describeTaskStatusResponse = client.DescribeTaskStatus(describeTaskStatusRequest);
            System.out.println(JsonUtils.toJson(describeTaskStatusResponse));
        } catch (ASRException | InterruptedException e) {
            System.out.println(e.toString());
        }

    }

}
