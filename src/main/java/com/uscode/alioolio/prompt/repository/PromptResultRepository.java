package com.uscode.alioolio.prompt.repository;

import com.uscode.alioolio.prompt.entity.PromptResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptResultRepository extends JpaRepository<PromptResult, Integer> {

    PromptResult findByUserId(String userId);
}