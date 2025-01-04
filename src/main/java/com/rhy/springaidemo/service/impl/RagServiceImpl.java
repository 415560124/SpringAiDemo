package com.rhy.springaidemo.service.impl;

import com.rhy.springaidemo.service.IRagService;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.ai.document.Document;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class RagServiceImpl implements IRagService {

    @Autowired
    private VectorStore vectorStore;
    @Override
    public List<Document> search(String message) {
        //先查元数据
        FilterExpressionBuilder filterExpressionBuilder = new FilterExpressionBuilder();
        SearchRequest searchRequest = SearchRequest.builder()
                .query(message)
                .topK(3)
                .similarityThreshold(0.9)
                .filterExpression(
                        filterExpressionBuilder.eq("question",message).build()
                )
                .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        if(!CollectionUtils.isEmpty(documents)){
            return documents;
        }
        //元数据没有查到，再查相似度搜索
        searchRequest = SearchRequest.builder()
                .query(message)
                .topK(3)
                .similarityThreshold(0.9)
                .build();
        documents = vectorStore.similaritySearch(searchRequest);
        return documents;
    }
}
