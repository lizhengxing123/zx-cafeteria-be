package com.lzx.utils;

import com.lzx.exception.DecryptDataException;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES-GCM解密工具类（对应前端加密逻辑）
 */
public class AesGcmUtil {

    // GCM模式参数
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 12字节IV，与前端保持一致
    private static final int GCM_TAG_LENGTH = 128; // 认证标签长度

    /**
     * AES-GCM解密
     * @param encryptedData 加密后的数据（Base64编码，包含IV）
     * @param aesKey AES密钥（Base64编码）
     * @return 解密后的明文
     */
    public static String decrypt(String encryptedData, String aesKey) throws Exception {
        try {
            // 1. 解码Base64数据
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] keyBytes = Base64.getDecoder().decode(aesKey);

            // 2. 分离IV和密文（前12字节是IV）
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] cipherText = new byte[encryptedBytes.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
            System.arraycopy(encryptedBytes, iv.length, cipherText, 0, cipherText.length);

            // 3. 初始化解密器
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            // 4. 解密
            byte[] decryptedBytes = cipher.doFinal(cipherText);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptDataException("AES-GCM解密失败: " + e.getMessage());
        }
    }
}

