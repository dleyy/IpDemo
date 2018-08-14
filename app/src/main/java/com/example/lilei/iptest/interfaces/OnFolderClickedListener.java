package com.example.lilei.iptest.interfaces;

import com.example.lilei.iptest.model.Image;

import java.util.List;

/**
 * Created by lilei on 2018/8/9.
 */

public interface OnFolderClickedListener {
    void onFolderClick(int position, List<Image> images);
}
