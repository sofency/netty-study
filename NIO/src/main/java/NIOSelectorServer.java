import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author sofency
 * @date 2023/2/2
 */
public class NIOSelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();

        Selector selector = Selector.open();

        // 绑定端口
        socketChannel.socket().bind(new InetSocketAddress(6666));
        socketChannel.configureBlocking(false); //设置非阻塞

        // socket注册到selector中
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);//关心accept方法

        while (true) {

            if (selector.select(5000) == 0) {
                System.out.println("服务器等待了一秒");
                continue;
            }
            // 如果返回的不是0 说明没有accept事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                // 获取selectionKey
                SelectionKey selectionKey = iterator.next();
                // 根据key对应的通道发生的事件做不同的处理
                if (selectionKey.isAcceptable()) {//如果时OP_ACCEPT 有新的客户端连接
                    // 给该客户端生成SocketChannel
                    SocketChannel channel = socketChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("有客户端连接" + channel.hashCode());
                } else if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment(); //key关联了channel 和buffer
                    int read = channel.read(buffer);
                    // ！！！注意这里
                    if (read == -1) {
                        //说明连接断开
                        channel.close();
                        iterator.remove();
                        continue;
                    }
                    System.out.println("客户端发送:" + new String(buffer.array(), 0, read));
                }
                //手动移除selectionKey
                iterator.remove();
            }
        }
    }
}
