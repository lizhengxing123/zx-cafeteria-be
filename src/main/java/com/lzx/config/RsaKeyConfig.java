package com.lzx.config;

import com.lzx.utils.RsaUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * RSA密钥对配置（生产环境建议从安全存储加载，而非动态生成）
 */
@Configuration
public class RsaKeyConfig {

    // 可从配置文件指定密钥，若未指定则动态生成
    @Value("${zx.rsa.public-key}")
    private String publicKey;

    @Value("${zx.rsa.private-key}")
    private String privateKey;

    @Bean
    public KeyPair rsaKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 如果配置了密钥则使用配置的，否则动态生成
        if (!publicKey.isEmpty() && !privateKey.isEmpty()) {
            return new KeyPair(
                    RsaUtil.getPublicKey(publicKey), // 需实现公钥解析方法
                    RsaUtil.getPrivateKey(privateKey) // 需实现私钥解析方法
            );
        }

        // 动态生成2048位RSA密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // 暴露公钥给前端（通过Controller）
    @Bean
    public String publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (!publicKey.isEmpty()) {
            return publicKey;
        }
        // 动态生成的密钥转为Base64
        return Base64.getEncoder().encodeToString(rsaKeyPair().getPublic().getEncoded());
    }
}
