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
public class DescribeTaskStatusRequest {
    @JsonProperty(value = "TaskId")
    private Long taskId;
}

