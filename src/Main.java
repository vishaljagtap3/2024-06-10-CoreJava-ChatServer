import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(
                    2003, 50, InetAddress.getLocalHost()
            );

            ChatServer chatServer = new ChatServer();

            byte [] data = new byte[1024];
            int count;

            while(true) {
                Socket socket = serverSocket.accept();
                count = socket.getInputStream().read(data);
                String username = new String(data, 0, count);
                chatServer.add(username, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
