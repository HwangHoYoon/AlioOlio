package com.uscode.alioolio.api.req;

import lombok.Data;

@Data
public class RagReq {
    private String query;
    private int topK;
}
