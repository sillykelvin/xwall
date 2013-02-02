package ini.kelvin.fkgfw.client.http;

import ini.kelvin.fkgfw.client.ProxyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/29/2013
 */

public class HttpProxyServer implements ProxyServer {
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