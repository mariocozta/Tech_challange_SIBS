package com.example.technical_challenge.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS,
    INTERNAL_ERROR,
    PROVIDED_ID_INCORRECT,
    DELETION_FAILED;
}
