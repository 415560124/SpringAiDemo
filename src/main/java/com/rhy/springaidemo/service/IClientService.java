package com.rhy.springaidemo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;

public interface IClientService {

    /**
     * 对话
     * @param prompt
     * @return
     */
    String call(String clientId,String message);
}
