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
public class ASRErrDTO {
  @JsonProperty(value = "RequestId")
  public String requestId;
  @JsonProperty(value = "Error")
  public ErrorInfoDTO error;
}
