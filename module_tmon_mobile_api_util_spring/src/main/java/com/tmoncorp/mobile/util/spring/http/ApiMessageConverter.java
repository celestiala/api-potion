package com.tmoncorp.mobile.util.spring.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ApiMessageConverter<T> implements GenericHttpMessageConverter<T> {

    private GenericHttpMessageConverter<Object> converter;

    private static Logger LOGGER = LoggerFactory.getLogger(ApiMessageConverter.class);


    public ApiMessageConverter(HttpMessageConverter<Object> converter){
        this.converter=(GenericHttpMessageConverter)converter;
    }

    @Override public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return converter.canRead(clazz,mediaType);
    }

    @Override public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return converter.canWrite(clazz,mediaType);
    }

    @Override public List<MediaType> getSupportedMediaTypes() {
        return converter.getSupportedMediaTypes();
    }

    @Override public T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        ApiResponse<T> apiResponse= (ApiResponse<T>) converter.read(new ApiResponse<T>().getClass(),inputMessage);
        return apiResponse.getData();
    }

    @Override public void write(T t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        converter.write(t,contentType,outputMessage);
    }

    @Override public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return converter.canRead(ApiResponse.class,contextClass,mediaType);
    }

    @Override public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        Class clazz;
        if (type instanceof Class){
            clazz=(Class) type;
        }else{
            try {
                clazz=Class.forName(type.getTypeName());
            } catch (ClassNotFoundException e) {
                clazz=null;
                LOGGER.warn("{}",e);
            }
        }
        ApiResponse<T> apiResponse= (ApiResponse<T>) converter.read(ApiResponse.class,clazz,inputMessage);
        return apiResponse.getData();
    }
}
