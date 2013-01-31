package ini.kelvin.fkgfw.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   01/27/2013
 */

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        int port = 7077; // TODO do not hard code, use configuration instead
        ProxyServer httpServer = new HttpProxyServer(port);
        try {
            log.info("Http proxy server is listening on {}...", port);
            httpServer.start();
        } finally {
            log.info("Shutting down http proxy server...");
            httpServer.shutdown();
            log.info("Http proxy server is stopped.");
        }
    }
}
