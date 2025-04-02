package me.juhye0k;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.juhye0k.dto.MessagePacketRequest;
import me.juhye0k.dto.MessagePacketResponse;

import java.io.*;
import java.net.Socket;

public class TcpIpClient {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ObjectMapper objectMapper = new ObjectMapper();

            // 이름 입력
            System.out.print("사용자 이름 입력:");
            String name = br.readLine();

            // 서버 포트 설정
            String serverIp = "127.0.0.1";

            // 소켓을 생성하여 서버에 연결을 요청한다.
            Socket socket = new Socket(serverIp, 7777);

            // 서버로부터 데이터를 읽기 위한 스트림 생성
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            // 서버로부터 데이터를 보내기 위한 스트림 생성
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            while (true) {
                // 에코 입력
                System.out.println("에코 옵션 (1:일반 에코, 2: 대문자 에코, 3:소문자로 에코, 4:채팅 종료)");
                System.out.print("입력:");
                String input = br.readLine();
                int n;
                try {
                    // 입력값을 정수로 변환
                    n = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    // 정수로 변환 실패 시 -1로 설정 (서버에서 처리)
                    n = -1;
                }

                String message = ""; // 사용자가 입력할 메시지
                String sendPacket; // 서버로 보낼 JSON 패킷
                switch (n) {
                    case 1:
                        // 일반 에코 기능
                        System.out.print("메세지 입력:");
                        message = br.readLine();
                        break;
                    case 2:
                        // 대문자로 에코 기능
                        System.out.print("메세지 입력:");
                        message = br.readLine();
                        break;
                    case 3:
                        // 소문자로 에코 기능
                        System.out.print("메세지 입력:");
                        message = br.readLine();
                        break;
                    case 4:
                        // 채팅 종료
                        sendPacket = objectMapper.writeValueAsString(new MessagePacketRequest(name, n, ""));
                        dos.writeUTF(sendPacket);
                        dos.flush();
                        dis.close();
                        socket.close();
                        System.out.println("연결이 종료되었습니다.");
                        return;
                    default:
                        message = input; // 잘못된 입력은 서버에서 처리
                        break;
                }

                // MessagePacketRequest 객체를 JSON 문자열로 변환
                sendPacket = objectMapper.writeValueAsString(new MessagePacketRequest(name, n, message));
                // 서버로 전송
                dos.writeUTF(sendPacket);
                // 버퍼 비워 즉시 전송
                dos.flush();

                // JSON 문자열 수신
                String receivedJson = dis.readUTF();

                // JSON 문자열 → 객체 변환
                MessagePacketResponse receivedPacket = objectMapper.readValue(receivedJson, MessagePacketResponse.class);

                // 응답 출력
                if (receivedPacket.isError()) {
                    // 에러가 있는 경우 에러 코드와 메시지 출력
                    System.out.println("오류 발생 - 코드: " + receivedPacket.getError().getErrorCode() + ", 메시지: " + receivedPacket.getError().getErrorMessage());
                } else {
                    // 정상적인 경우 메시지 출력
                    System.out.println("After: [" + name + "]:" + receivedPacket.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}