package chat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author sofency
 * @date 2023/2/4
 */
public class NettyIdleStateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            String eventState = "";
            switch (stateEvent.state()) {
                case READER_IDLE:
                    eventState = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventState = "写空闲";
                    break;
                case ALL_IDLE:
                    eventState = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"-当前状态:" + eventState);
            // 也可以关闭
        }
    }
}
