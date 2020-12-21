package com.group8.meetingall.utils;

import com.group8.meetingall.entity.CustomRun;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightUtil {
    public static String preProcessText(String text, List<String> words) {
        for (String word : words) {
            Pattern p = Pattern.compile(word);
            Matcher m = p.matcher(text);
            if (m.find()) {
                text = m.replaceAll("<" + word + "<");
            }
        }
        return text;
    }

    public static XWPFDocument createWord(String text, List<String> keywords) throws Exception {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleParagraphRun = titleParagraph.createRun();
        LocalDate localDate = LocalDate.now();
        titleParagraphRun.setText("Meeting Report-" + localDate.toString());
        titleParagraphRun.setColor(Color.BLACK.getColor());
        titleParagraphRun.setFontSize(20);
        XWPFParagraph paragraph = document.createParagraph();
        String preProcessStr = preProcessText(text, keywords);
        List<CustomRun> customRuns = generateCustomRuns(preProcessStr, keywords);
        generateHighLightRuns(paragraph, customRuns);

        return document;
    }

    private static void generateHighLightRuns(XWPFParagraph paragraph, List<CustomRun> customRuns) {
        customRuns.forEach(customRun -> {
            XWPFRun run = paragraph.createRun();
            run.setText(customRun.getText());
            if (customRun.isHighlight()) {
                CTShd ctShd = run.getCTR().addNewRPr().addNewShd();
                ctShd.setFill(Color.YELLOW.getColor());
            }
        });
    }

    public static List<CustomRun> generateCustomRuns(String text, List<String> keywords) {
        List<String> keys = new ArrayList<>(Arrays.asList(text.split("<")));
        List<CustomRun> customRunList = new ArrayList<>();
        for (String key : keys) {
            CustomRun customRun = new CustomRun(key);
            if (keywords.contains(key)) {
                customRun.setHighlight(true);
            }
            customRunList.add(customRun);
        }
        customRunList.removeIf(customRun -> customRun.getText().isEmpty());
        return customRunList;
    }
}
