package com.example.lilei.iptest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.lilei.iptest.R;
import com.example.lilei.iptest.holder.ImageHolder;
import com.example.lilei.iptest.interfaces.OnImageClickedListener;
import com.example.lilei.iptest.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilei on 2018/7/31.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageHolder> {

    private Activity activity;
    private List<Image> list;
    private OnImageClickedListener onImageClickedListener;

    public ImageListAdapter(Activity activity, List<Image> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_image_item,
                null, false);
        return new ImageHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, final int position) {

    }

    @Override
    public void onBindViewHolder(ImageHolder holder, final int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            holder.setImageResource(list.get(position));
            holder.setCheckImg(list.get(position).isChecked);

            if (onImageClickedListener != null) {
                holder.defImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImageClickedListener.onImageClicked(position);
                    }
                });

                holder.imgChecked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImageClickedListener.onImageChecked(position);
                    }
                });
            }
        } else {
            int type = (int) payloads.get(0);
            switch (type) {
                case 0:
                    holder.setCheckImg(list.get(position).isChecked);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnImageClickedListener(OnImageClickedListener onImageClickedListener) {
        this.onImageClickedListener = onImageClickedListener;
    }

    public List<Image> getAllData() {
        return list;
    }
}
