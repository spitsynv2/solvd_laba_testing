package com.solvd.util;

import java.net.URI;
import java.net.URL;

public final class Urls {
    private Urls() {}

    /** Returns scheme://host:port from any Selenium hub URL (strips /wd/hub, /, etc.) */
    public static String origin(URL seleniumHubUrl) {
        try {
            URI u = seleniumHubUrl.toURI();
            String scheme = u.getScheme();
            String host = u.getHost();
            int port = u.getPort();

            // If port missing, infer common defaults
            if (port == -1) {
                port = "https".equalsIgnoreCase(scheme) ? 443 : 80;
            }
            return scheme + "://" + host + ":" + port;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract origin from selenium hub URL: " + seleniumHubUrl, e);
        }
    }

    /** Same origin but with a different port. */
    public static String originWithPort(URL seleniumHubUrl, int newPort) {
        try {
            URI u = seleniumHubUrl.toURI();
            String scheme = u.getScheme();
            String host = u.getHost();
            return scheme + "://" + host + ":" + newPort;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build origin with port from: " + seleniumHubUrl, e);
        }
    }
}
