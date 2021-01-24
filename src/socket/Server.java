package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server() {
        BufferedReader br=null;
        PrintWriter pw=null;
        try {
            ServerSocket serverSocket=new ServerSocket(11111);
            Socket socket=serverSocket.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw=new PrintWriter(socket.getOutputStream(),true);

            pw.println("我是server,我给cilent发信息了");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
    }
}
