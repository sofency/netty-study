import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author sofency
 * @date 2023/2/2
 */
public class MapperByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        /**
         * 在堆外内存中修改
         * 参数1： 读写模式
         * 参数2：可以直接修改的起始位置
         * 参数3：是映射到内存的大小，即 将1.txt多少字节映射到内存 可以直接修改的范围是0-5
         * 当前文件夹下看 还没更新，另开文件夹看
         */
        MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        map.put(4, (byte) '9');
        randomAccessFile.close();
    }
}
