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
import java.util.List;
import java.util.UUID;

import static com.group8.meetingall.utils.DateTimeUtil.getCurrentDateTime;
import static java.util.Objects.nonNull;

@Service
public class MyMeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    public MeetingVo getActiveMeeting(String userId) {
        MeetingProfile meeting = meetingRepository.findActiveMeetingByUserId(userId);
        if (nonNull(meeting)) {
            return convertToMeetingVo(meeting);
        }
        return null;
    }

    public MeetingVo upsertMeeting(MeetingDto meetingDto) {
        MeetingProfile meetingProfile = new MeetingProfile();
        if (ObjectUtils.isEmpty(meetingDto.getMeetingId())) {
            meetingProfile = createMeeting(meetingDto);
            updateOtherMeetingToInActive(meetingDto.getUserId());
        } else {
            BeanUtils.copyProperties(meetingDto, meetingProfile);
        }
        boolean flag = meetingRepository.upsertMeeting(meetingProfile);
        if (flag) {
            return convertToMeetingVo(meetingProfile);
        }
        return null;
    }

    private void updateOtherMeetingToInActive(String userId) {
        MeetingProfile meeting = meetingRepository.findActiveMeetingByUserId(userId);
        meeting.setActive(false);
        meetingRepository.upsertMeeting(meeting);
    }

    private MeetingVo convertToMeetingVo(MeetingProfile m) {
        MeetingVo meetingVo = new MeetingVo();
        BeanUtils.copyProperties(m, meetingVo);
        return meetingVo;
    }

    private MeetingProfile createMeeting(MeetingDto meetingDto) {
        MeetingProfile meetingProfile = new MeetingProfile();
        BeanUtils.copyProperties(meetingDto, meetingProfile);
        meetingProfile.setMeetingId(UUID.randomUUID().toString());
        meetingProfile.setCreateTime(getCurrentDateTime());
        meetingProfile.setStatus("新建");
        meetingProfile.setActive(true);
        return meetingProfile;
    }

    public List<MeetingRecordVo> getMeetingRecords(String user) {
        List<MeetingRecordVo> meetingRecordVoList = new ArrayList<>();
        List<MeetingProfile> meetingProfiles = meetingRepository.findAllMeetingsByUserId(user);
        for(MeetingProfile meetingProfile : meetingProfiles){
            MeetingRecordVo meetingRecordVo = new MeetingRecordVo();
            StringBuilder room = new StringBuilder();
            room.append(meetingProfile.getRoom().get(0)).append("F ").append(meetingProfile.getRoom().get(0));
            meetingRecordVo.setMeetingRoom(room.toString());
            meetingRecordVo.setDate(meetingProfile.getStartTime());
            meetingRecordVo.setLanguage(String.valueOf(meetingProfile.getLanguage()));
            meetingRecordVo.setStatus(DateTimeUtil.isBefore(meetingProfile.getEndTime(), meetingProfile.getCreateTime()) ? "已结束" : "未开始");
            meetingRecordVo.setReportAddress(meetingProfile.getReportAddress());
            meetingRecordVo.setAudioAddress(meetingProfile.getAudioAddress());
            meetingRecordVoList.add(meetingRecordVo);
        }
        return meetingRecordVoList;
    }
}
