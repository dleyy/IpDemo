package com.example.lilei.iptest.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.lilei.iptest.R;
import com.example.lilei.iptest.model.Image;
import com.example.lilei.iptest.utils.Utils;


/**
 * Created by lilei on 2018/7/31.
 */

public class ImageHolder extends RecyclerView.ViewHolder {

    public ImageView defImg;
    public ImageView imgChecked;

    public ImageHolder(View itemView) {
        super(itemView);
        defImg = itemView.findViewById(R.id.img);
        imgChecked = itemView.findViewById(R.id.img_selected);
    }

    public void setImageResource(Image image) {
        if (image.path == null) {
            return;
        }
        defImg.setImageBitmap(Utils.ZipBitmap(image.path));
    }

    public void setCheckImg(boolean checked) {
        imgChecked.setBackgroundResource(checked ? R.mipmap.ic_checked : R.mipmap.ic_uncheck);
    }
}
