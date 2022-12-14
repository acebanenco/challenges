package com.acebanenco.challenge.opencl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class CLResourceLoader {

    String loadResource(ClassLoader classLoader, String resource) {
        URL url = getUrl(classLoader, resource);
        URLConnection connection = getUrlConnection(url);
        String contentEncoding = connection.getContentEncoding();
        try (InputStream input = connection.getInputStream()) {
            return copyToString(input, contentEncoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String copyToString(InputStream input, String contentEncoding) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            input.transferTo(buffer);
            return contentEncoding != null
                    ? buffer.toString(contentEncoding)
                    : buffer.toString(StandardCharsets.UTF_8);
        }
    }

    private static URLConnection getUrlConnection(URL url) {
        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private static URL getUrl(ClassLoader classLoader, String resource) {
        URL url = classLoader.getResource(resource);
        if ( url == null ) {
            throw new RuntimeException("Resource not found: " + resource);
        }
        return url;
    }


}
