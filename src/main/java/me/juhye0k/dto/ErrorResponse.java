package me.juhye0k.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.juhye0k.error.ErrorCode;

public class ErrorResponse {
    @JsonProperty("errorCode") // JSON 직렬화 시 이름 명시적으로 설정
    private String errorCode; // 에러의 식별 코드

    @JsonProperty("errorMessage")
    private String errorMessage; // 상세 메세지

    // 기본 생성자 (Jackson이 필요로 함)
    public ErrorResponse() {
        this.errorCode = null;
        this.errorMessage = null;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

    // Getters and Setters
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}