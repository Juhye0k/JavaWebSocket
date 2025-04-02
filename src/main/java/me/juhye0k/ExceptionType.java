package me.juhye0k;

public enum ExceptionType {
    NEGATIVE_OR_ZERO("401", "0 이하의 정수는 입력할 수 없습니다."),
    NOT_AN_INTEGER("402", "정수가 아닌 실수는 입력할 수 없습니다."),
    EXCEEDS_MAX("403", "5 이상의 정수는 입력할 수 없습니다."),
    INVALID_CHARACTER("404", "문자는 입력할 수 없습니다.");

    private final String errorCode;
    private final String errorMessage;

    private ExceptionType(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
