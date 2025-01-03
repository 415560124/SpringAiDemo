package com.rhy.springaidemo.service.impl;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MarkdownSplitter extends TextSplitter {
    @Override
    protected List<String> splitText(String text) {
        return List.of(splitMarkdown(text));
    }

    private String[] splitMarkdown(String text) {
        return text.split("\\s*\\R\\s*\\R\\s*");
    }
}
