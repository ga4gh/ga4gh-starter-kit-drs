package org.ga4gh.drs.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeImputer {

    private static final Map<String,String> mimeTypeMap = new HashMap<>() {{
        put("json", "application/json");
    }};

    public static String imputeMimeType(String extension) {
        return mimeTypeMap.get(extension);
    }
}
