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
import com.example.lilei.iptest.interfaces.OnImageClickedListener;
import com.example.lilei.iptest.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilei on 2018/7/31.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageHolder> {

    private Context context;
    private List<Image> list;
    private OnImageClickedListener onImageClickedListener;

    public ImageListAdapter(Context context, List<Image> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_image_item,
                parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, final int position) {
        holder.setImageResource(list.get(position));
        holder.setCheckImg(list.get(position).isChecked);
        if (onImageClickedListener!=null){
            holder.defImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickedListener.onImageClicked(position);
                }
            });

            holder.imgChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).isChecked = !list.get(position).isChecked;
                    onImageClickedListener.onImageChecked(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnImageClickedListener(OnImageClickedListener onImageClickedListener) {
        this.onImageClickedListener = onImageClickedListener;
    }
}
