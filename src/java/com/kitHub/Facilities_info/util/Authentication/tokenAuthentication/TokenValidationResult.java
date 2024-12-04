package com.kitHub.Facilities_info.util.Authentication.tokenAuthentication;

public enum TokenValidationResult {
    VALID,
    EXPIRED,
    SIGNATURE_INVALID,
    MALFORMED,
    UNSUPPORTED,
    INVALID,
    MISSING
}