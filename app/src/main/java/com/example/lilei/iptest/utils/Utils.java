package com.example.lilei.iptest.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
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

    //获取屏幕宽度
    public static int getPhoneWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕高度
    public static int getPhoneHeight(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                Log.i("createDir", "----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                Log.i("createDir", "----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }


}
