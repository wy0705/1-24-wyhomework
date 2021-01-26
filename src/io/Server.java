package io;
//2、改造server，将其变成一个能够持续接收和发送数据的server。
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//通过bio，让server可以持续接受发送数据
//服务器端启动一个ServerSocket
//客户端启动Socket对服务器进行通信，默认情况下服务器端需要对每个客户建立一个线程与之通讯
//客户端发出请求后，先咨询服务器是否有线程响应，如果没有则会等待，或者被拒绝
//如果有响应，客户端线程会等待请求结束后，在继续执行
public class Server {
    public static void main(String[] args) throws IOException {
        ExecutorService newCachedThreadPool= Executors.newCachedThreadPool();
        //创建ServerSocket
        ServerSocket serverSocket=new ServerSocket(11112);
        System.out.println("聊天室启动了");
        File fout = new File("src/aaa.txt");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        while (true){
            //监听，等待客户端连接
            final Socket socket=serverSocket.accept();
            System.out.println("连接到一个客户端");
            //创建一个线程，与之通讯
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {//重写
                    //可以和客户端通讯
                    try {
                        handle(socket,bw);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
    public static void handle(Socket socket,BufferedWriter bw) throws IOException {
        try {
            System.out.println("线程信息 id="+Thread.currentThread().getId()+"名字"+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环的读取客户端发送的数据
            while (true){
                System.out.println("线程信息 id="+Thread.currentThread().getId()+"名字"+Thread.currentThread().getName());
                int read=inputStream.read(bytes);
                if (read!=-1){
                    String s=new String(bytes,0,read);
                    System.out.println(s);
                    bw.write(s);
                    bw.newLine();
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("停止和client的连接");
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
