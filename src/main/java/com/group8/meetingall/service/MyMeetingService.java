package com.group8.meetingall.service;

import com.group8.meetingall.dto.MeetingDto;
import com.group8.meetingall.entity.MeetingProfile;
import com.group8.meetingall.repository.MeetingRepository;
import com.group8.meetingall.vo.MeetingRecordVo;
import com.group8.meetingall.vo.MeetingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.group8.meetingall.utils.Constant.*;
import static com.group8.meetingall.utils.DateTimeUtil.getCurrentDateTime;
import static java.util.Objects.nonNull;

@Service
public class MyMeetingService {
    @Autowired
    CantoneseASRService cantoneseASRService;
    @Autowired
    HighFrequencyService highFrequencyService;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private ASRService asrService;

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
        if (nonNull(meeting)) {
            meeting.setActive(false);
            meetingRepository.upsertMeeting(meeting);
        }
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
        String currentDateTime = getCurrentDateTime();
        meetingProfile.setCreateTime(currentDateTime);
        meetingProfile.setStartDate(currentDateTime.substring(0, 10));
        meetingProfile.setStartTime(currentDateTime.substring(11));
        meetingProfile.setStatus(NEW);
        meetingProfile.setActive(true);
        return meetingProfile;
    }

    public List<MeetingRecordVo> getMeetingRecords(String user) {
        List<MeetingRecordVo> meetingRecordVoList = new ArrayList<>();
        List<MeetingProfile> meetingProfiles = meetingRepository.findAllMeetingsByUserId(user);
        for (MeetingProfile meetingProfile : meetingProfiles) {
            MeetingRecordVo meetingRecordVo = new MeetingRecordVo();
            BeanUtils.copyProperties(meetingProfile, meetingRecordVo);
            meetingRecordVoList.add(meetingRecordVo);
        }
        return meetingRecordVoList.stream()
                .sorted(Comparator.comparing(MeetingRecordVo::getStartDate))
                .sorted(Comparator.comparing(MeetingRecordVo::getStartTime))
                .collect(Collectors.toList());
    }

    public MeetingVo generateReport(String meetingId) {
        MeetingProfile meeting = meetingRepository.findMeetingByMeetingId(meetingId);
        if (meeting == null) {
            return null;
        }
        meeting.setStatus(REPORT_ING);
        String fileName = generateReportName(meeting);
        switch (meeting.getLanguage()) {
            case 1:
                CompletableFuture.supplyAsync(() -> {
                    String uuid = asrService.convert(meeting.getAudioAddress());
                    generateWordFile(meeting, fileName, uuid);
                    return fileName;
                });
                meetingRepository.upsertMeeting(meeting);
                return convertToMeetingVo(meeting);
            case 2:
                CompletableFuture.supplyAsync(() -> {
                    String uuid = cantoneseASRService.startConvert(meeting.getAudioAddress());
                    generateWordFile(meeting, fileName, uuid);
                    return fileName;
                });
                boolean flag = meetingRepository.upsertMeeting(meeting);
                System.out.println("flag: " + flag);
                return convertToMeetingVo(meeting);
            default:
                break;
        }
        return convertToMeetingVo(meeting);
    }

    private void generateWordFile(MeetingProfile meeting, String fileName, String uuid) {
        highFrequencyService.generateHighlightWordFile(uuid, fileName);
        meeting.setReportAddress(fileName);
        meeting.setStatus(REPORT_FINISHED);
        meetingRepository.upsertMeeting(meeting);
    }

    private String generateReportName(MeetingProfile meetingProfile) {
        String reportName = "Meeting Report " + meetingProfile.getSubject() + SPACE + meetingProfile.getRoom().get(0) + SPACE + meetingProfile.getRoom().get(1) + SPACE + meetingProfile.getStartDate() + ".docx";
        reportName = reportName.replaceAll(SPACE, UNDERLINE);
        return reportName;
    }
}
