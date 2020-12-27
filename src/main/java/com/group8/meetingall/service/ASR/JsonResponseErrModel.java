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
public class JsonResponseErrModel {
  @JsonProperty(value = "RequestId")
  public String requestId;
  @JsonProperty(value = "Error")
  public ErrorInfo error;
}
