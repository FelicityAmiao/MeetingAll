package com.group8.meetingall.service.ASR;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    @JsonProperty(value = "Code")
    public String code;
    @JsonProperty(value = "Message")
    public String message;
}
