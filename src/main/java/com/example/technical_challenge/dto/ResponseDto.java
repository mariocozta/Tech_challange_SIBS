package com.example.technical_challenge.dto;

import com.example.technical_challenge.constant.ResponseCode;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDto<T> {
    private T data;
    private String message;
    private ResponseCode responseCode;

    public static <D> ResponseDto<D> wrapper(D data, String message, ResponseCode responseCode) {
        return ResponseDto.<D>builder().data(data).message(message).responseCode(responseCode).build();
    }
}
