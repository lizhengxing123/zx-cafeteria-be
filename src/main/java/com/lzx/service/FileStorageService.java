package com.lzx.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    /**
     * 保存上传的文件
     * @param file 上传的文件
     * @return 保存后的文件名（UUID）
     */
    String storeFile(MultipartFile file) throws IOException;

    /**
     * 获取文件的完整路径
     */
    String getFilePath(String fileName);
}
