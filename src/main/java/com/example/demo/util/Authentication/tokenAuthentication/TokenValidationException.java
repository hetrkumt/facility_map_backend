package com.example.demo.util.Authentication.tokenAuthentication;

public class TokenValidationException extends Exception {


    private final TokenValidationResult result;

    public TokenValidationException(TokenValidationResult result, String message) {
        super(message);
        this.result = result;
    }

    public TokenValidationResult getResult() {
        return result;
    }
}

