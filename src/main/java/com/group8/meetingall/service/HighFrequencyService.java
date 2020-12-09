package com.group8.meetingall.service;

import com.group8.meetingall.vo.Keyword;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HighFrequencyService {

    public List<Keyword> getHighFrequencyWords(String text,Integer count);

}
