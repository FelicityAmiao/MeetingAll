package com.group8.meetingall.dto.asr;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ASRAPIResultDto {
	private int ok;
	private int err_no;
	private String failed;
	private String data;
}