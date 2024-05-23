package com.example.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."), // 401 error
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."), // 401 error
    INVALID_AUTH(HttpStatus.UNAUTHORIZED, "검증되지 않은 사용자 입니다."), // 401 error
    FORBIDDEN_AUTH(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다."); // 403 error

    private final HttpStatus httpStatus;
    private final String message;

}
