package me.juhye0k.dto;

public class MessagePacketRequest { // 클라이언트에서 서버로 보내는 패킷
    private String user;
    private int echoOption;
    private String message;

    public MessagePacketRequest() {

    }
    public MessagePacketRequest(String user, int echoOption, String message) {
        this.user = user;
        this.echoOption = echoOption;
        this.message = message;
    }
    public String getUser(){
        return user;
    }
    public int getEchoOption(){
        return echoOption;
    }
    public String getMessage(){
        return message;
    }
}
