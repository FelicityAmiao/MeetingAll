/*
 * Copyright (c) 2017-2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
public class TaskStatus {
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
    /**
     * 识别结果。
     */
    @JsonProperty(value = "Result")
    private String Result;

    @JsonProperty(value = "ResultDetail")
    private String ResultDetail;
    /**
     * 失败原因说明。
     */
    @JsonProperty(value = "ErrorMsg")
    private String ErrorMsg;


}

