package com.rhy.springaidemo.config;

import com.rhy.springaidemo.entity.common.CustomOllamaEmbeddingModel;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.autoconfigure.ollama.OllamaEmbeddingProperties;
import org.springframework.ai.autoconfigure.ollama.OllamaInitializationProperties;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationConvention;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class OllamaConfig {

    @Bean
    public OllamaEmbeddingModel ollamaEmbeddingModel(OllamaApi ollamaApi, OllamaEmbeddingProperties properties, OllamaInitializationProperties initProperties, ObjectProvider<ObservationRegistry> observationRegistry, ObjectProvider<EmbeddingModelObservationConvention> observationConvention) {
        PullModelStrategy embeddingModelPullStrategy = initProperties.getEmbedding().isInclude() ? initProperties.getPullModelStrategy() : PullModelStrategy.NEVER;
        CustomOllamaEmbeddingModel embeddingModel = new CustomOllamaEmbeddingModel(
            ollamaApi,
            properties.getOptions(),
            (ObservationRegistry) observationRegistry.getIfUnique(() -> {
                return ObservationRegistry.NOOP;
            }),
            new ModelManagementOptions(embeddingModelPullStrategy, initProperties.getEmbedding().getAdditionalModels(), initProperties.getTimeout(), initProperties.getMaxRetries())
        );
        Objects.requireNonNull(embeddingModel);
        observationConvention.ifAvailable(embeddingModel::setObservationConvention);
        return embeddingModel;
    }

}
