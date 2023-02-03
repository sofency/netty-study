package http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author sofency
 * @date 2023/2/3
 */
public class NettyHttpInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        System.out.println("初始化开始");
        ch.pipeline().addLast("HttpRequestCodec", new HttpServerCodec())
                .addLast(new NettyHttpHandler());
    }
}
