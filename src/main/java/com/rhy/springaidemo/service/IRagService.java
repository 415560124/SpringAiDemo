package com.rhy.springaidemo.service;

import org.springframework.ai.document.Document;
import java.util.List;

public interface IRagService {
    /**
     * rag搜索
     * @param message
     * @return
     */
    List<Document> search(String message);
}
