package ini.kelvin.fkgfw.client;

import io.netty.channel.ChannelHandlerContext;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/30/2013
 */

public interface ConnectionCallback {
    public void connectionSucceeded(ChannelHandlerContext ctx);
    public void connectionFailed(ChannelHandlerContext ctx);
}
