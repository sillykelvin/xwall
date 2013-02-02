package ini.kelvin.fkgfw.client;

import ini.kelvin.fkgfw.client.http.HttpProxyRequestDispatcher;
import ini.kelvin.fkgfw.client.http.HttpUtils;
import ini.kelvin.fkgfw.client.http.RelayHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
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

        pipeline.addLast(LoggingHandler.NAME, new LoggingHandler());

        // be sure this handler is before HttpProxyRequestSender, because when there is a https request, it will
        // write back a DefaultHttpResponse, which should be encoded by this handler
        pipeline.addLast(HttpUtils.HTTP_RESPONSE_ENCODER_NAME, new HttpResponseEncoder()); // out, message => byte

        pipeline.addLast(HttpProxyRequestDispatcher.NAME, new HttpProxyRequestDispatcher());
        pipeline.addLast(HttpProxyRequestDecoder.NAME, new HttpProxyRequestDecoder()); // in, byte => message
        pipeline.addLast(HttpProxyRequestProcessor.NAME, new HttpProxyRequestProcessor()); // in, message => message
        pipeline.addLast(HttpProxyRequestSender.NAME, new HttpProxyRequestSender()); // in, message stop processing here

        // in, byte => byte, send it to relay channel
        // be careful !!! must set the outboundChannel to it later !!!
        pipeline.addLast(RelayHandler.NAME, new RelayHandler());
    }
}