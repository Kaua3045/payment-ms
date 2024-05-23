package com.kaua.payment.infrastructure.configurations.properties.mercadopago;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mercadopago")
public class MercadoPagoProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoProperties.class);

    private String accessToken;
    private int requestTimeout;
    private int connectionTimeout;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("MercadoPagoProperties initialized");
    }

    @Override
    public String toString() {
        return "MercadoPagoProperties(" +
                "accessToken='" + accessToken + '\'' +
                ')';
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
