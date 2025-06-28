package com.uscode.alioolio.prompt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prompt_result")
public class PromptResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 36)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "reg_dt", nullable = false)
    private Instant regDt;

    @Size(max = 50)
    @NotNull
    @Column(name = "location", nullable = false, length = 50)
    private String location;

    @Size(max = 100)
    @NotNull
    @Column(name = "level", nullable = false, length = 100)
    private String level;

    @Size(max = 30)
    @NotNull
    @Column(name = "crop", nullable = false, length = 30)
    private String crop;

    @Size(max = 100)
    @NotNull
    @Column(name = "money", nullable = false, length = 100)
    private String money;

    @Size(max = 30)
    @NotNull
    @Column(name = "period", nullable = false, length = 30)
    private String period;

    @NotNull
    @Lob
    @Column(name = "summary", nullable = false)
    private String summary;

    @NotNull
    @Lob
    @Column(name = "full_report", nullable = false)
    private String fullReport;

}