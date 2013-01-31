package ini.kelvin.fkgfw.client.http;

import ini.kelvin.fkgfw.client.ConnectionCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/30/2013
 */

public class HttpProxyServerHandler extends ChannelInboundMessageHandlerAdapter<HttpRequest> {
    private static final String name = "HTTP_PROXY_SERVER_HANDLER";

    public static String name() {
        return name;
    }

    public HttpProxyServerHandler() {
        super(HttpRequest.class);
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final HttpRequest request) throws Exception {
        final boolean isHttps = request.getMethod().equals(HttpMethod.CONNECT);
        int remotePort = 80;
        if(isHttps) {
            remotePort = 443; // TODO can this be hard coded ?
        }

        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void connectionSucceeded(ChannelHandlerContext outboundCtx) {
                final Channel outboundChannel = outboundCtx.channel();

                if(isHttps) {
                    ctx.channel().write(new DefaultHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(200, "Connection established")))
                            .addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    ctx.pipeline().remove("decoder");
                                    ctx.pipeline().remove("encoder");
                                    ctx.pipeline().remove(name());

                                    outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                                    ctx.channel().pipeline().addLast(new RelayHandler(outboundChannel));
                                }
                            });
                } else {
                    ctx.pipeline().addLast(RelayHandler.name(), new RelayHandler(outboundChannel));
                    ctx.pipeline().remove("decoder");
                    ctx.pipeline().remove(name());

                    outboundChannel.pipeline().addLast("encoder", new HttpRequestEncoder());
                    outboundChannel.pipeline().addLast(RelayHandler.name(), new RelayHandler(ctx.channel()));
                    outboundChannel.write(request);
                }
            }

            @Override
            public void connectionFailed(ChannelHandlerContext ctx) {
                // TODO add some processing logic here
            }
        };

        Bootstrap client = new Bootstrap();

        client.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .remoteAddress(HttpHeaders.getHost(request), remotePort)
                .handler(new HttpProxyClientHandler(callback))
                .connect();
    }












//    private HttpRequestDecoder decoder = new HttpRequestDecoder();
//
//    @Override
//    public void inboundBufferUpdated(final ChannelHandlerContext ctx, final ByteBuf in) throws Exception {
//        // 1. need to catch exception
//        // 2. need to verify if the result is null
//        // 3. is a message ? need to verify if it is invalid (decode failure)
//        // 4. is a chunk ?
//        Object result;
//        try {
//            result = decoder.decode(ctx, in); // the decode operation will always fail, WTF...
//        } catch(Exception e) {
//            result = null;
//        }
//
//        if(result == null) {
//            ctx.close();
//            return;
//        }
//
//
//        if(!(result instanceof HttpRequest)) { // TODO maybe a chunk here
//            ctx.close();
//            return;
//        }
//
//        HttpRequest request = (HttpRequest) result;
//        if(!request.getDecoderResult().equals(DecoderResult.SUCCESS)) {
//            ctx.close();
//            return;
//        }
//        request.removeHeader("Proxy-Connection");
//
//        Bootstrap client = new Bootstrap();
//
//        client.group(ctx.channel().eventLoop())
//                .channel(NioSocketChannel.class)
//                .remoteAddress(HttpHeaders.getHost(request),
//                        request.getMethod().equals(HttpMethod.CONNECT) ? 443 : 80); // TODO improve
//                //.handler(null);
//
//        ChannelFuture f = client.connect();
//        final Channel outboundChannel = f.channel();
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if(future.isSuccess()) {
//                    ctx.pipeline().addLast(new RelayHandler(outboundChannel))
//                            .remove(HttpProxyServerHandler.this);
//                    outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
//                    ctx.nextInboundByteBuffer().writeBytes(in);
//                    ctx.fireInboundBufferUpdated();
//                } else {
//                    ctx.channel().close();
//                }
//            }
//        });
//    }
}
