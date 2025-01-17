package com.group8.meetingall.dto.tcasr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfoDTO {
    @JsonProperty(value = "Code")
    public String code;
    @JsonProperty(value = "Message")
    public String message;
}
