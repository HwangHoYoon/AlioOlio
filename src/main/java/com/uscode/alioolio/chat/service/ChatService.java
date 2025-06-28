package com.uscode.alioolio.chat.service;

import com.uscode.alioolio.api.google.stt.service.SpeechService;
import com.uscode.alioolio.api.google.tts.service.TextToSpeechService;
import com.uscode.alioolio.api.google.vertex.service.VertexService;
import com.uscode.alioolio.chat.req.AudioChatReq;
import com.uscode.alioolio.prompt.response.PromptRes;
import com.uscode.alioolio.prompt.response.PromptResultRes;
import com.uscode.alioolio.prompt.service.PromptResultService;
import com.uscode.alioolio.prompt.service.PromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        //List<AudioChatRes> audioChatResList = audioChatService.getAudioChatList(userId);
        // 히스토리 추가
        //prompt.setHistoryList(audioChatResList);

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
}
