package com.example.lilei.iptest.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

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
    public static int getPhoneHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
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

    /**
     * 递归创建文件夹
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                Log.i("createFile","----- 创建文件" + file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                Log.i("createFile","----- 创建文件" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    public static int getPopupMenuHeight(final View authorView) {
        WindowManager wm = (WindowManager) authorView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int[] locInWindow = new int[2];
        authorView.getLocationInWindow(locInWindow);
        int height = metrics.heightPixels - locInWindow[1] - authorView.getHeight();
        return height;
    }

    public static int dp2px(int dp, Activity activity) {
        return (int) (activity.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    //获取导航栏高度
    public static int getNavigatorBarHeight(Context context) {
        int result = 0;
        int resourceId=0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }
}
