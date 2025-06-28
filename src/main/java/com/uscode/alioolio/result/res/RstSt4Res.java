package com.uscode.alioolio.result.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RstSt4Res {

    @JsonProperty("summary")  // JSON 키가 "region_name"일 경우 매핑
    private String summary;

    @JsonProperty("full_report")  // JSON 키가 "region_name"일 경우 매핑
    private String fullReport;
}
