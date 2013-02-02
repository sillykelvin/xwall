package ini.kelvin.fkgfw.client;

import org.slf4j.Logger;

/**
 * Author: kelvin_hu
 * Email:  ini.kelvin@gmail.com
 * Date:   02/02/2013
 */

public final class LogHelper {
    private LogHelper() {}

    public static void logException(Logger log, String description, Throwable cause) {
        log.error(description + ", message: {}, stack trace:\n{}", cause.getMessage(), formatException(cause));
    }

    private static String formatException(Throwable cause) {
        StringBuffer sb = new StringBuffer();
        sb.append(cause).append("\n");

        StackTraceElement[] trace = cause.getStackTrace();
        for(StackTraceElement e : trace) {
            sb.append("\tat ").append(e).append("\n");
        }

        Throwable ourCause = cause.getCause();
        if (ourCause != null) {
            sb.append(formatException(ourCause));
        }
        return sb.toString();
    }
}
