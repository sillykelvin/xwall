package ini.kelvin.fkgfw.client;

import ini.kelvin.fkgfw.client.http.HttpProxyClientInitializer;
import ini.kelvin.fkgfw.client.http.HttpUtils;
import ini.kelvin.fkgfw.client.http.RelayHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/01/2013
 */

public class HttpProxyRequestSender extends ChannelInboundMessageHandlerAdapter<HttpRequest> {
    public static final String NAME = "HTTP_PROXY_REQUEST_SENDER";
    private Bootstrap client;

    public HttpProxyRequestSender() {
        this.client = new Bootstrap();
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final HttpRequest msg) throws Exception {
        String host = ctx.channel().attr(HttpUtils.REMOTE_HOST).get();
        int port = ctx.channel().attr(HttpUtils.REMOTE_PORT).get();
        final boolean isHttps = ctx.channel().attr(HttpUtils.IS_HTTPS).get();

        final Channel inboundChannel = ctx.channel();

        client.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new HttpProxyClientInitializer(inboundChannel));

        final ChannelFuture f = client.connect();

        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()) {
                    ((RelayHandler)ctx.pipeline().get(RelayHandler.NAME)).setRelayChannel(f.channel());

                    if(isHttps) {
                        ctx.write(HttpUtils.HTTPS_INIT_RESPONSE);
                    } else {
                        f.channel().write(msg);
                    }
                } else {
                    if(ctx.channel().isActive()) {
                        ctx.channel().flush().addListener(ChannelFutureListener.CLOSE);
                    }
                }
            }
        });
    }
}
