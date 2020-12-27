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
public class CreateRecTaskResponse {
    @JsonProperty(value = "Data")
    private Task data;
    @JsonProperty(value = "RequestId")
    private String requestId;
}

