package com.group8.meetingall.controller;

import com.group8.meetingall.service.ASRService;
import com.group8.meetingall.service.ReportGenerationService;
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
public class ASRController {

    @Value("${server.port}")
    private String serverPort;
    @Autowired
    ASRService asrService;
    @Autowired
    ReportGenerationService reportGenerationService;

    @GetMapping(value = "/convert")
    public String convert() throws UnknownHostException {
        String UUID = asrService.convert();
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName() + ":" + serverPort + "/api/ASR/getTranslateResultFile?uuid=" + UUID;
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
