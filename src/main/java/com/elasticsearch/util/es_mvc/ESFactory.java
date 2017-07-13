package com.elasticsearch.util.es_mvc;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESFactory {

    @Value("${es.ip}")
    private String ES_IP;

    @Value("${es.port}")
    private int ES_PORT;

    /**
     * 注入的ElasticSearch实例
     */
    @Bean(name = "client")
    public Client getESClient() {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(ES_IP, ES_PORT));
        return client;
    }
}
