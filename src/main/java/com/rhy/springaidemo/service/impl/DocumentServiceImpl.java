package com.rhy.springaidemo.service.impl;

import cn.hutool.core.io.FileUtil;
import com.rhy.springaidemo.constant.Constants;
import com.rhy.springaidemo.service.IDocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Service
public class DocumentServiceImpl implements IDocumentService {

    @Autowired
    private MarkdownSplitter markdownSplitter;

    @Autowired
    private VectorStore vectorStore;

    @Override
    public void uploadAndEmbedding(MultipartFile multipartFile) {
        File tempFile = FileUtil.createTempFile();
        try {
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Resource resource = new FileSystemResource(tempFile);
        //读取文件
        TextReader textReader = new TextReader(resource);
        Map<String, Object> customMetadata = textReader.getCustomMetadata();
        //写入原数据文件名
        customMetadata.put("fileName", multipartFile.getOriginalFilename());
        List<Document> documents = textReader.get();
        //分割文档
        List<Document> documentsSplit = markdownSplitter.apply(documents);
        documentsSplit.forEach(document -> {
            Map<String, Object> metadata = document.getMetadata();
            //问题写入元数据
            metadata.put("question", document.getContent().split("\n")[0]);
        });
        vectorStore.add(documentsSplit);
        tempFile.delete();
    }
}
