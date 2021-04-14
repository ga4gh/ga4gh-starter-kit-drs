package org.ga4gh.starterkit.drs.utils.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class AccessCache {

    private LoadingCache<String, AccessCacheItem> cache;

    public AccessCache() {
        buildCache();
    }

    private void buildCache() {
        cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(
                new CacheLoader<String, AccessCacheItem>(){
                    public AccessCacheItem load(String key) {
                        return new AccessCacheItem();
                    }
                }
            );
    }

    public void put(String objectId, String accessId, AccessCacheItem value) {
        cache.put(getCompositeKey(objectId, accessId), value);
    }

    public AccessCacheItem get(String objectId, String accessId) {
        return cache.getIfPresent(getCompositeKey(objectId, accessId));
    }

    private String getCompositeKey(String objectId, String accessId) {
        return objectId + ":" + accessId;
    }
}
