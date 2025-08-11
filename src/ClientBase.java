import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientBase {
    public void startClient(String name) {
        final String SERVER_IP = "192.168.1.213";
        final int SERVER_PORT = 12345;

        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            output.writeUTF(name); // server'a ismini gönder

            // Dinleyici thread
            new Thread(() -> {
                try {
                    while (true) {
                        String message = input.readUTF();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Bağlantı koptu.");
                }
            }).start();

            // Kullanıcıdan mesaj gir
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print(">> Hedef client adı: ");
                String target = scanner.nextLine();
                System.out.print(">> Mesaj: ");
                String message = scanner.nextLine();

                output.writeUTF(target + ":" + message);
            }
        } catch (IOException e) {
            System.out.println("Bağlantı kurulamadı: " + e.getMessage());
        }
    }
}
