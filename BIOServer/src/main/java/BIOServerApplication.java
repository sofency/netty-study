import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sofency
 * @date 2023/2/1
 */
public class BIOServerApplication {
    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 30, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(20),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true) {
            final Socket socket = serverSocket.accept();
            System.out.println("客户端连接端口:" + socket.getLocalPort());
            executor.execute(new Runnable() {
                public void run() {
                    System.out.println("线程ID"+ Thread.currentThread().getId());
                    byte[] bytes = new byte[1024];
                    InputStream inputStream;
                    try {
                        inputStream = socket.getInputStream();
                        while (true) {
                            int read = inputStream.read(bytes);
                            if (read == -1) {
                                break;
                            }
                            System.out.printf(new String(bytes, 0, read));
                        }
                        System.out.println();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("关闭客户端的连接");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
