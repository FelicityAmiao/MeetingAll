package com.group8.meetingall.dto.xfasr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestDataDTO {
    int status;
    String format;
    String encoding;
    String audio;
}
