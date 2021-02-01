package nio;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ClientHandler implements Runnable {

    private Selector selector;

    private SocketChannel socketChannel;

    /**
     * 0-读，1-写
     */
    private int type;

    public ClientHandler() {
    }

    public ClientHandler(Selector selector, SocketChannel socketChannel, int type) {
        // selector是为了解决读时候CPU飙升的问题，具体见客户端的启动类代码注释
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            if (type == 0) {
                doClientReadJob();
                return;
            }

            doClientWriteJob();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写操作
     */
    private void doClientWriteJob() throws IOException {
        SocketChannel sc = socketChannel;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (null != line && !"".equals(line)) {
                    ByteBuffer buffer = ByteBuffer.wrap(line.getBytes(StandardCharsets.UTF_8));
                    sc.write(buffer);
                }
            }
        }
    }

    /**
     * 读操作
     */
    private void doClientReadJob() throws IOException {
        SocketChannel sc = socketChannel;
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (true) {
            int select = selector.select();
            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    // 这是必须的，不然下方的remove会出错
                    SelectionKey next = iterator.next();
                    // 这里因为只有本身这个客户端注册到客户端的selector中，所以有事件一定是它的，也就不用从key拿了，直接操作就行
                    buf.clear();
                    int read = sc.read(buf);
                    if (read > 0) {
                        String msg = new String(buf.array(), StandardCharsets.UTF_8);
                        System.err.println(msg);
                    }
                    // 事件处理完之后要移除这个key，否则的话selector.select()方法不会再读到这个key，即便有新的时间到这个channel来
                    iterator.remove();
                }
            }
        }
    }

}