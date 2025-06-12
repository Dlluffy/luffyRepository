package com.elderlycare.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 自动装配 AlipayClient
 */
@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayAutoConfig {

    @Bean
    public AlipayClient alipayClient(AlipayProperties p) throws Exception {
        // 读取 PEM 文件内容
        String priKey = Files.readString(Path.of(p.getMerchantPrivateKeyPath().getURI()));
        String aliKey = Files.readString(Path.of(p.getAlipayPublicKeyPath().getURI()));

        return new DefaultAlipayClient(
                p.getGateway(),          // 网关
                p.getAppId(),            // APPID
                priKey,                  // 应用私钥
                "json",                  // 请求格式
                StandardCharsets.UTF_8.name(),
                aliKey,                  // 支付宝公钥
                "RSA2"                   // 签名算法
        );
    }
}
