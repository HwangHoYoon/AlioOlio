package com.uscode.alioolio.chat.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.uscode.alioolio.api.google.stt.service.SpeechService;
import com.uscode.alioolio.api.google.tts.service.TextToSpeechService;
import com.uscode.alioolio.api.google.vertex.service.VertexService;
import com.uscode.alioolio.chat.req.AudioChatReq;
import com.uscode.alioolio.chat.res.AudioChatRes;
import com.uscode.alioolio.prompt.response.PromptRes;
import com.uscode.alioolio.prompt.response.PromptResultRes;
import com.uscode.alioolio.prompt.service.PromptResultService;
import com.uscode.alioolio.prompt.service.PromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final VertexService vertexService;
    private final SpeechService speechService;
    private final TextToSpeechService textToSpeechService;
    private final AudioChatService audioChatService;
    private final PromptResultService promptResultService;
    private final PromptService promptService;
    public String chat(String content) throws IOException {
        // return vertexService.chat(content);
        return "";
    }

    public void streamChat(String content, boolean search, Consumer<String> onText) {
        //vertexService.streamChat(content, search, onText);
    }

    public String transcribeAudio(byte[] audioBytes) throws IOException {
        return speechService.transcribeAudio(audioBytes);
    }

    public byte[] synthesizeSpeech(String text) throws IOException {
        return textToSpeechService.synthesizeSpeech(text);
    }

    public String audioChat(String userId, String text) throws IOException {
        // 결과 조회
        PromptResultRes promptResultRes = promptResultService.getPromptResult(userId);

        // 프롬프트 조회
        PromptRes prompt = promptService.getPrompt("ST5");

        // 프롬프트 바꿔치기
        String systemPrompt = prompt.getSystemPrompt()
                .replace("${location}", promptResultRes.getLocation())
                .replace("${level}", promptResultRes.getLevel())
                .replace("${crop}", promptResultRes.getCrop())
                .replace("${money}", promptResultRes.getMoney())
                .replace("${period}", promptResultRes.getPeriod());
        prompt.setSystemPrompt(systemPrompt);

        //TODO 히스토리 조회 및 추가
        // 히스토리 조회
        List<AudioChatRes> audioChatResList = audioChatService.getAudioChatList(userId);
        // 히스토리 추가
        prompt.setHistoryList(audioChatResList);

        // 채팅 요청
        String promptText = prompt.getPrompt().replace("${사용자 목소리 입력값}$", text);
        prompt.setPrompt(promptText);
        String resultText = vertexService.chat(prompt);

        // 결과 히스토리 저장
        AudioChatReq audioChatReq = new AudioChatReq();
        audioChatReq.setUserId(userId);
        audioChatReq.setReqData(promptText);
        audioChatReq.setResData(resultText);
        audioChatService.saveAudioChat(audioChatReq);
        return resultText;
    }

    private static final String PROJECT_ID = "uscode-porject";
    private static final String LOCATION = "us-central1"; // 또는 asia-northeast3
    private static final String MODEL_ID = "gemini-2.5-flash";
    private static final String DATA_STORE_ID= "6917529027641081856 ";

    /*public String ragTest(String content) throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream("C:/work/uscode/key/uscode-porject-key.json"))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        credentials.refreshIfExpired();
        String accessToken = credentials.getAccessToken().getTokenValue();

        WebClient client = WebClient.builder()
                .baseUrl("https://us-central1-aiplatform.googleapis.com")
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .build();


        Map<String, Object> requestBody = Map.of(
                "instances", List.of(
                        Map.of(
                                "contents", Map.of(
                                        "role", "user",
                                        "parts", List.of(
                                                Map.of("text", "Your query related to the corpus content.")
                                        )
                                )
                        )
                ),
                "parameters", Map.of(
                        "temperature", 0.2,
                        "maxOutputTokens", 1024,
                        "topP", 0.8,
                        "topK", 40,
                        "grounding_config", Map.of(
                                "grounding_source_name", "projects/uscode-porject/locations/us-central1/ragCorpora/6917529027641081856",
                                "query_triggers", Map.of(
                                        "query_mode", "PROMPT_ONLY"
                                ),
                                "retrieval_parameters", Map.of(
                                        "num_retrieved_documents", 3
                                )
                        )
                )
        );


        String modelUri = String.format(
                "/v1/projects/%s/locations/%s/publishers/google/models/%s:generateContent",
                PROJECT_ID, LOCATION, MODEL_ID
        );

        String response = client.post()
                .uri(modelUri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    return Mono.just("ERROR");
                })
                .block();

        System.out.println("응답 결과:\n" + response);
        return response;
    }*/

    public String ragTest(String content) throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream("C:/work/uscode/key/uscode-porject-key.json"))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        credentials.refreshIfExpired();
        String accessToken = credentials.getAccessToken().getTokenValue();
        String engineName = "test-sech_1751086257778";
        String projectId = "1054525610472";

        WebClient client = WebClient.builder()
                .baseUrl("https://discoveryengine.googleapis.com")
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .defaultHeader("Content-Type", "application/json")
                .build();

        Map<String, Object> requestBody = Map.of(
                "query", "hello",
                "pageSize", 10,
                "queryExpansionSpec", Map.of("condition", "AUTO"),
                "spellCorrectionSpec", Map.of("mode", "AUTO"),
                "languageCode", "ko",
                "contentSearchSpec", Map.of(
                        "extractiveContentSpec", Map.of("maxExtractiveAnswerCount", 1)
                ),
                "userInfo", Map.of("timeZone", "Asia/Seoul")
        );

        String url = String.format(
                "/v1alpha/projects/%s/locations/global/collections/default_collection/engines/%s/servingConfigs/default_search:search",
                projectId, engineName
        );

        String response = client.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just("ERROR");
                })
                .block();

        System.out.println("응답 결과:\n" + response);
        return response;

    }
}
