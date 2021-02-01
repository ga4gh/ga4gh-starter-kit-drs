package org.ga4gh.drs.testutils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {

    public static String load(String resourcePath) throws IOException {
        String filename = ResourceLoader.class.getResource(resourcePath).getFile();
        FileInputStream fs = new FileInputStream(filename);
        byte[] bytes = fs.readAllBytes();
        String json = new String(bytes, StandardCharsets.UTF_8);
        fs.close();
        return json;
    }
}
