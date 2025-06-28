package com.uscode.alioolio.prompt.repository;

import com.uscode.alioolio.prompt.entity.PromptCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PromptCategoryRepository extends JpaRepository<PromptCategory, Long> {

    @NonNull
    Optional<PromptCategory> findByCodeIgnoreCase(String code);
}