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
public class TaskStatusDTO {
    @JsonProperty(value = "TaskId")
    private Long TaskId;
    /**
     * 任务状态码，0：任务等待，1：任务执行中，2：任务成功，3：任务失败。
     */
    @JsonProperty(value = "Status")
    private Long Status;
    /**
     * 任务状态，waiting：任务等待，doing：任务执行中，success：任务成功，failed：任务失败。
     */
    @JsonProperty(value = "StatusStr")
    private String StatusStr;
    @JsonProperty(value = "Result")
    private String Result;
    @JsonProperty(value = "ResultDetail")
    private String ResultDetail;
    @JsonProperty(value = "ErrorMsg")
    private String ErrorMsg;


}

