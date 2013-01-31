package ini.kelvin.fkgfw.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/29/2013
 */

public class HttpProxyServer implements ProxyServer {
    private static final Logger log = LoggerFactory.getLogger(HttpProxyServer.class);

    private int port = 7077; // default 7077
    private ServerBootstrap server;

    public HttpProxyServer(int port) {
        this.port = port;
        this.server = new ServerBootstrap();
    }

    @Override
    public void start() throws Exception {
        server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new HttpProxyServerInitializer());
        server.bind().sync().channel().closeFuture().sync();
    }

    @Override
    public void shutdown() throws Exception {
        server.shutdown();
    }
}