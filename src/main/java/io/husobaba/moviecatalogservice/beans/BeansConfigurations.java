package io.husobaba.moviecatalogservice.beans;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeansConfigurations {

    @Bean(name = "restTemplate")
    @Scope(scopeName = "prototype")
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(3000);
        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean(name = "webClientBuilder")
    @Scope(scopeName = "prototype")
    public WebClient.Builder getWebClientBuilder(){
        return WebClient.builder();
    }
}
