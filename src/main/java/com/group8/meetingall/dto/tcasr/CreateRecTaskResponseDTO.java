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
public class CreateRecTaskResponseDTO {
  @JsonProperty(value = "Response")
  public CreateRecTaskDTO response;
}
