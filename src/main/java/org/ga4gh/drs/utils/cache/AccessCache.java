package org.ga4gh.drs.utils.cache;

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

    public void put(String key, AccessCacheItem value) {
        cache.put(key, value);
    }

    public AccessCacheItem get(String key) {
        return cache.getIfPresent(key);
    }
}
