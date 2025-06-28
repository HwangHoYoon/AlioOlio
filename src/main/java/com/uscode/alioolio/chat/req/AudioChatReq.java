package com.uscode.alioolio.chat.req;

import lombok.Data;

import java.time.Instant;

@Data
public class AudioChatReq {
    private Integer id;

    private String userId;

    private String reqData;

    private String resData;

    private Instant regDt;

}