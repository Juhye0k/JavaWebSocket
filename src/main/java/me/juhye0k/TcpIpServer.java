package me.juhye0k;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.juhye0k.dto.ErrorResponse;
import me.juhye0k.dto.MessagePacketRequest;
import me.juhye0k.dto.MessagePacketResponse;
import me.juhye0k.error.ErrorCode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpIpServer {
    public static void main(String[] args) throws IOException {
        // 환영 소켓 생성 --> 서버 측에서 클라이언트의 연결 요청을 수신하기 위해 사용되는 환영 소켓
        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("서버가 준비되었습니다. 연결을 기다립니다...");

        // 클라이언트가 연결을 시도할 때까지 block 상태로 대기 --> 연결이 성립되면 새로운 Socket 객체 반환, 실제 데이터 통신 담당 소켓
        Socket socket = serverSocket.accept();
        System.out.println("The server is ready to receive");

        // 클라이언트로부터 데이터를 읽기 위한 입력스트림 생성
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // 클라이언트에게 데이터를 보내기 위한 출력 스트림을 생성
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // 자바 객체를 JSON으로 매핑하기 위한 ObjectMapper 정의
        ObjectMapper mapper = new ObjectMapper();

        try {
            while (true) {
                // 클라이언트로부터 UTF-8 형식의 JSON 문자열을 읽음
                String json = dis.readUTF();
                System.out.println("받은 JSON: " + json);

                // JSON을 MessagePacketRequest 객체로 변환
                MessagePacketRequest packet = mapper.readValue(json, MessagePacketRequest.class);
                MessagePacketResponse response = null;

                // 에코 옵션이 4일 경우 종료
                if (packet.getEchoOption() == 4) {
                    System.out.println("클라이언트가 종료 요청을 보냈습니다.");
                    break;
                }

                // 에코 옵션 검증 및 처리
                int echoOption = packet.getEchoOption();
                try {
                    // 클라이언트에서 NumberFormatException 발생 시 echoOption이 -1로 설정됨
                    if (echoOption == -1) {
                        // 원본 입력값을 확인하여 실수인지 문자열인지 판단
                        String originalInput = packet.getMessage().equals("error") ? "unknown" : packet.getMessage();
                        try {
                            Double.parseDouble(originalInput); // 실수로 변환 가능한지 체크
                            response = new MessagePacketResponse(new ErrorResponse(ErrorCode.FLOAT_INPUT));
                        } catch (NumberFormatException e) {
                            response = new MessagePacketResponse(new ErrorResponse(ErrorCode.STRING_INPUT));
                        }
                    }
                    // 0 이하의 정수 입력 오류
                    else if (echoOption <= 0) {
                        response = new MessagePacketResponse(new ErrorResponse(ErrorCode.NEGATIVE_OR_ZERO));
                    }
                    // 5 이상의 정수 입력 오류
                    else if (echoOption >= 5) {
                        response = new MessagePacketResponse(new ErrorResponse(ErrorCode.OVER_MAX_OPTION));

                    }
                    // 유효한 옵션(1, 2, 3)일 경우 메시지 처리
                    else if (echoOption >= 1 && echoOption <= 3) {
                        // 정상적인 메시지 처리
                        String resultMessage = setMessage(packet.getMessage(), echoOption);
                        System.out.println("Before: [" + packet.getUser() + "]:" + packet.getMessage());
                        System.out.println("After: [" + packet.getUser() + "]:" + resultMessage);
                        response = new MessagePacketResponse(resultMessage);
                    }
                } catch (Exception e) {
                    // 예외 발생 시 오류 메시지 생성
                    response = new MessagePacketResponse(new ErrorResponse(ErrorCode.SERVER_ERROR));
                }

                // 응답 객체를 JSON으로 변환
                String responseJson = mapper.writeValueAsString(response);
                // 클라이언트에게 JSON 형식의 응답을 전송
                dos.writeUTF(responseJson);
                // 출력 버퍼를 비워 즉시 전송
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 처리
            System.out.println("클라이언트 연결이 종료되었습니다.");
        } finally {
            dis.close();  // 입력 스트림 닫기
            dos.close();  // 출력 스트림 닫기
            socket.close(); // 클라이언트와 소켓 연결 닫기
            serverSocket.close(); // 서버 소켓 닫기
        }
    }

    public static String setMessage(String message, int echoOption) {
        if (echoOption == 1)  // 옵션 1: 일반 에코
            return message;
        else if (echoOption == 2)  // 옵션 2: 대문자 에코
            return message.toUpperCase();
        else  // 옵션 3: 소문자 에코
            return message.toLowerCase();
    }
}