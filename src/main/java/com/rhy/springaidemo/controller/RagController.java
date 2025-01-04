package com.rhy.springaidemo.controller;

import com.rhy.springaidemo.service.IRagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rag")
@Tag(name = "RAG模块")
public class RagController {

    @Autowired
    private IRagService ragService;

    @GetMapping("/search")
    @Operation(summary = "搜索")
    public List<Document> search(@RequestParam("message") String message){
        return ragService.search(message);
    }
}
