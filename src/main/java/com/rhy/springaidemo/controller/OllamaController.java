package com.rhy.springaidemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.pgvector.PGvector;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ollama")
public class OllamaController {
    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Autowired
    private OllamaEmbeddingModel ollamaEmbeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/test")
    public String call(@RequestParam("message") String message){
        return ollamaChatModel.call(message);
    }

    @GetMapping("/embedding")
    public String embedding(@RequestParam("message") String message){
        float[] embed = ollamaEmbeddingModel.embed(message);
        return JSONObject.toJSONString(embed);
    }
    @PostMapping("/embedding/store")
    public String embeddingStore(@RequestParam("message") String message){
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

        // Add the documents to PGVector
        vectorStore.add(documents);

        // Retrieve documents similar to a query
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
        return JSONObject.toJSONString(results);
    }


}
