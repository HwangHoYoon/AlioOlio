package com.uscode.alioolio.prompt.response;

import lombok.Data;

import java.time.Instant;

@Data
public class PromptResultRes {
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
