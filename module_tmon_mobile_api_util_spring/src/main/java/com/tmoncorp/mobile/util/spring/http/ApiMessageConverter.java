package com.tmoncorp.mobile.util.spring.http;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.List;

public class ApiMessageConverter implements HttpMessageConverter<Object> {

    private HttpMessageConverter<Object> converter;

    public ApiMessageConverter(HttpMessageConverter<Object> converter){
        this.converter=converter;
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

    @Override public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        ApiResponse apiResponse=(ApiResponse)converter.read(ApiResponse.class,inputMessage);
        return apiResponse.getData();
    }

    @Override public void write(Object t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        converter.write(t,contentType,outputMessage);
    }
}
