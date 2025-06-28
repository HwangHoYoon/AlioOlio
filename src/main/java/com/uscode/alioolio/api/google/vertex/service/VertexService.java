package com.uscode.alioolio.api.google.vertex.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.Tool;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.uscode.alioolio.chat.res.AudioChatRes;
import com.uscode.alioolio.prompt.response.PromptRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class VertexService {
    // Vertex AI와의 통신을 위한 서비스 메소드들을 구현합니다.
    // 예: 질문을 보내고 응답을 받는 메소드 등

    public String chat(PromptRes promptRes) throws IOException {
        // Vertex AI에 질문을 보내고 응답을 받는 로직을 구현합니다.
        // 현재는 테스트용으로 간단한 문자열을 반환합니다.
        log.info("응답을 시작합니다...");
        log.info("========================================");

        String result = generateContent(promptRes);

        log.info("\n========================================");
        log.info("응답이 완료되었습니다.");

        return result;
    }

    public String generateContent (PromptRes promptRes) throws IOException {
        String projectId = promptRes.getProjectId();
        String location = promptRes.getLocation(); // 예: "us-central1"
        String prompt = promptRes.getPrompt();
        String modelName = promptRes.getModelName();
        String systemPrompt = promptRes.getSystemPrompt();
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            Tool googleSearchTool = Tool.newBuilder()
                    .setGoogleSearch(Tool.GoogleSearch.newBuilder())
                    .build();

            // 1. 시스템 안내 (System Instruction) 정의
            // 모델의 역할과 출력 규칙을 명시합니다.
/*            Content systemInstruction = Content.newBuilder()
                    .addParts(Part.newBuilder().setText(
                            "너는 음식 추천 전문가야. 사용자가 재료를 말하면, 그 재료로 만들 수 있는 창의적인 요리 이름과 간단한 1줄 설명을 JSON 형식으로 응답해야 해. JSON 키는 'recipe_name'과 'description'을 사용해줘."))
                    .build();*/

            if (StringUtils.isBlank(prompt)) {
                prompt = systemPrompt;
                systemPrompt = "";
            }
            Content systemInstruction = null;
            if (StringUtils.isNotBlank(systemPrompt)) {
                systemInstruction = Content.newBuilder()
                        .addParts(Part.newBuilder().setText(systemPrompt))
                        .build();
            }

            List<AudioChatRes> audioChatResList =  promptRes.getHistoryList();

            List<Content> historyList = new ArrayList<>();
            if (audioChatResList != null && !audioChatResList.isEmpty()) {
                List<AudioChatRes> reversedList = new ArrayList<>(audioChatResList);  // 복사
                Collections.reverse(reversedList);  // 역순 정렬
                for (AudioChatRes audioChatRes : reversedList) {
                    // 히스토리에서 사용자와 모델의 역할을 구분하여 Content 객체 생성
                    historyList.add(Content.newBuilder()
                            .setRole("user")
                            .addParts(Part.newBuilder().setText(audioChatRes.getResData()))
                            .build());

                    historyList.add(Content.newBuilder()
                            .setRole("model")
                            .addParts(Part.newBuilder().setText(audioChatRes.getResData()))
                            .build());
                }
            }
            // 3. 최종 사용자 프롬프트(User Prompt) 정의
            // 모델에게 실제로 답변을 요청할 질문입니다.
            Content finalUserPrompt = Content.newBuilder()
                    .setRole("user")
                    .addParts(Part.newBuilder().setText(prompt))
                    .build();
            historyList.add(finalUserPrompt);

            // 5. 모델 초기화 및 시스템 안내 설정
            // GenerativeModel.Builder를 사용하여 모델을 생성할 때 시스템 안내를 주입합니다.
            GenerativeModel model;
            if (systemInstruction != null) {
                model = new GenerativeModel.Builder()
                        .setModelName(modelName)
                        .setVertexAi(vertexAi)
                        .setTools(Collections.singletonList(googleSearchTool))
                        .setSystemInstruction(systemInstruction) // 여기에 시스템 안내를 설정합니다.
                        .build();
            } else {
                model = new GenerativeModel.Builder()
                        .setModelName(modelName)
                        .setVertexAi(vertexAi)
                        .setTools(Collections.singletonList(googleSearchTool))
                        .build();
            }
            GenerateContentResponse generateContentResponse = model.generateContent(historyList);

            // 결과 텍스트 가져오기
            return ResponseHandler.getText(generateContentResponse);
        }
    }
   /* public void streamChat(String content, boolean search, Consumer<String> onText) {
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {

            Tool googleSearchTool = Tool.newBuilder()
                    .setGoogleSearch(Tool.GoogleSearch.newBuilder())
                    .build();

            GenerativeModel model = new GenerativeModel.Builder()
                    .setModelName(modelName)
                    .setVertexAi(vertexAi)
                    .setTools(Collections.singletonList(googleSearchTool))
                    .build();

            Content prompt = Content.newBuilder()
                    .setRole("user")
                    .addParts(Part.newBuilder().setText(content))
                    .build();

            GenerationConfig config = GenerationConfig.newBuilder()
                    .setTemperature(0.7f)
                    .setTopP(0.8f)
                    .build();

            model.withGenerationConfig(config);

            ResponseStream<GenerateContentResponse> stream = model.generateContentStream(prompt);

            for (GenerateContentResponse response : stream) {
                onText.accept(ResponseHandler.getText(response));
            }
        }
        catch (IOException e) {
            log.error("Vertex AI 스트리밍 요청 중 오류 발생: {}", e.getMessage());
        }
        catch (Exception e) {
            log.error("Vertex AI 스트리밍 요청 중 예외 발생: {}", e.getMessage());
        }
        finally {
            log.info("스트리밍 요청이 완료되었습니다.");
        }
    }*/
}
