package com.group8.meetingall.service;

import com.group8.meetingall.dto.xfasr.*;
import com.group8.meetingall.entity.TranslateResultEntity;
import com.group8.meetingall.repository.TranslateResultRepository;
import com.group8.meetingall.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class XFCantoneseASRService extends WebSocketListener {
    private static final String hostUrl = "https://iat-niche-api.xfyun.cn/v2/iat";
    private static final String appid = "5fcd6f17";
    private static final String apiSecret = "49b35812b3f76ada00c60fab65b594ab";
    private static final String apiKey = "dcbd8654315265b138314a0a2107bbf0";
    public static final int StatusFirstFrame = 0;
    public static final int StatusContinueFrame = 1;
    public static final int StatusLastFrame = 2;
    private Decoder decoder = new Decoder();
    private String result = null;
    private String audioAddress = null;
    @Value("${filePath.audio}")
    private String audioPath;
    @Autowired
    TranslateResultRepository translateResultRepository;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAudioAddress() {
        return audioAddress;
    }

    public void setAudioAddress(String audioAddress) {
        this.audioAddress = audioAddress;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("开始建立连接...");
        new Thread(() -> {
            int frameSize = 1280; //每一帧音频的大小,建议每 40ms 发送 122B
            int intervel = 40;
            int status = 0;  // 音频的状态
            try (FileInputStream fs = new FileInputStream(audioPath+ getAudioAddress())) {
                byte[] buffer = new byte[frameSize];
                end:
                while (true) {
                    int len = fs.read(buffer);
                    if (len == -1) {
                        status = StatusLastFrame;  //文件读完，改变status 为 2
                    }
                    switch (status) {
                        case StatusFirstFrame:   // 第一帧音频status = 0
                            XFRequestDTO xfRequestDTO = XFRequestDTO.builder()
                                    .common(CommonDTO.builder()
                                            .app_id(appid)
                                            .build())
                                    .business(BusinessDTO.builder()
                                            .language("zh_cn")
                                            .domain("iat")
                                            .accent("cn_cantonese")
                                            .build())
                                    .data(RequestDataDTO.builder()
                                            .status(StatusFirstFrame)
                                            .format("audio/L16;rate=16000")
                                            .encoding("raw")
                                            .audio(Base64.getEncoder().encodeToString(Arrays.copyOf(buffer, len)))
                                            .build())
                                    .build();
                            webSocket.send(JsonUtils.toJson(xfRequestDTO));
                            status = StatusContinueFrame;  // 发送完第一帧改变status 为 1
                            break;
                        case StatusContinueFrame:  //中间帧status = 1
                            XFRequestDTO xfRequestDTO1 = XFRequestDTO.builder()
                                    .data(RequestDataDTO.builder()
                                            .status(StatusContinueFrame)
                                            .format("audio/L16;rate=16000")
                                            .encoding("raw")
                                            .audio(Base64.getEncoder().encodeToString(Arrays.copyOf(buffer, len)))
                                            .build())
                                    .build();
                            webSocket.send(JsonUtils.toJson(xfRequestDTO1));
                            break;
                        case StatusLastFrame:    // 最后一帧音频status = 2 ，标志音频发送结束
                            XFRequestDTO xfRequestDTO2 = XFRequestDTO.builder()
                                    .data(RequestDataDTO.builder()
                                            .status(StatusLastFrame)
                                            .format("audio/L16;rate=16000")
                                            .encoding("raw")
                                            .audio("")
                                            .build())
                                    .build();
                            webSocket.send(JsonUtils.toJson(xfRequestDTO2));
                            log.info("最后一帧发送完毕！");
                            break end;
                    }
                    Thread.sleep(intervel); //模拟音频采样延时
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        ResponseData resp = JsonUtils.fromJson(text, ResponseData.class);
        System.out.println(resp.toString());
        if (resp != null) {
            if (resp.getCode() != 0) {
                log.info("code=>" + resp.getCode() + " error=>" + resp.getMessage() + " sid=" + resp.getSid());
                log.info("错误码查询链接：https://www.xfyun.cn/document/error-code");
                return;
            }
            if (resp.getData() != null) {
                if (resp.getData().getResult() != null) {
                    Text te = resp.getData().getResult().getText();
                    try {
                        decoder.decode(te);
                        log.info("中间识别结果 ==》" + decoder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (resp.getData().getStatus() == 2) {
                    log.info("最终识别结果 ==》" + decoder.toString());
                    setResult(decoder.toString());
                    System.out.println("本次识别sid ==》" + resp.getSid());
                    decoder.discard();
                    webSocket.close(1000, "");
                    log.info("会话结束，关闭会话");
                }
            }
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        if (null != response) {
            int code = response.code();
            log.info("onFailure code:" + code);
            try {
                log.info("onFailure body:" + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (101 != code) {
                log.info("connection failed");
            }
        }
    }

    public String startXFASRProcessing(String audioAddress) throws Exception {
        log.info("开始转写过程...");
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        XFCantoneseASRService xfCantoneseASRService = new XFCantoneseASRService();
        xfCantoneseASRService.setAudioAddress(audioAddress);
        client.newWebSocket(request, xfCantoneseASRService);
        while (true) {
            String result = xfCantoneseASRService.getResult();
            if (result != null) {
                System.out.println("返回结果--" + xfCantoneseASRService.getResult());
                TranslateResultEntity translateResultEntity = translateResultRepository.saveTranslateResult(result);
                return translateResultEntity.getUUID();
            }
            Thread.sleep(500);// 因为监听器是异步的，需要返回结果
        }
    }

    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        StringBuilder builder = new StringBuilder("host: ")
                .append(url.getHost())
                .append("\n")
                .append("date: ")
                .append(date)
                .append("\n")
                .append("GET ")
                .append(url.getPath())
                .append(" HTTP/1.1");
        Charset charset = StandardCharsets.UTF_8;
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost()).
                        build();
        return httpUrl.toString();
    }

}
