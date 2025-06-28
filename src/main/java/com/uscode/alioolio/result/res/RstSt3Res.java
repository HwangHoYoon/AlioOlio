package com.uscode.alioolio.result.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RstSt3Res {

    @JsonProperty("location")  // JSON 키가 "region_name"일 경우 매핑
    private String location;

    @JsonProperty("user_level")
    private String userLevel;

    @JsonProperty("final_recommended_crop")
    private String finalRecommendedCrop;

    @JsonProperty("estimated_budget")
    private String estimatedBudget;

    @JsonProperty("preparation_period")
    private String preparationPeriod;

}
