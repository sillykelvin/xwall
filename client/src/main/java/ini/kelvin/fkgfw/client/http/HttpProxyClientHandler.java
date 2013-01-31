package ini.kelvin.fkgfw.client.http;

import ini.kelvin.fkgfw.client.ConnectionCallback;
import ini.kelvin.fkgfw.client.LoggingHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelStateHandlerAdapter;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/29/2013
 */

public class HttpProxyClientHandler extends ChannelStateHandlerAdapter {
    private static final String name = "HTTP_PROXY_CLIENT_HANDLER";

    private final ConnectionCallback callback;

    public static String name() {
        return name;
    }

    public HttpProxyClientHandler(ConnectionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.pipeline().remove(this);
        ctx.pipeline().addLast("logging", new LoggingHandler()); // TODO hard code
        callback.connectionSucceeded(ctx);
    }
}
