package com.example.technical_challenge.controller.adviser;

import com.example.technical_challenge.constant.ResponseCode;
import com.example.technical_challenge.dto.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ResponseWrapperAdviser implements ResponseBodyAdvice<Object> {

    private static final List<String> exclusions =  Arrays.asList("openapiJson", "text/plain", "application/openmetrics-text");

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getDeclaringClass() != ExceptionHandlerAdviser.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (exclusions.contains(returnType.getExecutable().getName()) ||
                exclusions.stream().anyMatch(selectedContentType.toString()::contains)) {
            return body;
        }
        if (((ServletServerHttpResponse) response).getServletResponse().getStatus() == 500) {
            return ResponseDto.wrapper(body, null, ResponseCode.INTERNAL_ERROR);
        }
        return ResponseDto.wrapper(body, null, ResponseCode.SUCCESS);
    }
}
