import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String name;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ClientHandler oluşturulamadı: " + e.getMessage());
        }
    }

    public void sendMessage(String message) throws IOException {
        output.writeUTF(message);
    }

    public void run() {
        try {
            while (true) {
                String received = input.readUTF();
                String[] parts = received.split(":", 2);
                if (parts.length != 2) continue;

                String targetName = parts[0];
                String message = parts[1];

                ClientHandler target = Server.clients.get(targetName);
                if (target != null) {
                    target.sendMessage("[" + name + "]: " + message);
                } else {
                    output.writeUTF("Kullanıcı '" + targetName + "' bulunamadı.");
                }
            }
        } catch (IOException e) {
            System.out.println(name + " bağlantısı koptu.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket kapatılamadı: " + e.getMessage());
            }
            Server.clients.remove(name);
            System.out.println(name + " çıkarıldı.");
        }
    }
}
