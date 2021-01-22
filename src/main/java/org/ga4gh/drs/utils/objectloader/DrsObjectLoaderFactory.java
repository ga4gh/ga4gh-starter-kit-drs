package org.ga4gh.drs.utils.objectloader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.ga4gh.drs.model.AccessType;

public class DrsObjectLoaderFactory {

    private static final HashMap<AccessType,Class<? extends DrsObjectLoader>> objectLoaderDict = loadObjectLoaderDict();

    public DrsObjectLoaderFactory() {

    }

    public DrsObjectLoader createDrsObjectLoader(AccessType accessType) {
        DrsObjectLoader loader = null;

        try {
            Class<? extends DrsObjectLoader> loaderClass = objectLoaderDict.get(accessType);
            loader = loaderClass.getConstructor().newInstance();
        } catch (NullPointerException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return loader;
        }
        return loader;
    }

    private static HashMap<AccessType,Class<? extends DrsObjectLoader>> loadObjectLoaderDict() {
        return new HashMap<AccessType,Class<? extends DrsObjectLoader>>(){{
            put(AccessType.FILE, FileDrsObjectLoader.class);
            put(AccessType.HTTPS, HttpsDrsObjectLoader.class);
        }};
    }
    
}
