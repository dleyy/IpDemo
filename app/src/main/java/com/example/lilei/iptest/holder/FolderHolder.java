package com.example.lilei.iptest.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lilei.iptest.R;
import com.example.lilei.iptest.config.ImagePickerConfig;
import com.example.lilei.iptest.model.Folder;
import com.example.lilei.iptest.model.Image;

/**
 * Created by lilei on 2018/8/7.
 */

public class FolderHolder extends RecyclerView.ViewHolder {

    public ImageView coverImg;
    public TextView folderName;
    public TextView imageSize;
    public ImageView checked;

    public FolderHolder(View itemView) {
        super(itemView);
        coverImg = itemView.findViewById(R.id.ivFolder);
        folderName = itemView.findViewById(R.id.tvFolderName);
        imageSize = itemView.findViewById(R.id.tvImageNum);
        checked = itemView.findViewById(R.id.indicator);
    }

}
