package com.rhy.springaidemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.pgvector.PGvector;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ollama")
@Tag(name = "Ollama模块")
public class OllamaController {
    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Autowired
    private OllamaEmbeddingModel ollamaEmbeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/test")
    @Operation(summary = "语言模型测试")
    public ResponseEntity<String> call(@RequestParam("message") String message){
        return ResponseEntity.ok(ollamaChatModel.call(message));
    }

    @GetMapping("/embedding")
    @Operation(summary = "向量模型测试")
    public ResponseEntity<String> embedding(@RequestParam("message") String message){
        float[] embed = ollamaEmbeddingModel.embed(message);
        return ResponseEntity.ok(JSONObject.toJSONString(embed));
    }
    @PostMapping("/embedding/store")
    @Operation(summary = "向量存储测试")
    public ResponseEntity<String> embeddingStore(@RequestParam("message") String message){
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

        // Add the documents to PGVector
        vectorStore.add(documents);

        // Retrieve documents similar to a query
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
        return ResponseEntity.ok(JSONObject.toJSONString(results));
    }


}
