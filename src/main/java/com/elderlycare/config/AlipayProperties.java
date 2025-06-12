package com.elderlycare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "alipay")   // 读取 alipay.*
public class AlipayProperties {

    private String gateway;
    private String appId;

    /** classpath:keys/app_private_key.pem */
    private Resource merchantPrivateKeyPath;

    /** classpath:keys/alipay_public_key.pem */
    private Resource alipayPublicKeyPath;

    private String returnUrl;
    private String notifyUrl;

    /* --- getters / setters --- */
    public String getGateway() { return gateway; }
    public void setGateway(String gateway) { this.gateway = gateway; }

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }

    public Resource getMerchantPrivateKeyPath() { return merchantPrivateKeyPath; }
    public void setMerchantPrivateKeyPath(Resource merchantPrivateKeyPath) {
        this.merchantPrivateKeyPath = merchantPrivateKeyPath;
    }

    public Resource getAlipayPublicKeyPath() { return alipayPublicKeyPath; }
    public void setAlipayPublicKeyPath(Resource alipayPublicKeyPath) {
        this.alipayPublicKeyPath = alipayPublicKeyPath;
    }

    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }

    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String notifyUrl) { this.notifyUrl = notifyUrl; }
}
