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

        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("서버가 준비되었습니다. 연결을 기다립니다...");

        //
        Socket socket = serverSocket.accept();
        System.out.println(socket.getInetAddress() +"로부터 연결 요청이 들어왔습니다. 연결을 수락합니다.");

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ObjectMapper mapper = new ObjectMapper();

        try {
            while (true) {
                String json = dis.readUTF();
                System.out.println("받은 JSON: " + json);

                MessagePacketRequest packet = mapper.readValue(json, MessagePacketRequest.class);
                if (packet.getEchoOption() == 4) {
                    System.out.println("클라이언트가 종료 요청을 보냈습니다.");
                    break;
                }
                String resultMessage = setMessage(packet.getMessage(), packet.getEchoOption());

                MessagePacketResponse response = new MessagePacketResponse(resultMessage);
                String responseJson = mapper.writeValueAsString(response);

                dos.writeUTF(responseJson);
                dos.flush();
            }
           } catch (IOException e) {
            e.printStackTrace();
            System.out.println("클라이언트 연결이 종료되었습니다.");
        } finally {
            dis.close();
            dos.close();
            socket.close();
            serverSocket.close();
        }
    }

    public static String setMessage(String message, int echoOption) {
        if (echoOption == 1)
            return message;
        else if (echoOption == 2)
            return message.toUpperCase();
        else
            return message.toLowerCase();
    }
}
