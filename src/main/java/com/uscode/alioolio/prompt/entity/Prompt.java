package com.uscode.alioolio.prompt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "prompt")
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @Size(max = 100)
    @NotNull
    @Column(name = "provider", nullable = false, length = 100)
    private String provider;

    @Column(name = "max_tokens")
    private Integer maxTokens;

    @Column(name = "max_output_tokens")
    private Integer maxOutputTokens;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @ColumnDefault("1.00")
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @NotNull
    @ColumnDefault("1.00")
    @Column(name = "top_p", nullable = false)
    private Double topP;

    @Column(name = "top_k")
    private Integer topK;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_dt")
    private Instant regDt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "upd_dt")
    private Instant updDt;

    @NotNull
    @Lob
    @Column(name = "prompt", nullable = false)
    private String prompt;

    @NotNull
    @Lob
    @Column(name = "system_prompt", nullable = false)
    private String systemPrompt;

    @Size(max = 100)
    @NotNull
    @Column(name = "prompt_name", nullable = false, length = 100)
    private String promptName;

    @Size(max = 200)
    @NotNull
    @Column(name = "project_id", nullable = false, length = 200)
    private String projectId;

    @Size(max = 50)
    @NotNull
    @Column(name = "location", nullable = false, length = 50)
    private String location;

    @Size(max = 1)
    @NotNull
    @ColumnDefault("N")
    @Column(name = "use_yn", nullable = false, length = 1)
    private String useYn;

    @Size(max = 1)
    @NotNull
    @ColumnDefault("Y")
    @Column(name = "search", nullable = false, length = 1)
    private String search;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PromptCategory category;

}