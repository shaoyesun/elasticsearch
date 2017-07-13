package com.elasticsearch.util.es_main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by root on 17-5-25.
 */
public class ESUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static String toJson(Object o){
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
