package com.uscode.alioolio.prompt.service;

import com.uscode.alioolio.prompt.entity.PromptResult;
import com.uscode.alioolio.prompt.repository.PromptResultRepository;
import com.uscode.alioolio.prompt.request.PromptResultReq;
import com.uscode.alioolio.prompt.response.PromptResultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromptResultService {
    private final PromptResultRepository promptResultRepository;

    public void savePromptResult(PromptResultReq promptResultReq) {
        PromptResult promptResult = PromptResult.builder()
                .userId(promptResultReq.getUserId())
                .location(promptResultReq.getLocation())
                .crop(promptResultReq.getCrop())
                .money(promptResultReq.getMoney())
                .period(promptResultReq.getPeriod())
                .level(promptResultReq.getLevel())
                .summary(promptResultReq.getSummary())
                .fullReport(promptResultReq.getFullReport())
                .regDt(Instant.now())
                .build();
        promptResultRepository.save(promptResult);
    }

    public PromptResultRes getPromptResult(String userId) {
        PromptResult result = promptResultRepository.findByUserId(userId);
        PromptResultRes promptResultRes = new PromptResultRes();
        promptResultRes.setUserId(result.getUserId());
        promptResultRes.setLocation(result.getLocation());
        promptResultRes.setCrop(result.getCrop());
        promptResultRes.setMoney(result.getMoney());
        promptResultRes.setPeriod(result.getPeriod());
        promptResultRes.setLevel(result.getLevel());
        promptResultRes.setSummary(result.getSummary());
        promptResultRes.setFullReport(result.getFullReport());
        promptResultRes.setRegDt(result.getRegDt());
        return promptResultRes;
    }
}
