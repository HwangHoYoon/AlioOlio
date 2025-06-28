package com.uscode.alioolio.result.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uscode.alioolio.api.google.vertex.service.VertexService;
import com.uscode.alioolio.prompt.request.PromptResultReq;
import com.uscode.alioolio.prompt.response.PromptRes;
import com.uscode.alioolio.prompt.response.PromptResultRes;
import com.uscode.alioolio.prompt.service.PromptResultService;
import com.uscode.alioolio.prompt.service.PromptService;
import com.uscode.alioolio.result.res.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultService {

    private final VertexService vertexService;
    private final PromptService promptService;
    private final PromptResultService promptResultService;

    public RstSt1Res step1() throws IOException {
        RstSt1Res rstSt1Res = new RstSt1Res();
        PromptRes prompt = promptService.getPrompt("ST1");
        String st1Result = vertexService.chat(prompt);
        List<String> list = getJsonForList(st1Result);
        rstSt1Res.setLocationList(list);
        return rstSt1Res;
    }

    public RstSt2Res step2(String location) throws IOException {
        RstSt2Res rstSt2Res = new RstSt2Res();
        PromptRes prompt = promptService.getPrompt("ST2");
        String promptText = prompt.getPrompt();
        prompt.setPrompt(promptText.replace("${location}", location));
        String st2Result = vertexService.chat(prompt);
        List<String> list = getJsonForList(st2Result);
        rstSt2Res.setCropList(list);
        return rstSt2Res;
    }

    private List<String> getJsonForList(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, List.class);
    }

    public RstSt3A4Res step3And4(String location, String crop, String money, String level) throws IOException {
        RstSt3A4Res rstSt3A4Res = new RstSt3A4Res();
        PromptRes prompt = promptService.getPrompt("ST3");
        String promptText = prompt.getPrompt()
                .replace("${location}", location)
                .replace("${level}", level)
                .replace("${crop}", crop)
                .replace("${money}", money)
                .replace("${period}", "선택 안함");
        prompt.setPrompt(promptText);
        String st3Result = vertexService.chat(prompt);
        ObjectMapper mapper = new ObjectMapper();
        RstSt3Res rstSt3Res = mapper.readValue(st3Result, RstSt3Res.class);

        PromptRes lastPrompt = promptService.getPrompt("ST4");
        String lastPromptText = lastPrompt.getPrompt()
                .replace("${location}", rstSt3Res.getLocation())
                .replace("${level}", rstSt3Res.getUserLevel())
                .replace("${crop}", rstSt3Res.getFinalRecommendedCrop())
                .replace("${money}", rstSt3Res.getEstimatedBudget())
                .replace("${period}", rstSt3Res.getPreparationPeriod());
        lastPrompt.setPrompt(lastPromptText);
        String st4Result = vertexService.chat(lastPrompt);
        RstSt4Res rstSt4Res = mapper.readValue(st4Result, RstSt4Res.class);

        rstSt3A4Res.setLocation(rstSt3Res.getLocation());
        rstSt3A4Res.setCrop(rstSt3Res.getFinalRecommendedCrop());
        rstSt3A4Res.setMoney(rstSt3Res.getEstimatedBudget());
        rstSt3A4Res.setLevel(rstSt3Res.getUserLevel());
        rstSt3A4Res.setPeriod(rstSt3Res.getPreparationPeriod());
        rstSt3A4Res.setSummary(rstSt4Res.getSummary());
        rstSt3A4Res.setFullReport(rstSt4Res.getFullReport());

         String userId = UUID.randomUUID().toString();

        PromptResultReq promptResultReq = new PromptResultReq();
        promptResultReq.setLocation(rstSt3A4Res.getLocation());
        promptResultReq.setCrop(rstSt3A4Res.getCrop());
        promptResultReq.setMoney(rstSt3A4Res.getMoney());
        promptResultReq.setLevel(rstSt3A4Res.getLevel());
        promptResultReq.setPeriod(rstSt3A4Res.getPeriod());
        promptResultReq.setSummary(rstSt3A4Res.getSummary());
        promptResultReq.setFullReport(rstSt3A4Res.getFullReport());
        promptResultReq.setUserId(userId);
        promptResultService.savePromptResult(promptResultReq);

        rstSt3A4Res.setUserId(userId);
        return rstSt3A4Res;
    }

    public PromptResultRes getResult(String userId) {
        return promptResultService.getPromptResult(userId);
    }
}
