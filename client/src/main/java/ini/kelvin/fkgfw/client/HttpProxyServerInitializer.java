package ini.kelvin.fkgfw.client;

import ini.kelvin.fkgfw.client.http.HttpProxyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/29/2013
 */

public class HttpProxyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("logging", new LoggingHandler()); // TODO change the hard coded names to constants
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast(HttpProxyServerHandler.name(), new HttpProxyServerHandler());
    }
}
