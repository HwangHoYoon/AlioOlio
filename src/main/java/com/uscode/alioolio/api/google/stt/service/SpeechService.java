package com.uscode.alioolio.api.google.stt.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpeechService {

    private final String languageCode = "ko-KR"; // 기본 언어 코드 (예: 한국어)
    private final int sampleRateHertz = 16000; // 기본 샘플링 레이트 (예: 16000Hz)
    private final String encoding = "MP3"; // 기본 인코딩 형식 (예: LINEAR16)

    public String transcribeAudio(byte[] audioBytes) throws IOException {
        // 오디오 설정
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setLanguageCode(languageCode) // 예: "ko-KR", "en-US"
                .setSampleRateHertz(sampleRateHertz) // 예: 16000
                .setAudioChannelCount(1) // 오디오 채널 수 (보통 1 또는 2)
                .setEnableAutomaticPunctuation(true) // 자동 구두점 추가 (선택 사항)
                .setEncoding(RecognitionConfig.AudioEncoding.valueOf(encoding)) // 예: "LINEAR16", "FLAC", "MP3"
                .build();

        // 오디오 데이터
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioBytes))
                .build();

        try (SpeechClient speechClient = SpeechClient.create()) {
            // STT API 호출
            RecognizeResponse response = speechClient.recognize(config, audio);

            // 결과 처리
            StringBuilder transcript = new StringBuilder();
            if (response.getResultsCount() > 0) {
                for (com.google.cloud.speech.v1.SpeechRecognitionResult result : response.getResultsList()) {
                    if (result.getAlternativesCount() > 0) {
                        SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                        transcript.append(alternative.getTranscript()).append(" ");
                    }
                }
            }
            System.out.println("Transcription: " + transcript.toString().trim());
            // 결과가 없으면 빈 문자열 반환
            return transcript.toString().trim();
        }
    }
}