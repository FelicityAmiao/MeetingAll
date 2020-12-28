package com.group8.meetingall.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@CrossOrigin
@RequestMapping("/files")
@RestController
public class FileController {
    @Value("${filePath.report}")
    private String reportPath;

    @GetMapping(value = "/download/{file}")
    public void getTranslateResultFile(HttpServletResponse response, @PathVariable(value = "file") String fileName) throws IOException {
        OutputStream out = null;
        String path = reportPath + fileName;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            out = response.getOutputStream();
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            response.setContentType("application/msword;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(URLEncoder.encode("Meeting Report - " + fileName + ".docx", "UTF-8")
                            .replaceAll("\\+", "%20")));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

}
