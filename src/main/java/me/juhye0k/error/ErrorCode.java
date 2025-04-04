package me.juhye0k.error;

public enum ErrorCode {
    NEGATIVE_OR_ZERO("401", "0 이하의 정수는 입력할 수 없습니다."),
    FLOAT_INPUT("402", "정수가 아닌 실수는 입력할 수 없습니다."),
    OVER_MAX_OPTION("403", "5 이상의 정수는 입력할 수 없습니다."),
    STRING_INPUT("404", "문자는 입력할 수 없습니다."),
    SERVER_ERROR("500", "서버에서 처리 중 문제가 발생했습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
