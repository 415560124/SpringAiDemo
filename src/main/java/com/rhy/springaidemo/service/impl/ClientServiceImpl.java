package com.rhy.springaidemo.service.impl;

import com.rhy.springaidemo.service.IClientService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements IClientService {

    @Autowired
    private ChatClient chatClient;

    @Override
    public String call(String clientId, String message) {

        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt();
        prompt.user(message);
        prompt.advisors(advisor -> advisor.param("chat_memory_conversation_id", clientId).param("chat_memory_response_size", 100));
        return prompt.call().content();
    }
}
