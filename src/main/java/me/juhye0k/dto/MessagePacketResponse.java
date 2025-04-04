package me.juhye0k.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

// null 값은 JSON 직렬화 시 포함되지 않게 함. 즉 반환시 정상 응답이면 message, 에러가 나면 error가 반환
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
