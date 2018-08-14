package com.example.lilei.iptest.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.lilei.iptest.R;
import com.example.lilei.iptest.adapter.FolderListAdapter;
import com.example.lilei.iptest.config.ImagePickerConfig;
import com.example.lilei.iptest.interfaces.OnFolderClickedListener;
import com.example.lilei.iptest.model.Folder;
import com.example.lilei.iptest.utils.Utils;

import java.util.List;

/**
 * Created by lilei on 2018/8/7.
 */

public class ImagePathPopupWindow {

    private PopupWindow popupWindow;
    private FolderListAdapter adapter;
    private View parentsView;
    private RecyclerView recyclerView;
    private Activity activity;

    public ImagePathPopupWindow(Activity activity, View targetView,
                                int height, ImagePickerConfig config,
                                PopupWindow.OnDismissListener listener,
                                List<Folder> folders) {
        parentsView = targetView;
        this.activity = activity;
        adapter = new FolderListAdapter(folders, config, activity, 0);

        View conventView = LayoutInflater.from(activity).inflate(R.layout.view_folder_list,
                null);
        recyclerView = conventView.findViewById(R.id.folders);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        //防止notifyItemChanged闪烁
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        popupWindow = new PopupWindow(conventView, LinearLayout.LayoutParams.MATCH_PARENT,
                height * 7 / 10, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x7DC0C0C0));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(listener);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindowStyle);
    }

    public void setOnItemClickListener(OnFolderClickedListener listener) {
        adapter.setOnFolderClickedListener(listener);
    }

    public void show() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(parentsView, Gravity.BOTTOM, 0, Utils.dp2px(50, activity));
        }
    }

    public void dismiss() {

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

}
