package ini.kelvin.fkgfw.client.http;

import ini.kelvin.fkgfw.client.LoggingHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/01/2013
 */

public class HttpProxyClientInitializer extends ChannelInitializer<SocketChannel> {

    private Channel localChannel;

    public HttpProxyClientInitializer(Channel localChannel) {
        this.localChannel = localChannel;
    }

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(LoggingHandler.NAME, new LoggingHandler());

        channel.pipeline().addLast(HttpUtils.HTTP_REQUEST_ENCODER_NAME, new HttpRequestEncoder()); // out, message => byte
        channel.pipeline().addLast(RelayHandler.NAME, new RelayHandler(localChannel)); // in, byte => byte
    }
}
