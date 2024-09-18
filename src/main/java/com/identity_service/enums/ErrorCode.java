package com.identity_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTS(1001, "User Exists!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1001, "User not found!", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1002, "Username must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1002, "Password must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1002, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1003, "You do not have permission!", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1003, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    ;
    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
