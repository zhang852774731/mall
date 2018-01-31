package com.bin.action.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangbin on 17/12/16.
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static String TOKEN_PREFIX = "token_";
    private static LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return null;
                }
            });

    public static String getKey(String key){
        String value = null;
        try {
            value = loadingCache.get(key);
        } catch (ExecutionException e) {
            logger.error("读取缓存异常",e);
            return null;
        }
        return value;
    }

    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }
}
