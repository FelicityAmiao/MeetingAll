package com.group8.meetingall.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.*;
import java.nio.charset.Charset;

@CrossOrigin
@RequestMapping("/files")
@RestController
public class FileController {

    @PostMapping
    public void getFileHighFrequencyWords(@RequestParam MultipartFile file ) {
        String path = this.getClass().getResource("/files").getPath();
        File filePath = new File(path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        File targetFile = new File(path+"//"+file.getOriginalFilename());
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
