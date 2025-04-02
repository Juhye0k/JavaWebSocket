package me.juhye0k.dto;

public class MessagePacketResponse {
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
