package com.lzx.service.impl;

import com.lzx.constant.MessageConstant;
import com.lzx.exception.UploadFailedException;
import com.lzx.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${zx.file.upload-path}")
    private String uploadPath;

    /**
     * 保存上传的文件
     * @param file 上传的文件
     * @return 保存后的文件名（UUID）
     */
    public String storeFile(MultipartFile file) throws IOException {
        // 创建存储目录（如果不存在）
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new UploadFailedException(MessageConstant.FILE_EMPTY);
        }

        // 生成唯一文件名，避免冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + fileExtension;

        // 保存文件到本地
        File destFile = new File(uploadPath + fileName);
        file.transferTo(destFile);

        return fileName;
    }

    /**
     * 获取文件的完整路径
     */
    public String getFilePath(String fileName) {
        return uploadPath + fileName;
    }
}
