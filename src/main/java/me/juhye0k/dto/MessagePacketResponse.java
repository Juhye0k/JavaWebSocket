package me.juhye0k.dto;

public class MessagePacketResponse { // 서버에서 클라이언트로 보내는 응답 패킷
    String message;

    public MessagePacketResponse(){

    }
    public MessagePacketResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
