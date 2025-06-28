package com.uscode.alioolio.api.service;

import com.uscode.alioolio.api.req.ApiReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleService implements ApiService {

    @Override
    public String callApi(ApiReq apiReq) {
        return "";
    }

    @Override
    public Flux<String> callApiForStream(ApiReq apiReq) {
        return null;
    }
}
