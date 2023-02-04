package websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDate;

/**
 * @author sofency
 * @date 2023/2/4
 */
public class NettyWebSockerFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.println("服务器收到消息"+msg.text());
        ctx.writeAndFlush(new TextWebSocketFrame("服务器时间"+LocalDate.now()));
    }

    /**
     * 连接后会触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("add"+ctx.channel().id().asLongText());
        System.out.println("add"+ctx.channel().id().asShortText()); //不唯一
    }

    
}
