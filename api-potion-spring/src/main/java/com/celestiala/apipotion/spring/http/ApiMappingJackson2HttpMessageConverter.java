package com.celestiala.apipotion.spring.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Type;

public class ApiMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    ApiMappingJackson2HttpMessageConverter(ObjectMapper mapper){
        super(mapper);
    }

    @Override
    protected JavaType getJavaType(Type type, Class<?> contextClass) {
        if (contextClass!=null){
            return this.objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, contextClass);
        }else{
            return this.objectMapper.getTypeFactory().constructType(type,contextClass);
        }
    }
}
