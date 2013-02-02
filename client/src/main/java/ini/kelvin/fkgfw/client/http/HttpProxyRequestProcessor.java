package ini.kelvin.fkgfw.client.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerUtil;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/01/2013
 */

public class HttpProxyRequestProcessor extends ChannelInboundMessageHandlerAdapter<HttpRequest> {
    public static final String NAME = "HTTP_PROXY_REQUEST_PROCESSOR";

    @Override
    public void messageReceived(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
        String addr = msg.getHeader(HttpHeaders.Names.HOST);
        if(addr == null) {
            throw new IllegalStateException("No host specified");
        }

        String[] ss = addr.split(":");
        if(ss.length != 1 && ss.length != 2) {
            throw new IllegalStateException("Invalid request header");
        }

        boolean isHttps = msg.getMethod().equals(HttpMethod.CONNECT);
        String host = ss[0];
        int port = isHttps ? HttpUtils.DEFAULT_HTTPS_PORT : HttpUtils.DEFAULT_HTTP_PORT;

        if(ss.length == 2) {
            port = Integer.parseInt(ss[1]);
        }

        ctx.channel().attr(HttpUtils.REMOTE_HOST).set(host);
        ctx.channel().attr(HttpUtils.REMOTE_PORT).set(port);
        ctx.channel().attr(HttpUtils.IS_HTTPS).set(isHttps);

        // remove the proxy related headers
        msg.removeHeader(HttpUtils.PROXY_CONNECTION_HEADER);

        if(!ChannelHandlerUtil.unfoldAndAdd(ctx, msg, true)) {
            throw new IllegalStateException("Need extra handlers to process the http request.");
        }

        ctx.fireInboundBufferUpdated();
    }
}
