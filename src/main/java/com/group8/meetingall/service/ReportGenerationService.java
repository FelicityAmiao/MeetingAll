package com.group8.meetingall.service;

import com.group8.meetingall.repository.TranslateResultRepository;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportGenerationService {
    @Autowired
    TranslateResultRepository translateResultRepository;


    public XWPFDocument generateMeetingReport(String uuid)
            throws IOException {
        String reportContent = translateResultRepository.getFileContent(uuid);
        return getXWPFDocument(reportContent);
    }

    private XWPFDocument getXWPFDocument(String reportContent) throws IOException {
        XWPFDocument document = new XWPFDocument(this.getClass().getResourceAsStream("/template/MeetingReportTemplate.docx"));
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.text();
                String replacedText = text.replaceAll("Content", reportContent);
                run.setText(replacedText, 0);
            }
        }
        return document;
    }

}
