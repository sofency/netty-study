package chat.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author sofency
 * @date 2023/2/4
 */
public class NettyChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("chatHandler", new NettyChatServerHandler());
        pipeline.addLast("idleHandler",
                new IdleStateHandler(10, 10, 20, TimeUnit.SECONDS));
        // 上一步监听的 交给下一个handler处理
        pipeline.addLast(new NettyIdleStateHandler());
    }
}
