package com.rhy.springaidemo.controller;

import com.rhy.springaidemo.service.IDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
@Tag(name = "文档模块")
public class DocumentController {

    @Autowired
    private IDocumentService documentService;
    /**
     * 文档上传
     * @param document
     * @return
     */
    @PostMapping("/upload/embedding")
    @Operation(summary = "上传模型并向量化")
    public ResponseEntity uploadAndEmbedding(@RequestParam("file") MultipartFile multipartFile){
        documentService.uploadAndEmbedding(multipartFile);
        return ResponseEntity.ok().build();
    }
}
