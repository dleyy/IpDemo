package com.example.lilei.iptest.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.lilei.iptest.R;

/**
 * Created by lilei on 2018/7/31.
 */

public class ImageHolder extends RecyclerView.ViewHolder {

    private ImageView defImg;
    private ImageView imgChecked;

    public ImageHolder(View itemView) {
        super(itemView);
        defImg = itemView.findViewById(R.id.img);
        imgChecked = itemView.findViewById(R.id.img_selected);
    }
}
