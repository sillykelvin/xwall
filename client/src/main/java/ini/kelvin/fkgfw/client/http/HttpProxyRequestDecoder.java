package ini.kelvin.fkgfw.client.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMessageDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.ssl.SslHandler;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/01/2013
 */

public class HttpProxyRequestDecoder extends HttpRequestDecoder {
    public static final String NAME = "HTTP_PROXY_REQUEST_DECODER";

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(ctx.channel().isActive() && SslHandler.isEncrypted(in)) {
            // this is a trick invocation, if the state is not changed, and the inbound
            // buffer is not consumed, netty will complain that the handler should consume
            // "at least one byte", so we change the state to make netty think that the
            // inbound buffer "is consumed"
            state(HttpMessageDecoder.State.BAD_MESSAGE);
            return in;
        }
        return super.decode(ctx, in);
    }
}