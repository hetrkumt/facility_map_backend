package com.example.demo.util.Authentication.tokenAuthentication;

public enum TokenValidationResult {
    VALID,
    EXPIRED,
    SIGNATURE_INVALID,
    MALFORMED,
    UNSUPPORTED,
    INVALID,
    MISSING
}