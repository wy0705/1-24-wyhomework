package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class ServerHandler implements Runnable {

    private SelectionKey key;

    private Selector selector;

    public ServerHandler() {

    }

    /**
     * 本来可以通过key拿到selector，这里为了图方便就这样写了
     */
    public ServerHandler(SelectionKey key, Selector selector) {
        this.key = key;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            if (key.isAcceptable()) {
                // 说明是服务端的事件，注意这里强转换为的是ServerSocketChannel
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                // 接收连接
                SocketChannel socket = channel.accept();
                if (Objects.isNull(socket)) {
                    return;
                }

                socket.configureBlocking(false);
                // 接收客户端的socket并且将其注册到服务端这边的selector中，注意客户端在此时跟服务端selector产生关联
                socket.register(selector, SelectionKey.OP_READ);
                System.err.println("服务端已接收连接");
            } else if (key.isReadable()) {
                // 客户端发送信息过来了
                doReadJob();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 错误处理
        }
    }

    /**
     * 读取操作
     */
    private void doReadJob() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readCount = socketChannel.read(buffer);
        if (readCount > 0) {
            String msg = new String(buffer.array(), StandardCharsets.UTF_8);
            System.err.println(socketChannel.getRemoteAddress().toString() + "的信息为:" + msg);

            // 转发给其他客户端
            sendMsgToOtherClients(msg);
        }
    }

    /**
     * 转发消息给其他客户端
     *
     * @param msg 消息
     */
    private void sendMsgToOtherClients(String msg) throws IOException {

        SocketChannel self = (SocketChannel) key.channel();

        Set<SelectionKey> keys = selector.keys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            SelectableChannel channel = selectionKey.channel();
            // 如果是本身或者不是socketChannel类型则跳过
            if (self.equals(channel) || channel instanceof ServerSocketChannel) {
                continue;
            }

            SocketChannel socketChannel = (SocketChannel) channel;
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
            socketChannel.write(byteBuffer);
        }
    }
}