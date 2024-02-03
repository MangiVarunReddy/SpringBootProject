package io.endeavour.stocks.config;

import feign.RequestInterceptor;
import io.endeavour.stocks.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This config class is used to hit actual cumulative calculating webService API
 * Hence it needs the LoginService to generate a Bearer Token
 */
@Configuration
public class StocksFeignConfig {
    LoginService loginService;

    @Autowired
    public StocksFeignConfig(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * This generated RequestInterceptor adds an Authorization Bearer header to any of the requests sent by
     * using this config class
     * @return
     */
    @Bean
    public RequestInterceptor getRequestInterceptor(){
        return requestTemplate -> requestTemplate.header("Authorization","Bearer "+loginService.getBearerToken());
    }
}
