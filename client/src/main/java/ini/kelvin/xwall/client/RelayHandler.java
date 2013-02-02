package ini.kelvin.xwall.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/30/2013
 */

public final class RelayHandler extends ChannelInboundByteHandlerAdapter {
    public static final String NAME = "RELAY_HANDLER";

    private static final Logger log = LoggerFactory.getLogger(RelayHandler.class);

    private Channel relayChannel;

    public RelayHandler() {}

    public RelayHandler(Channel relayChannel) {
        this.relayChannel = relayChannel;
    }

    public void setRelayChannel(Channel relayChannel) {
        this.relayChannel = relayChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(relayChannel == null) {
            throw new IllegalStateException("The relay channel has not been initialized");
        }

        ByteBuf out = relayChannel.outboundByteBuffer();
        out.discardReadBytes();
        out.writeBytes(in);
        in.clear();
        if (relayChannel.isActive()) {
            relayChannel.flush();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (relayChannel != null && relayChannel.isActive()) {
            relayChannel.flush().addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogHelper.logException(log, "Exception caught", cause);
        ctx.close();
    }
}