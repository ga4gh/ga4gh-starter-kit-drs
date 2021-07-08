package org.ga4gh.starterkit.drs.utils.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Cache singleton storing information mapping DRS Object ids and access ids
 * to the byte source for a requested DRSObject
 */
public class AccessCache {

    /**
     * a cache mapping DRSObject + access id to richer AccessCacheItem info
     */
    private LoadingCache<String, AccessCacheItem> cache;

    /**
     * Instantiates a new AccessCache
     */
    public AccessCache() {
        buildCache();
    }

    /**
     * Builds the cache
     */
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

    /**
     * Add a new item to the cache
     * @param objectId DRS Object id
     * @param accessId access id
     * @param value the access cache item providing info on how to access the bytes
     */
    public void put(String objectId, String accessId, AccessCacheItem value) {
        cache.put(getCompositeKey(objectId, accessId), value);
    }

    /**
     * Retrieve an item from the cache
     * @param objectId DRS Object id
     * @param accessId access id
     * @return the access cache item for the given ids, provides info on how to access the bytes
     */
    public AccessCacheItem get(String objectId, String accessId) {
        return cache.getIfPresent(getCompositeKey(objectId, accessId));
    }

    /**
     * Construct a key for the cache based on DRS Object id and access id
     * @param objectId DRS Object id
     * @param accessId access id
     * @return a composite id constructed from both ids
     */
    private String getCompositeKey(String objectId, String accessId) {
        return objectId.toString() + ":" + accessId;
    }
}
