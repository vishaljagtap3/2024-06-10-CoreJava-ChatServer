import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer {
    private HashMap<String, Socket> socketHashMap;

    public ChatServer() {
        socketHashMap = new HashMap<String, Socket>();
    }

    public void add(String username, Socket socket) {
        socketHashMap.put(username, socket);
        try {
            new Reader(username, socket.getInputStream()).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class Reader extends Thread{

        private String hostName;
        private InputStream in;
        private boolean flag = true;

        public Reader(String hostName, InputStream in) {
            this.hostName = hostName;
            this.in = in;
        }

        @Override
        public void run() {
            System.out.println("**** " + hostName + " ****");
            byte [] data = new byte[1024 * 1];
            int count;
            while(flag) {
                try {
                    if(in.available() > 0) {
                        count = in.read(data);
                        String message = new String(data, 0, count);
                        if(message.equalsIgnoreCase("bye")){
                            socketHashMap.get(hostName).close();
                            System.out.println(hostName + " left....");
                            flag = false;
                            continue;
                        }
                        System.out.println("Got: " + message);
                        String [] messageParts = message.split(":");

                        socketHashMap.get(messageParts[1])
                                .getOutputStream()
                                .write(message.getBytes());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
