package com.uscode.alioolio.prompt.service;

import com.uscode.alioolio.prompt.response.PromptCategoryRes;
import com.uscode.alioolio.prompt.response.PromptRes;

public interface PromptService {

    PromptCategoryRes getPromptCategory(String code);

    PromptRes getPrompt(String code);

    int getMaxToken(String code);

}
