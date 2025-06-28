package com.uscode.alioolio.chat.controller;

import com.uscode.alioolio.chat.req.ChatReq;
import com.uscode.alioolio.chat.res.ChatRes;
import com.uscode.alioolio.chat.service.ChatService;
import com.uscode.alioolio.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "chat", description = "채팅 API")
@RequestMapping("chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "질문", description = "질문")
    public String chat(@Schema(description = "content", example = "테스트", name = "content") String content) throws IOException, ExecutionException {
        return chatService.chat(content);
    }

    /*@GetMapping(value = "/chatToStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "질문", description = "질문")
    public Flux<String> streamChat(@Schema(description = "content", example = "테스트", name = "content") String content,
                                   @Schema(description = "search", example = "false", name = "search") boolean search
    ) {
        return Flux.create(emitter -> {
            try {
                chatService.streamChat(content, search, emitter::next);
                emitter.complete();
            } catch (Exception e) {
                log.error("Error during chat streaming", e);
                emitter.error(e);
            }
        });
    }

    @PostMapping(path = "/audioToChat", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "질문", description = "질문")
    public String transcribeAudio(
            @RequestPart(value = "file") MultipartFile audioFile
    ) {
        if (audioFile.isEmpty()) {
            throw new CommonException("Please select an audio file to upload", HttpStatus.BAD_REQUEST.name());
        }

        try {
            byte[] audioBytes = audioFile.getBytes();
            return chatService.transcribeAudio(audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("Failed to transcribe audio: " + e.getMessage(), HttpStatus.BAD_REQUEST.name());
        } catch (IllegalArgumentException e) {
            // 예를 들어 encoding 파라미터가 유효하지 않은 경우
            throw new CommonException("Invalid parameter: " + e.getMessage(), HttpStatus.BAD_REQUEST.name());
        }
    }*/

    /**
     * 텍스트를 음성으로 변환하여 오디오 스트림으로 반환하는 API 엔드포인트.
     * /synthesize?text=안녕하세요&languageCode=ko-KR&voiceName=ko-KR-Wavenet-A&audioEncoding=MP3
     */
    @PostMapping("/chatToAudio")
    @Operation(summary = "질문", description = "음성답변")
    public ChatRes chatToAudio(
            @RequestBody ChatReq chatReq
            ) {
        String text = chatReq.getText();
        String userId = chatReq.getUserId();
        if (text == null || text.trim().isEmpty()) {
            throw new CommonException("Text parameter is required.", HttpStatus.BAD_REQUEST.name());
        }
        try {
            String aiResulText = chatService.audioChat(userId, text);
            byte[] audioBytes = chatService.synthesizeSpeech(aiResulText);
            String base64Audio = Base64.encodeBase64String(audioBytes);

            ChatRes chatRes = new ChatRes();
            chatRes.setText(aiResulText);
            chatRes.setAudio(base64Audio);
            return chatRes;
        } catch (IllegalArgumentException e) {
            // 예를 들어 audioEncoding 파라미터가 유효하지 않은 경우
            throw new CommonException("Invalid parameter: " + e.getMessage(), HttpStatus.BAD_REQUEST.name());
        } catch (IOException e) {
            throw new CommonException("Failed to synthesize speech: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

}
