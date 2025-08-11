import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;

    // Client isimleri ve onların handler'ları burada tutulur
    static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server başlatıldı, port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String clientName = dis.readUTF();

                ClientHandler clientHandler = new ClientHandler(socket, clientName);
                clients.put(clientName, clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();

                System.out.println(clientName + " bağlandı.");
            }
        } catch (IOException e) {
            System.err.println("Sunucu hatası: " + e.getMessage());
        }
    }
}
