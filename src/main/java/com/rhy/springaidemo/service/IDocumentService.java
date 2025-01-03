package com.rhy.springaidemo.service;

import org.springframework.web.multipart.MultipartFile;

public interface IDocumentService {

    /**
     * 上传文档并向量化
     * @param multipartFile
     */
    void uploadAndEmbedding(MultipartFile multipartFile);
}
