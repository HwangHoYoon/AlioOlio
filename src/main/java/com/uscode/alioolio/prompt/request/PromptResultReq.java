package com.uscode.alioolio.prompt.request;

import lombok.Data;

import java.time.Instant;

@Data
public class PromptResultReq {
    private Integer id;

    private String userId;

    private Instant regDt;

    private String location;

    private String level;

    private String crop;

    private String money;

    private String period;

    private String summary;

    private String fullReport;
}
