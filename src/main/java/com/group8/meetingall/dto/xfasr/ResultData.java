package com.group8.meetingall.dto.xfasr;

import com.group8.meetingall.service.XFCantoneseASRService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResultData {
    private int status;
    private Result result;
}
