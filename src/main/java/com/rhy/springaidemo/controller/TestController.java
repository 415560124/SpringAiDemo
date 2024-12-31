package com.rhy.springaidemo.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ollama")
public class TestController {
    @Autowired
    private OllamaChatModel ollamaChatModel;
    @RequestMapping("/call")
    public String call(@RequestParam("message") String message){
        return ollamaChatModel.call(message);
    }
}
