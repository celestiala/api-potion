package com.tmoncorp.mobile.util.spring.http;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Type;

public class ApiMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static Logger LOGGER = (Logger) LoggerFactory.getLogger(ApiMappingJackson2HttpMessageConverter.class);
    ApiMappingJackson2HttpMessageConverter(ObjectMapper mapper){
        super(mapper);
    }

    protected JavaType getJavaType(Type type, Class<?> contextClass) {
        if (contextClass!=null){
            return this.objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, contextClass);
        }else{
            return this.objectMapper.getTypeFactory().constructType(type,contextClass);
        }
    }
}
