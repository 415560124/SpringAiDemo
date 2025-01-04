package com.rhy.springaidemo.entity.common;

import groovy.transform.builder.Builder;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
public class CustomOllamaEmbeddingModel extends OllamaEmbeddingModel {
    public CustomOllamaEmbeddingModel(OllamaApi ollamaApi, OllamaOptions defaultOptions, ObservationRegistry observationRegistry, ModelManagementOptions modelManagementOptions) {
        super(ollamaApi, defaultOptions, observationRegistry, modelManagementOptions);
    }

    @Override
    public float[] embed(Document document) {
        String question = (String) document.getMetadata().get("question");
        return this.embed(question);
    }

    @Override
    public List<float[]> embed(List<Document> documents, EmbeddingOptions options, BatchingStrategy batchingStrategy) {
        Assert.notNull(documents, "Documents must not be null");
        List<float[]> embeddings = new ArrayList<>(documents.size());
        List<List<Document>> batch = batchingStrategy.batch(documents);
        for (List<Document> subBatch : batch) {
            List<String> texts = subBatch.stream().map(document-> (String) document.getMetadata().get("question")).toList();
            EmbeddingRequest request = new EmbeddingRequest(texts, options);
            EmbeddingResponse response = this.call(request);
            for (int i = 0; i < subBatch.size(); i++) {
                embeddings.add(response.getResults().get(i).getOutput());
            }
        }
        Assert.isTrue(embeddings.size() == documents.size(),
                "Embeddings must have the same number as that of the documents");
        return embeddings;
    }
}
