package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatClient {

    private Selector selector;

    private SocketChannel socketChannel;

    private static ExecutorService dealPool = Executors.newFixedThreadPool(2);

    public ChatClient() throws IOException {

        /*
         * 说明一下：
         * 客户端这边的selector跟刚才在服务端定义的selector是不同的两个selector
         * 客户端这边不需要selector也能实现功能，但是读取的时候必须不断的循环，会导致CPU飙升，
         * 所以使用selector是为了解决这个问题的，别跟服务端的selector搞混就好
         */
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }


    public void start() throws IOException, InterruptedException {
        // 连接
//        socketChannel.connect(new InetSocketAddress("localhost", 9999));
        while (!socketChannel.finishConnect()) {
            System.err.println("正在连接...");
            TimeUnit.MILLISECONDS.sleep(200);
        }

        System.err.println("连接成功");

        // 使用两个线程来分别处理读取和写操作
        // 写数据
        dealPool.execute(new ClientHandler(selector, socketChannel, 1));

        // 读取数据
        dealPool.execute(new ClientHandler(selector, socketChannel, 0));
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        new ChatClient().start();
    }
}