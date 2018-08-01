package com.example.lilei.iptest.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * Created by lilei on 2018/8/1.
 */

public class CacheUtils {

    private final static int CACHE_SIZE = 4 * 1024;

    private final LruCache<String, Bitmap> imgCache = new LruCache<>(CACHE_SIZE);

    private CacheUtils() {
    }

    private static CacheUtils cacheUtils;

    public static CacheUtils getInstance() {
        if (cacheUtils == null) {
            cacheUtils = new CacheUtils();
        }
        return cacheUtils;
    }

    public void putBitmap(String path, Bitmap bitmap) {
        if (TextUtils.isEmpty(path) || bitmap == null) {
            return;
        }
        imgCache.put(path, bitmap);
    }

    public Bitmap getBitmap(String path) {
        if (TextUtils.isEmpty(path) || imgCache.size() == 0) {
            return null;
        }
        return imgCache.get(path);
    }
}
