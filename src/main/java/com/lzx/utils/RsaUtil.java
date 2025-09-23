package com.lzx.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密解密工具类，包含公钥私钥解析方法
 */
public class RsaUtil {

    /**
     * 从Base64编码的字符串解析RSA公钥
     * @param publicKeyBase64 Base64编码的公钥字符串
     * @return 解析后的PublicKey对象
     * @throws NoSuchAlgorithmException 不支持RSA算法时抛出
     * @throws InvalidKeySpecException 密钥格式不正确时抛出
     */
    public static PublicKey getPublicKey(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 1. 解码Base64字符串为字节数组
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // 2. 创建X.509格式的密钥规范（公钥标准格式）
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // 3. 获取RSA密钥工厂并生成公钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从Base64编码的字符串解析RSA私钥
     * @param privateKeyBase64 Base64编码的私钥字符串
     * @return 解析后的PrivateKey对象
     * @throws NoSuchAlgorithmException 不支持RSA算法时抛出
     * @throws InvalidKeySpecException 密钥格式不正确时抛出
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 1. 解码Base64字符串为字节数组
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);

        // 2. 创建PKCS#8格式的密钥规范（私钥标准格式）
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        // 3. 获取RSA密钥工厂并生成私钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用私钥解密数据（用于解密前端传来的AES密钥）
     * @param encryptedData 加密后的数据（Base64编码）
     * @param privateKey 私钥对象
     * @return 解密后的明文
     * @throws Exception 解密过程中发生异常时抛出
     */
    public static String decryptByPrivateKey(String encryptedData, PrivateKey privateKey) throws Exception {
        // 1. 解码Base64加密数据
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // 2. 初始化RSA解密器
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 3. 执行解密（处理长数据分段解密）
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // 4. 将字节数组转为字符串返回
        return new String(decryptedBytes);
    }

    /**
     * 生成RSA密钥对并转为Base64（用于初始化配置时生成密钥）
     * @return 包含公钥和私钥Base64的数组 [publicKey, privateKey]
     * @throws NoSuchAlgorithmException 不支持RSA算法时抛出
     */
    public static String[] generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048); // 2048位密钥
        KeyPair keyPair = generator.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        return new String[]{publicKey, privateKey};
    }
}
