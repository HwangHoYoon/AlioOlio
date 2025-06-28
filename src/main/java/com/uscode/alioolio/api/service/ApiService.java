package com.uscode.alioolio.api.service;

import com.uscode.alioolio.api.req.ApiReq;
import reactor.core.publisher.Flux;

public interface ApiService {

    String callApi(ApiReq apiReq);
    Flux<String> callApiForStream(ApiReq apiReq);

}
