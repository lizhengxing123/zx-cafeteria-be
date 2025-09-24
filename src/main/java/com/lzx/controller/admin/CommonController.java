package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.exception.UploadFailedException;
import com.lzx.result.Result;
import com.lzx.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return Result<String> 上传成功后的文件路径
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);

            return Result.success(MessageConstant.UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new UploadFailedException(MessageConstant.UPLOAD_FAILED);
        }
    }
}
