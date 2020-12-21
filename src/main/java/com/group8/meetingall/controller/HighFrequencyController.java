package com.group8.meetingall.controller;

import com.group8.meetingall.service.HighFrequencyService;
import com.group8.meetingall.vo.Keyword;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin
@RequestMapping("/sentence")
@RestController
public class HighFrequencyController {

    @Autowired
    private HighFrequencyService highFrequencyService;

    @PostMapping("/highFrequencyWords")
    public List<Keyword> getHighFrequencyWords(@RequestParam String text, @RequestParam Integer count) {
        return highFrequencyService.getHighFrequencyWords(text, count);
    }

    @GetMapping("/{filename}/{filetype}/{count}")
    public List<Keyword> getFileHighFrequencyWords(@PathVariable(value = "filename", required = true) String fileName,
                                                   @PathVariable(value = "filetype", required = true) String fileType,
                                                   @PathVariable(value = "count", required = true) String count) {
        String path = this.getClass().getResource("/files").getPath() + "/" + fileName + "." + fileType;


        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String text = extractor.getText();
            fis.close();
            return highFrequencyService.getHighFrequencyWords(text, Integer.valueOf(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/{filename}/{filetype}")
    public String highLightKeyWords(@PathVariable(value = "filename") String fileName,
                                    @PathVariable(value = "filetype") String fileType) {
        String path = this.getClass().getResource("/files").getPath() + "/" + fileName + "." + fileType;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String text = extractor.getText();
            fis.close();
            XWPFDocument document = highFrequencyService.generateHighlightWordFile(text);
            FileOutputStream out = new FileOutputStream(new File(path.replace(".","-highlight.")));
            document.write(out);
            out.close();
            return fileName + "-highlight." + fileType;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
