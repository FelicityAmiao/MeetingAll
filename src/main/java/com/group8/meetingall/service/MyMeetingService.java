package com.group8.meetingall.service;

import com.group8.meetingall.dto.MeetingDto;
import com.group8.meetingall.entity.MeetingProfile;
import com.group8.meetingall.repository.MeetingRepository;
import com.group8.meetingall.utils.DateTimeUtil;
import com.group8.meetingall.vo.MeetingRecordVo;
import com.group8.meetingall.vo.MeetingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.group8.meetingall.utils.DateTimeUtil.getCurrentDateTime;

@Service
public class MyMeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    public List<MeetingVo> findAllMeetingsByUserId(String userId) {
        List<MeetingProfile> allMeetings = meetingRepository.findAllMeetingsByUserId(userId);
        return allMeetings.stream().map(this::convertToMeetingVo).collect(Collectors.toList());
    }

    public List<MeetingVo> upsertMeeting(MeetingDto meetingDto) {
        MeetingProfile meetingProfile = new MeetingProfile();
        List<MeetingProfile> allMeetings = new ArrayList<>();
        if (ObjectUtils.isEmpty(meetingDto.getMeetingId())) {
            meetingProfile = createMeeting(meetingDto);
        } else {
            BeanUtils.copyProperties(meetingDto, meetingProfile);
        }
        boolean flag = meetingRepository.upsertMeeting(meetingProfile);
        if (flag) {
            allMeetings = meetingRepository.findAllMeetingsByUserId(meetingDto.getUserId());
        }
        return allMeetings.stream().map(this::convertToMeetingVo).collect(Collectors.toList());
    }

    private MeetingVo convertToMeetingVo(MeetingProfile m) {
        MeetingVo meetingVo = new MeetingVo();
        BeanUtils.copyProperties(m, meetingVo);
        meetingVo.setTime(Arrays.asList(DateTimeUtil.toLocalDateTimeWithDefaultFormat(m.getStartTime()), DateTimeUtil.toLocalDateTimeWithDefaultFormat(m.getEndTime())));
        if (DateTimeUtil.isBefore(m.getEndTime(), m.getCreateTime())) {
            meetingVo.setFinished(true);
        }else{
            meetingVo.setFinished(false);
        }
        return meetingVo;
    }

    private MeetingProfile createMeeting(MeetingDto meetingDto) {
        MeetingProfile meetingProfile = new MeetingProfile();
        BeanUtils.copyProperties(meetingDto, meetingProfile);
        meetingProfile.setMeetingId(UUID.randomUUID().toString());
        meetingProfile.setCreateTime(getCurrentDateTime());
        return meetingProfile;
    }

    public List<MeetingVo> deleteMeeting(String userId, String meetingId) {
        boolean flag = meetingRepository.removeMeeting(userId, meetingId);
        List<MeetingProfile> allMeetings = new ArrayList<>();
        if (flag) {
            allMeetings = meetingRepository.findAllMeetingsByUserId(userId);
        }
        return allMeetings.stream().map(this::convertToMeetingVo).collect(Collectors.toList());
    }

    public List<MeetingRecordVo> getMeetingRecords(String user) {
        List<MeetingRecordVo> meetingRecordVoList = new ArrayList<>();
        List<MeetingProfile> meetingProfiles = meetingRepository.findAllMeetingsByUserId(user);
        for(MeetingProfile meetingProfile : meetingProfiles){
            MeetingRecordVo meetingRecordVo = new MeetingRecordVo();
            meetingRecordVo.setRoom(meetingProfile.getRoom());
            meetingRecordVo.setDate(meetingProfile.getStartTime());
            meetingRecordVo.setLanguage(meetingProfile.getLanguage());
            meetingRecordVo.setStatus(meetingProfile.getStatus());
            meetingRecordVo.setReportAddress(meetingProfile.getReportAddress());
            meetingRecordVo.setAudioAddress(meetingProfile.getAudioAddress());
            meetingRecordVoList.add(meetingRecordVo);
        }
        return meetingRecordVoList;
    }
}
