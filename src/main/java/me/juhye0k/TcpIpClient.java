package me.juhye0k;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.juhye0k.dto.BusinessException;
import me.juhye0k.dto.MessagePacketRequest;
import me.juhye0k.dto.MessagePacketResponse;

import java.io.*;
import java.net.Socket;

public class TcpIpClient {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ObjectMapper objectMapper = new ObjectMapper();

            // 서버 포트 설정
            String serverIp = "127.0.0.1";
            System.out.println("서버에 연결중입니다. 서버 IP : " + serverIp);

            // 소켓을 생성하여 서버에 연결을 요청한다.
            Socket socket = new Socket(serverIp, 7777);

            // 서버로부터 데이터를 읽기 위한 스트림 생성
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            // 서버로부터 데이터를 보내기 위한 스트림 생성
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // 이름 입력
            System.out.println("이름을 입력해주세요.");
            String name = br.readLine();

            while(true){
                // 에코 입력
                System.out.println("에코 옵션 (1:일반 에코, 2: 대문자 에코, 3:소문자로 에코, 4:채팅 종료)");
                String input = br.readLine();
                int n;
                try {
                    // 입력값이 정수인지 확인
                    n = Integer.parseInt(input);
                    // 0 이하의 정수 입력 오류
                    if (n <= 0) {
                        throw new BusinessException(ExceptionType.NEGATIVE_OR_ZERO);
                    }
                    // 5 이상의 정수 입력 오류
                    if (n >= 5) {
                        throw new BusinessException(ExceptionType.EXCEEDS_MAX);
                    }
                } catch (NumberFormatException e) {
                    // 입력값이 숫자가 아닌 경우 (문자열) 또는 실수인지 확인
                    try {
                        Double.parseDouble(input); // 실수로 변환 가능한지 체크
                        throw new BusinessException(ExceptionType.NOT_AN_INTEGER);
                    } catch (NumberFormatException e2) {
                        // 실수도 아닌 경우 (문자열)
                        throw new BusinessException(ExceptionType.INVALID_CHARACTER);
                    }
                }

                String message=""; // 사용자가 입력할 메시지
                String sendPacket; // 서버로 보낼 JSON 패킷
                switch(n){ // 입력된 에코 옵션에 따라 처리
                    case 1:
                        // 일반 에코 기능
                        System.out.println("메세지 입력:");
                        // 메시지 입력받기
                        message = br.readLine();
                        // MessagePacketRequest 객체를 JSON 문자열로 변환
                        sendPacket = objectMapper.writeValueAsString(new MessagePacketRequest(name,n,message));
                        // 서버로 전송
                        dos.writeUTF(sendPacket);
                        // 버퍼 비워 즉시 전송
                        dos.flush();
                        break;
                    case 2:
                        // 대문자로 에코 기능
                        System.out.println("메세지 입력:");
                        message = br.readLine();
                        sendPacket = objectMapper.writeValueAsString(new MessagePacketRequest(name,n,message));
                        dos.writeUTF(sendPacket);
                        dos.flush();
                        break;
                    case 3:
                        // 소문자로 에코 기능
                        System.out.println("메세지 입력:");
                        message = br.readLine();
                        sendPacket = objectMapper.writeValueAsString(new MessagePacketRequest(name,n,message));
                        dos.writeUTF(sendPacket);
                        dos.flush();
                        break;
                    case 4:
                        // 채팅 종료
                        dis.close();
                        socket.close();
                        System.out.println("연결이 종료되었습니다.");
                        return;
                    default:
                        break;
                }
                // JSON 문자열 수신
                String receivedJson = dis.readUTF();

                // JSON 문자열 → 객체 변환
                MessagePacketResponse receivedPacket = objectMapper.readValue(receivedJson, MessagePacketResponse.class);

                // 출력
                System.out.println("Before: " + "["+name+"]"+message);
                System.out.println("After: " + "["+name+"]"+receivedPacket.getMessage());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}