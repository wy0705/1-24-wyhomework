package socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public Client() {
        BufferedReader br = null;
        PrintWriter pw = null;
        Socket socket = null;
        try {
            socket = new Socket("localhost", 11111);
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(),true);
            pw.println("你好，server");
            String data = "";
            while (true) {
                if ((data = br.readLine()) != null) {
                    System.out.println(data);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                pw.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
