package com.uscode.alioolio.prompt.response;

import com.uscode.alioolio.chat.res.AudioChatRes;
import com.uscode.alioolio.prompt.entity.PromptCategory;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PromptRes {

    private Long id;

    private String modelName;

    private String provider;

    private Integer maxTokens;

    private Integer maxOutputTokens;

    private String description;

    private Double temperature;

    private Double topP;

    private Integer topK;

    private Instant regDt;

    private Instant updDt;

    private String prompt;

    private String systemPrompt;

    private String promptName;

    private String projectId;

    private String location;

    private String useYn;

    private String search;

    private PromptCategory category;

    private List<AudioChatRes> historyList;
}
