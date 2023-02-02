import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author sofency
 * @date 2023/2/2
 * input 从文件中读出数据 output向文件中写入数据
 */
public class NIOChannel {
    public static void main(String[] args) throws IOException {
        read("src", "dist");
    }

    private static void read() throws IOException {
        File file = new File("channel.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }

    private static void read(String src, String dist) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(src + ".txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(dist + ".txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        while (true) {
            //清空buffer 复位
            byteBuffer.clear();

            int length = inputStreamChannel.read(byteBuffer);
            System.out.println(length);
            if (length == -1) {
                break;
            }

            byteBuffer.flip();

            outputStreamChannel.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

    private static void write() throws IOException {
        String str = "Hello world";

        FileOutputStream outputStream = new FileOutputStream(new File("channel.txt"));

        FileChannel fileChannel = outputStream.getChannel();

        // 申请ByteBuffer 并写数据到buffer中
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put(str.getBytes());

        // 读写切换
        byteBuffer.flip();

        fileChannel.write(byteBuffer);

        outputStream.close();
    }
}
