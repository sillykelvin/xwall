package ini.kelvin.xwall.client.http;

import ini.kelvin.xwall.client.RelayHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/01/2013
 */

public class HttpProxyRequestDispatcher extends ChannelInboundByteHandlerAdapter {
    public static final String NAME = "HTTP_PROXY_REQUEST_DISPATCHER";

    @Override
    public void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(shouldRemoveMessageHandler(ctx, in)) {
            ChannelPipeline pipeline = ctx.pipeline();
            pipeline.remove(HttpProxyRequestDecoder.NAME);
            pipeline.remove(HttpProxyRequestProcessor.NAME);
            pipeline.remove(HttpProxyRequestSender.NAME);

            pipeline.remove(this); // also remove this handler itself
        }

        ctx.nextInboundByteBuffer().writeBytes(in);
        ctx.fireInboundBufferUpdated();
    }

    private boolean shouldRemoveMessageHandler(ChannelHandlerContext ctx, ByteBuf in) {
        RelayHandler relayHandler = (RelayHandler)ctx.pipeline().get(RelayHandler.NAME);
        return relayHandler.getRelayChannel() != null || SslHandler.isEncrypted(in); // TODO improve the performance here
    }
}
