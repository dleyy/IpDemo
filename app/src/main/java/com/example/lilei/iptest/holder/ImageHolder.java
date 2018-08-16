package com.example.lilei.iptest.holder;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.lilei.iptest.Constant;
import com.example.lilei.iptest.R;
import com.example.lilei.iptest.model.Image;
import com.example.lilei.iptest.utils.Utils;


/**
 * Created by lilei on 2018/7/31.
 */

public class ImageHolder extends RecyclerView.ViewHolder {

    public ImageView defImg;
    public ImageView imgChecked;
    private Activity activity;

    public ImageHolder(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        defImg = itemView.findViewById(R.id.img);
        imgChecked = itemView.findViewById(R.id.img_selected);
        ViewGroup.LayoutParams layoutParams = defImg.getLayoutParams();
        int needSize = (Utils.getPhoneWidth(activity)) / 3;
        layoutParams.width = needSize;
        layoutParams.height = needSize;
        defImg.setLayoutParams(layoutParams);
    }

    public void setImageResource(Image image) {
        if (image.isCamera) {
            defImg.setImageResource(R.mipmap.photo);
            imgChecked.setVisibility(View.GONE);
        } else {
            Constant.config.loader.loadImage(activity, image.path, defImg);
        }
    }

    public void setCheckImg(boolean checked) {
        imgChecked.setBackgroundResource(checked ? R.mipmap.ic_checked : R.mipmap.ic_uncheck);
    }
}
