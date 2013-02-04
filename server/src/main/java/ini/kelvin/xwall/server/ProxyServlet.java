package ini.kelvin.xwall.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/04/2013
 */

public class ProxyServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch(IOException e) {
            out = null;
        }

        if(out != null) {
            out.write("xWall server is working now.");
        }
    }
}
