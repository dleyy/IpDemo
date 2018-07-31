package com.example.lilei.iptest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.lilei.iptest.R;
import com.example.lilei.iptest.holder.ImageHolder;
import com.example.lilei.iptest.model.Image;

import java.util.List;

/**
 * Created by lilei on 2018/7/31.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageHolder> {

    private Context context;

    public ImageListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_image_item,
                parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
