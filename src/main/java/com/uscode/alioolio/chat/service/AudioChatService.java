package com.uscode.alioolio.chat.service;

import com.uscode.alioolio.chat.entity.AudioChat;
import com.uscode.alioolio.chat.repository.AudioChatRepository;
import com.uscode.alioolio.chat.req.AudioChatReq;
import com.uscode.alioolio.chat.res.AudioChatRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioChatService {
    private final AudioChatRepository audioChatRepository;

    public void saveAudioChat(AudioChatReq audioChatReq) {
        AudioChat audioChat =  AudioChat.builder()
                .userId(audioChatReq.getUserId())
                .reqData(audioChatReq.getReqData())
                .resData(audioChatReq.getResData())
                .regDt(Instant.now())
                .build();
        audioChatRepository.save(audioChat);
    }

    public List<AudioChatRes> getAudioChatList(String userId) {
        List<AudioChat> audioList = audioChatRepository.findTop10ByUserIdOrderByRegDtDesc(userId);
        return audioList.stream()
                .map(audioChat -> AudioChatRes.builder()
                        .id(audioChat.getId())
                        .userId(audioChat.getUserId())
                        .reqData(audioChat.getReqData())
                        .resData(audioChat.getResData())
                        .regDt(audioChat.getRegDt())
                        .build())
                .toList();
    }

}
