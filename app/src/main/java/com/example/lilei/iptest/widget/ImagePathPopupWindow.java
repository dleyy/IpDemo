package com.example.lilei.iptest.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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
import com.example.lilei.iptest.model.Folder;

import java.util.List;

/**
 * Created by lilei on 2018/8/7.
 */

public class ImagePathPopupWindow {

    private PopupWindow popupWindow;
    private FolderListAdapter adapter;
    private View parentsView;
    private RecyclerView recyclerView;
    private Context context;

    private ValueAnimator valueShowAnimator;
    private ValueAnimator valueDismissAnimator;

    private final int animatorDuration = 300;

    public ImagePathPopupWindow(Context context, View targetView,
                                int height, ImagePickerConfig config,
                                PopupWindow.OnDismissListener listener,
                                List<Folder> folders) {
        parentsView = targetView;
        this.context = context;

        adapter = new FolderListAdapter(folders, config, context, 0);

        View conventView = LayoutInflater.from(context).inflate(R.layout.view_folder_list,
                null);
        recyclerView = conventView.findViewById(R.id.folders);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        //防止notifyItemChanged闪烁
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        initRecycleAnimator(recyclerView, height * 7 / 10);

        popupWindow = new PopupWindow(conventView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x80ff00ff));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(listener);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.AppTheme);

    }

    private void initRecycleAnimator(final RecyclerView recyclerView, int height) {
        valueShowAnimator = ValueAnimator.ofInt(0, height);
        valueDismissAnimator = ValueAnimator.ofInt(recyclerView.getHeight(), 0);
        valueShowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                recyclerView.getLayoutParams().height = height;
                recyclerView.requestLayout();
            }
        });
        valueDismissAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                recyclerView.getLayoutParams().height = height;
                recyclerView.requestLayout();
            }
        });
    }

    public void show() {
        popupWindow.showAtLocation(parentsView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0);
        valueShowAnimator.setDuration(animatorDuration);
        valueShowAnimator.start();
    }

    public void dismiss() {
        valueDismissAnimator.setDuration(animatorDuration);
        valueDismissAnimator.start();
        //popupWindow.dismiss();
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

}
