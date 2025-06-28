package com.uscode.alioolio.chat.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioChatRes {
    private Integer id;

    private String userId;

    private String reqData;

    private String resData;

    private Instant regDt;

}