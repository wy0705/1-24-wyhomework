package io;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wei.jiang
 * @since 2018/10/31
 */
public class Client {
    public static void main(String args[]) throws Exception {
        ExecutorService newCacheThreadPool= Executors.newCachedThreadPool();
        // 与服务端建立连接
        Socket socket = new Socket("localhost",11112);
        String message=null;
        Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.println("输入向server发送的信息");
            message=scanner.next();
            System.out.println(message);
            if (message=="111") {
                System.out.println("break");
                break;
            }
            System.out.println("客户端发送数据");
            String finalMessage = message;
            newCacheThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handle(socket, finalMessage);
                }
            });
        }
        System.out.println("通话结束");
        socket.shutdownOutput();
        socket.close();

    }
    public static void handle(Socket socket,String message){
        try {
            // 建立连接后获得输出流
            OutputStream outputStream = socket.getOutputStream();
            socket.getOutputStream().write(message.getBytes("UTF-8"));
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            //socket.shutdownOutput();

            /*InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, "UTF-8"));
            }
            System.out.println("get message from server: " + sb);
*/
        }catch (Exception e){
            e.printStackTrace();
        }/*finally {
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
    }
}
