package ini.kelvin.fkgfw.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ChannelBuf;
import io.netty.buffer.UnsafeByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/30/2013
 */

public class LoggingHandler extends ChannelHandlerAdapter implements ChannelInboundHandler {
    public static enum LogLevel { // three levels will be enough
        DEBUG,
        INFO,
        ERROR,
    }

    private static final Logger log = LoggerFactory.getLogger(LoggingHandler.class);

    private LogLevel level;
    private Charset charset = Charset.forName("UTF-8");

    public LoggingHandler() {
        this(LogLevel.DEBUG);
    }

    public LoggingHandler(LogLevel level) {
        this.level = level;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log("A new channel registered: {}.", ctx.channel());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log("A channel un-registered: {}.", ctx.channel());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log("A channel activated: {}.", ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log("A channel inactivated: {}.", ctx.channel());
        super.channelInactive(ctx);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        log("A channel caught exception: {}, exception: {}.", ctx.channel(), cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,
                                   Object evt) throws Exception {
        log("A channel triggered user event: {}, event: {}.", ctx.channel(), evt);
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void bind(ChannelHandlerContext ctx,
                     SocketAddress localAddress, ChannelFuture future) throws Exception {
        log("A channel bound: {}, local address: {}.", ctx.channel(), localAddress);
        super.bind(ctx, localAddress, future);
    }

    @Override
    public void connect(ChannelHandlerContext ctx,
                        SocketAddress remoteAddress, SocketAddress localAddress,
                        ChannelFuture future) throws Exception {
        log("A channel connected: {}, remote address: {}.", ctx.channel(), remoteAddress);
        super.connect(ctx, remoteAddress, localAddress, future);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx,
                           ChannelFuture future) throws Exception {
        log("A channel disconnected: {}.", ctx.channel());
        super.disconnect(ctx, future);
    }

    @Override
    public void close(ChannelHandlerContext ctx,
                      ChannelFuture future) throws Exception {
        log("A channel closed: {}.", ctx.channel());
        super.close(ctx, future);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx,
                           ChannelFuture future) throws Exception {
        log("A channel de-registered: {}.", ctx.channel());
        super.deregister(ctx, future);
    }

    @Override
    public void flush(ChannelHandlerContext ctx, ChannelFuture future)
            throws Exception {
        ctx.flush(future);
    }

    @Override
    public void inboundBufferUpdated(ChannelHandlerContext ctx)
            throws Exception {
        ByteBuf in = ctx.inboundByteBuffer();
        if(in != null && in.readable() && in.readableBytes() > 0) {
            log("Data transferred from {}, content is as below:\n{}", ctx.channel(), in.toString(charset));
        } else {
            log("Inbound buffer updated, but no readable data: {}.", ctx.channel());
        }
        ctx.nextInboundByteBuffer().writeBytes(in);
        ctx.fireInboundBufferUpdated();
    }

    @Override
    public ChannelBuf newInboundBuffer(ChannelHandlerContext ctx) throws Exception {
        return ctx.alloc().buffer();
    }

    @Override
    public void freeInboundBuffer(ChannelHandlerContext ctx, ChannelBuf buf) throws Exception {
        ((UnsafeByteBuf) buf).free();
    }

    private void log(String pattern, Object... args) {
        if(level.equals(LogLevel.DEBUG) && log.isDebugEnabled()) {
            log.debug(pattern, args);
        } else if(level.equals(LogLevel.INFO) && log.isInfoEnabled()) {
            log.info(pattern, args);
        } else if(level.equals(LogLevel.ERROR) && log.isErrorEnabled()) {
            log.error(pattern, args);
        }
    }
}
