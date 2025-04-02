package me.juhye0k;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.juhye0k.dto.MessagePacketRequest;
import me.juhye0k.dto.MessagePacketResponse;
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
        System.out.println(socket.getInetAddress() +"로부터 연결 요청이 들어왔습니다. 연결을 수락합니다.");

        // 클라이언트로부터 데이터를 읽기 위한 입력스트림 생성
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        // 클라이언트에게 데이터를 보내기 위한 출력 스트림을 생성
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // 자바 객제를 JSON으로 매핑하기 위한 ObjectMapper 정의
        ObjectMapper mapper = new ObjectMapper();

        try {
            while (true) {
                // 클라이언트로부터 UTF-8 형식의 JSON 문자열을 읽음
                String json = dis.readUTF();
                System.out.println("받은 JSON: " + json);

                MessagePacketRequest packet = mapper.readValue(json, MessagePacketRequest.class);
                if (packet.getEchoOption() == 4) {
                    System.out.println("클라이언트가 종료 요청을 보냈습니다.");
                    break;
                }
                // 클라이언트의 메세지와 에코 옵션을 기반으로 결과 메세지 생성
                String resultMessage = setMessage(packet.getMessage(), packet.getEchoOption());

                // 응답 객체를 생성
                MessagePacketResponse response = new MessagePacketResponse(resultMessage);
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
