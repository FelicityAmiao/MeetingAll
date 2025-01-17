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
    @Value("${filePath.audio}")
    private String audioPath;

    @GetMapping(value = "/download/{file}")
    public void downloadFile(HttpServletResponse response, @PathVariable(value = "file") String fileName) throws IOException {
        OutputStream out = null;
        String filteType = fileName.substring(fileName.lastIndexOf(".")+1);
        String path = reportPath + fileName;
        String contentType = "application/msword;charset=UTF-8";
        if("wav".equalsIgnoreCase(filteType)){
            path = audioPath + "/" + fileName;
            contentType = "audio/wav";
        }
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            out = response.getOutputStream();
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(URLEncoder.encode(fileName, "UTF-8")
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
