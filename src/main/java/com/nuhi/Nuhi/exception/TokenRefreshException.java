package com.nuhi.Nuhi.exception;

public class TokenRefreshException extends RuntimeException {


    public TokenRefreshException(String message, String refreshTokenIsInvalid) {
        super(message);
    }
}
