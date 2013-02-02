package ini.kelvin.fkgfw.client.http;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/01/2013
 */

public final class HttpUtils {
    private HttpUtils() {}

    public static final int DEFAULT_HTTP_PORT = 80;
    public static final int DEFAULT_HTTPS_PORT = 443;

    public static final String PROXY_CONNECTION_HEADER = "Proxy-Connection";
    public static final String HTTP_REQUEST_ENCODER_NAME = "HTTP_REQUEST_ENCODER";
    public static final String HTTP_RESPONSE_ENCODER_NAME = "HTTP_RESPONSE_ENCODER";

    public static final HttpResponseStatus PROXY_ESTABLISHED = new HttpResponseStatus(200, "Connection established");

    public static final HttpResponse HTTPS_INIT_RESPONSE = new DefaultHttpResponse(HttpVersion.HTTP_1_1, PROXY_ESTABLISHED);

    public static final AttributeKey<String> REMOTE_HOST = new AttributeKey<String>("remote.host");
    public static final AttributeKey<Integer> REMOTE_PORT = new AttributeKey<Integer>("remote.port");
    public static final AttributeKey<Boolean> IS_HTTPS = new AttributeKey<Boolean>("schema.https");
}
