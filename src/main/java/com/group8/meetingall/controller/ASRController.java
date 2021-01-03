package com.group8.meetingall.controller;

import com.group8.meetingall.service.XFCantoneseASRService;
import com.group8.meetingall.service.XFChineseASRService;
import com.group8.meetingall.service.TCCantoneseASRService;
import com.group8.meetingall.service.ReportGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/ASR")
@Slf4j
public class ASRController {

    @Value("${server.port}")
    private String serverPort;
    @Autowired
    XFChineseASRService XFChineseAsrService;
    @Autowired
    XFCantoneseASRService xfCantoneseASRService;
    @Autowired
    TCCantoneseASRService TCCantoneseASRService;
    @Autowired
    ReportGenerationService reportGenerationService;
    public static final String AUDIO_FILE = "test.mp3";

    @GetMapping(value = "/convert")
    public String convert() throws UnknownHostException {
        String UUID = XFChineseAsrService.convert(AUDIO_FILE);
        InetAddress inetAddress = InetAddress.getLocalHost();
        return "http://" + inetAddress.getHostName() + ":" + serverPort + "/api/ASR/getTranslateResultFile?uuid=" + UUID;
    }

    @GetMapping(value = "/convertCantoneseVideo")
    public String convertCantoneseVideo() throws IOException {
        String UUID = TCCantoneseASRService.startConvert("cantonese.mp3");
        return "http://www.meetingall.info" + ":" + serverPort + "/api/ASR/getTranslateResultFile?uuid=" + UUID;
    }

    @GetMapping(value = "/testExecShellScript")
    public String testExecShellScript() throws IOException, InterruptedException {
        log.info("开始执行脚本...");
        ProcessBuilder pb = new ProcessBuilder("/home/test/test.sh", "test");
        Process process = pb.start();
        int exitValue = process.waitFor();
        return "exitValue is " + exitValue;
    }

    @GetMapping(value = "/testTranslateToSimplifiedChinese")
    public String testTranslateToSimplifiedChinese() throws Exception {
        return xfCantoneseASRService.startXFASRProcessing("test");
    }

    @GetMapping(value = "/getTranslateResultFile")
    public void getTranslateResultFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String uuid) throws IOException {
        OutputStream out = null;
        try {
            XWPFDocument document = reportGenerationService.generateMeetingReport(uuid);
            out = response.getOutputStream();
            response.setContentType("application/msword;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(URLEncoder.encode("Meeting Report - " + uuid + ".docx", "UTF-8")
                            .replaceAll("\\+", "%20")));
            document.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }
}
