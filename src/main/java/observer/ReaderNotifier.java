package observer;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReaderNotifier implements BookObserver {
    private final String readerId;
    private final String host;
    private final int port;

    public ReaderNotifier(String readerId, String host, int port) {
        this.readerId = readerId;
        this.host = host;
        this.port = port;
    }

    @Override
    public void notifyAvailable(String isbn) {
        try (Socket socket = new Socket(host, port)) {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("Book [" + isbn + "] is ready to borrow. Reader ID: " + readerId);
        } catch (Exception e) {
            System.err.println("Notification failed for reader " + readerId + ": " + e.getMessage());
        }
    }
}