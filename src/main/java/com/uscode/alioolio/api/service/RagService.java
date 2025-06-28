package com.uscode.alioolio.api.service;

import com.uscode.alioolio.api.req.RagReq;
import com.uscode.alioolio.api.res.RagRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class RagService {
    public String searchRag(String query) {
        // 1. 벡터 검색 API 호출
        RestTemplate restTemplate = new RestTemplate();
        RagReq ragReq = new RagReq();
        ragReq.setQuery(query);
        ragReq.setTopK(5); // 상위 5개 결과를 요청

        RagRes response = restTemplate.postForObject(
                "https://35.216.51.152/search",
                ragReq,
                RagRes.class
        );
        // 2. 결과에서 텍스트 추출
        assert response != null;
        return response.getResult();
    }
}
