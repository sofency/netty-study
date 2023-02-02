import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author sofency
 * @date 2023/2/2
 */
public class NIOSelectorClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        //提供服务器端的ip和端口
        InetSocketAddress socketAddress = new InetSocketAddress(6666);
        if (!socketChannel.connect(socketAddress)) {
            while (!socketChannel.finishConnect()){
                System.out.println("等待中");
            }
        }

        String str = "Hello World";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据
        socketChannel.write(buffer);

        System.in.read();
    }
}
