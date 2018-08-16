package com.example.lilei.iptest.interfaces;

import android.support.v7.widget.SimpleItemAnimator;

import com.example.lilei.iptest.model.Image;

/**
 * Created by lilei on 2018/8/1.
 */

public interface OnImageClickedListener {

    void onImageClicked(int position, Image image);

    void onImageChecked(int position);
}
