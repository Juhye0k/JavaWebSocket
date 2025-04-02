package me.juhye0k.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessagePacketResponse {

    private String message; // 정상 메시지일 때
    private ErrorResponse error; // 에러가 있을 때

    // 기본 생성자
    public MessagePacketResponse() {}

    // 성공 응답 생성자
    public MessagePacketResponse(String message) {
        this.message = message;
    }

    // 에러 응답 생성자
    public MessagePacketResponse(ErrorResponse error) {
        this.error = error;
    }

    // 메시지 getter
    public String getMessage() {
        return message;
    }

    // 에러 getter
    public ErrorResponse getError() {
        return error;
    }

    // 에러 여부 판단
    public boolean isError() {
        return error != null;
    }
}
