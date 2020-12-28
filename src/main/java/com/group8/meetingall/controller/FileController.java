package com.group8.meetingall.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@CrossOrigin
@RequestMapping("/files")
@RestController
public class FileController {

    @GetMapping(value = "/download/{file}")
    public void getTranslateResultFile(HttpServletResponse response, @PathVariable(value = "file") String fileName) throws IOException {
        OutputStream out = null;
        String path = this.getClass().getResource("/files").getPath() + "/" + fileName;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            out = response.getOutputStream();
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer,0,len);
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
