package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final ExecutorService handlerPool = Executors.newFixedThreadPool(100);

    public ChatServer() throws IOException {
        this.selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(9999));
        // 将服务端的socket注册到selector中，接收客户端，并将其注册到selector中，其本身也是selector中的一个I/O事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.err.println("聊天室服务端初始化结束");
    }

    /**
     * 启动方法
     * 1.监听，拿到之后进行处理
     */
    public void start() throws IOException {
        int count;
        while (true) {
            // 可能出现select方法没阻塞，空轮询导致死循环的情况
            count = selector.select();

            if (count > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 交给线程池处理
                    handlerPool.execute(new ServerHandler(key, selector));
                    // 处理完成后移除
                    iterator.remove();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().start();
    }
}