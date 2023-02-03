import handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author sofency
 * @date 2023/2/3
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 只处理连接请求
         */
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1); 这样只有一个NioEventLoop

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        /**
         * 真正和客户端业务处理，会交给workerGroup完成
         * 两者默认实际线程数 cpu核数*2
         * 客户端在分配时，按顺序循环非配worker线程
         */
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持连接状态
                    .handler(null) //对BossGroup生效
                    .childHandler(new ChannelInitializer<SocketChannel>() { //对WorkerGroup生效
                        // 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyServerHandler());
                        }
                    }); //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("server is ready");
            //绑定一个端口并同步处理
            //future listener机制
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("Hello World");
                } else {
                    System.out.println("绑定失败");
                }
            });
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
