package com.uscode.alioolio.chat.repository;

import com.uscode.alioolio.chat.entity.AudioChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioChatRepository extends JpaRepository<AudioChat, Integer> {

    List<AudioChat> findTop10ByUserIdOrderByRegDtDesc(String userId);
}