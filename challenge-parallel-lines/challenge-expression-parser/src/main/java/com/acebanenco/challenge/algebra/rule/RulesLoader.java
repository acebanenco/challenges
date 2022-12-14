package com.acebanenco.challenge.algebra.rule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RulesLoader {

    String loadRulesText(String resourceName) {
        URL resource = RuleFactory.class.getClassLoader().getResource(resourceName);
        if ( resource == null ) {
            throw new RuntimeException("Not found " + resourceName);
        }

        try (InputStream inputStream = resource.openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            inputStream.transferTo(outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
