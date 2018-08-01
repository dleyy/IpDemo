package com.example.lilei.iptest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/**
 * Created by lilei on 2018/8/1.
 */

public class Utils {

    public static Bitmap ZipBitmap(String filepath) {
        Float defaultWidth = 480f;
        Float defaultHeight = 800f;
        if (CacheUtils.getInstance().getBitmap(filepath) != null) {
            return CacheUtils.getInstance().getBitmap(filepath);
        }
        return ZipBitmap(defaultWidth, defaultHeight, filepath);
    }

    public static Bitmap ZipBitmap(float targetWidth,
                                   float targetHeight,
                                   String filePath) {
        if (filePath == null) {
            return null;
        }
        Log.e("1234", "ZipBitmap: " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        float beHeight = 0f, beWidth = 0f;
        float targetBe;
        if (options.outHeight > targetHeight) {
            beHeight = options.outHeight / targetHeight;
        }
        if (options.outWidth > targetWidth) {
            beWidth = options.outWidth / targetWidth;
        }
        targetBe = beHeight > beWidth ? beHeight : beWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.outConfig = Bitmap.Config.RGB_565;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) targetBe;
        Bitmap realBitMap = BitmapFactory.decodeFile(filePath, options);
        CacheUtils.getInstance().putBitmap(filePath, realBitMap);
        return realBitMap;
    }
}
