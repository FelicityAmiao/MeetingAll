package com.group8.meetingall.dto.baidutrans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TranslateResultDTO {
    String from;
    String to;
    List<ResultDTO> trans_result;
}

