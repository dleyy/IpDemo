package com.example.lilei.iptest.interfaces;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

/**
 * Created by lilei on 2018/8/6.
 */

public interface ImageLoader {

    //添加自定义加载
    void loadImage(Context context, String imagePath, ImageView imageView);
}
