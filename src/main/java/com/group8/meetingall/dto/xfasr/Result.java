package com.group8.meetingall.dto.xfasr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    int bg;
    int ed;
    boolean ls;//是否是最后一片结果
    String pgs;
    int[] rg;
    int sn;//返回结果的序号
    Ws[] ws;//听写结果
    public Text getText() {
        Text text = new Text();
        StringBuilder sb = new StringBuilder();
        for (Ws ws : this.ws) {
            sb.append(ws.cw[0].w);
        }
        text.sn = this.sn;
        text.text = sb.toString();
        text.ls = this.ls;
        return text;
    }
}
