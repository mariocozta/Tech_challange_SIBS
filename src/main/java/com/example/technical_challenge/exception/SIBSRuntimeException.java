package com.example.technical_challenge.exception;

import com.example.technical_challenge.constant.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SIBSRuntimeException extends RuntimeException{

    private ResponseCode responseCode;

    public SIBSRuntimeException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public SIBSRuntimeException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public SIBSRuntimeException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public SIBSRuntimeException(String message, Throwable cause, ResponseCode responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public SIBSRuntimeException(Throwable cause, ResponseCode responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public SIBSRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                                ResponseCode responseCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseCode = responseCode;
    }
}
