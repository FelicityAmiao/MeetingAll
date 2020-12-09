package com.group8.meetingall.controller;

import com.group8.meetingall.service.HighFrequencyService;
import com.group8.meetingall.vo.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sentence")
@RestController
public class HighFrequencyController {

    @Autowired
    private HighFrequencyService highFrequencyService;

    @PostMapping("/highFrequencyWords")
    public List<Keyword> getHighFrequencyWords(@RequestParam String text,@RequestParam Integer count){
        return highFrequencyService.getHighFrequencyWords(text, count);
    }

}