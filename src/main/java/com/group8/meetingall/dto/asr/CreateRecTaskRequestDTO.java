package com.group8.meetingall.dto.asr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecTaskRequestDTO {
    @JsonProperty(value = "EngineModelType")
    private String engineModelType;
    @JsonProperty(value = "ChannelNum")
    private Long channelNum;
    @JsonProperty(value = "ResTextFormat")
    private Long resTextFormat;
    @JsonProperty(value = "SourceType")
    private Long sourceType;
    @JsonProperty(value = "Data")
    private String data;
    @JsonProperty(value = "DataLen")
    private Long dataLen;
}

