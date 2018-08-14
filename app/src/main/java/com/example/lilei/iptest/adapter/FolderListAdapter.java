package com.example.lilei.iptest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lilei.iptest.R;
import com.example.lilei.iptest.config.ImagePickerConfig;
import com.example.lilei.iptest.holder.FolderHolder;
import com.example.lilei.iptest.interfaces.OnFolderClickedListener;
import com.example.lilei.iptest.model.Folder;

import java.util.List;

/**
 * Created by lilei on 2018/8/7.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderHolder> {

    private List<Folder> folders;
    private ImagePickerConfig config;
    private Context context;
    private int currentFolder;
    private OnFolderClickedListener onFolderClickedListener;

    public FolderListAdapter(List<Folder> folders,
                             ImagePickerConfig config,
                             Context context,
                             int currentFolder) {
        this.folders = folders;
        this.config = config;
        this.context = context;
        this.currentFolder = currentFolder;

    }

    @Override
    public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_folder_item, parent, false);
        return new FolderHolder(view);
    }

    @Override
    public void onBindViewHolder(final FolderHolder holder, final int position) {
        final Folder folder = folders.get(position);
        holder.folderName.setText(folder.name);
        holder.imageSize.setText(String.format(context.getResources().getString(R.string.has_images)
                , folder.images.size() + ""));
        config.loader.loadImage(context,
                folder.images.size() > 0 ? folder.images.get(0).path : "-1",
                holder.coverImg);
        holder.checked.setVisibility(position == currentFolder ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int passIndex = currentFolder;
                currentFolder = position;
                notifyItemChanged(passIndex);
                holder.checked.setVisibility(View.VISIBLE);
                if (onFolderClickedListener != null) {
                    onFolderClickedListener.onFolderClick(position, folders.get(position).images);
                }
            }
        });
    }

    public void setOnFolderClickedListener(OnFolderClickedListener onFolderClickedListener) {
        this.onFolderClickedListener = onFolderClickedListener;
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

}
