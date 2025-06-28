package com.uscode.alioolio.chat.entity;

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
@Table(name = "audio_chat")
public class AudioChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 200)
    private String userId;

    @NotNull
    @Lob
    @Column(name = "req_data", nullable = false)
    private String reqData;

    @NotNull
    @Lob
    @Column(name = "res_data", nullable = false)
    private String resData;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "reg_dt", nullable = false)
    private Instant regDt;

}