package chat;

import chat.handler.NettyChatServerHandler;
import chat.handler.NettyChatServerInitializer;
import http.NettyHttpInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author sofency
 * @date 2023/2/4
 */
public class NettyChatServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new NettyChatServerInitializer());

            System.out.println("聊天室正在启动");
            ChannelFuture channelFuture = bootstrap.bind(6699).sync();

            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("启动成功");
                } else {
                    System.out.println("启动失败");
                }
            });
            // 关闭状态监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
