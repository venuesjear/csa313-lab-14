import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientNotifyApp {
    public static void main(String[] args) throws Exception {
        int port = 9999;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Waiting...");

        while (true) {
            Socket client = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String message = reader.readLine();
            System.out.println("Notification: " + message);
            client.close();
        }
    }
}
