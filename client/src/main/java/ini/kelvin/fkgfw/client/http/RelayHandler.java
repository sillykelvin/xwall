package ini.kelvin.fkgfw.client.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/30/2013
 */

public final class RelayHandler extends ChannelInboundByteHandlerAdapter {
    private static final String name = "RELAY_HANDLER";

    public static String name() {
        return name;
    }

    private final Channel relayChannel;

    public RelayHandler(Channel relayChannel) {
        this.relayChannel = relayChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
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
        if (relayChannel.isActive()) {
            relayChannel.flush().addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}