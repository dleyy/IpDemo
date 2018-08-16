package com.example.lilei.iptest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lilei.iptest.adapter.ImageListAdapter;
import com.example.lilei.iptest.config.ImagePickerConfig;
import com.example.lilei.iptest.interfaces.OnFolderClickedListener;
import com.example.lilei.iptest.interfaces.OnImageClickedListener;
import com.example.lilei.iptest.model.Folder;
import com.example.lilei.iptest.model.Image;
import com.example.lilei.iptest.utils.Utils;
import com.example.lilei.iptest.widget.DividerGridItemDecoration;
import com.example.lilei.iptest.widget.GridSpacingItemDecoration;
import com.example.lilei.iptest.widget.ImagePathPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private LinearLayout bottomLine;

    private final int LOADER_ALL = 0;
    private final int LOADER_CATEGORY = 1;
    private boolean hasFolderGened = false;

    private List<Image> imageList = new ArrayList<>();
    private List<Folder> folderList = new ArrayList<>();

    private ImageListAdapter imageListAdapter;
    private ImagePathPopupWindow imagePathPopupWindow;
    private ImagePickerConfig config;

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallBack;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_image_list, container, false);
        recyclerView = view.findViewById(R.id.image_recycle);
        textView = view.findViewById(R.id.select_image_resource);
        bottomLine = view.findViewById(R.id.bottom_lin);
        config = Constant.config;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageListAdapter = new ImageListAdapter(getActivity(), imageList);
        imageListAdapter.setOnImageClickedListener(new OnImageClickedListener() {
            @Override
            public void onImageClicked(int position, Image image) {
                ((OnImageClickedListener) getActivity()).onImageClicked(position,
                        image);
            }

            @Override
            public void onImageChecked(int position) {
                if (Constant.selectedImg.size() == config.maxNum) {
                    Toast.makeText(getActivity(), "最多可选" + config.maxNum + "张",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Image im = imageListAdapter.getAllData().get(position);
                im.isChecked = !im.isChecked;
                imageListAdapter.notifyItemChanged(position, 0);
                if (im.isChecked) {
                    Constant.selectedImg.add(im);
                } else {
                    Constant.selectedImg.remove(im);
                }
                ((OnImageClickedListener) getActivity()).onImageChecked(position);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(recyclerView.getContext()));
        recyclerView.setAdapter(imageListAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mLoaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {

            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID};

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == LOADER_ALL) {
                    CursorLoader cursorLoader = new CursorLoader(getActivity(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            null, null, IMAGE_PROJECTION[2] + " DESC");
                    return cursorLoader;
                } else if (id == LOADER_CATEGORY) {
                    CursorLoader cursorLoader = new CursorLoader(getActivity(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
                            null, IMAGE_PROJECTION[2] + " DESC");
                    return cursorLoader;
                }
                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    int count = data.getCount();
                    if (count > 0) {
                        List<Image> tempImageList = new ArrayList<>();
                        data.moveToFirst();
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            Image image = new Image(path, name, dateTime);

                            //先添加到图片列表里
                            if (!image.path.endsWith("gif"))
                                tempImageList.add(image);
                            if (!hasFolderGened) {
                                File imageFile = new File(path);
                                File folderFile = imageFile.getParentFile();
                                if (folderFile == null) {
                                    System.out.println(path);
                                    return;
                                }
                                Folder folder = new Folder();
                                folder.name = folderFile.getName();
                                folder.path = folderFile.getAbsolutePath();
                                folder.cover = image;
                                if (!folderList.contains(folder)) {
                                    List<Image> imageList = new ArrayList<>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    folderList.add(folder);
                                } else {
                                    Folder f = folderList.get(folderList.indexOf(folder));
                                    f.images.add(image);
                                }
                            }

                        } while (data.moveToNext());
                        imageList.clear();
                        List<Folder> allFolderList = new ArrayList<>();
                        Folder allFolder = new Folder();
                        allFolder.images = new ArrayList<>();
                        allFolder.images.addAll(tempImageList);
                        allFolder.path = "-1";
                        allFolder.name = "所有图片";
                        allFolderList.add(allFolder);
                        allFolderList.addAll(folderList);
                        if (Constant.selectedImg.size() > 0) {
                            for (Image image : Constant.selectedImg) {
                                if (tempImageList.contains(image)) {
                                    tempImageList.get(tempImageList.indexOf(image)).isChecked
                                            = true;
                                }
                            }
                        }

                        if (config.needCamera)
                            imageList.add(new Image());
                        imageList.addAll(tempImageList);
                        imageListAdapter.notifyDataSetChanged();

                        imagePathPopupWindow = new ImagePathPopupWindow(getActivity(), bottomLine,
                                Utils.getPhoneHeight(getActivity()), config, new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {

                            }
                        }, allFolderList, recyclerView);
                        imagePathPopupWindow.setOnItemClickListener(new OnFolderClickedListener() {
                            @Override
                            public void onFolderClick(int position, List<Image> images) {
                                if (position == 0) {
                                    imagePathPopupWindow.dismiss();
                                    getActivity().getSupportLoaderManager().initLoader(LOADER_ALL,
                                            null, mLoaderCallBack);
                                } else {
                                    imageList.clear();
                                    imageList.addAll(images);
                                    imageListAdapter.notifyDataSetChanged();
                                }
                                imagePathPopupWindow.dismiss();
                            }
                        });
                        hasFolderGened = true;
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };

        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallBack);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePathPopupWindow != null) {
                    if (!imagePathPopupWindow.isShowing()) {
                        imagePathPopupWindow.show();
                    } else {
                        imagePathPopupWindow.dismiss();
                    }
                }
            }
        });
    }

    public boolean onBackPressed() {
        if (imagePathPopupWindow.isShowing()) {
            imagePathPopupWindow.dismiss();
            return true;
        }
        return false;
    }

}
