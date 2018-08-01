package com.example.lilei.iptest;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lilei.iptest.adapter.ImageListAdapter;
import com.example.lilei.iptest.interfaces.OnImageClickedListener;
import com.example.lilei.iptest.model.Folder;
import com.example.lilei.iptest.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;

    private final int LOADER_ALL = 0;
    private final int LOADER_CATEGORY = 1;
    private boolean hasFolderGened = false;


    private List<Image> imageList = new ArrayList<>();
    private List<Folder> folderList = new ArrayList<>();

    private ImageListAdapter imageListAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallBack;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_image_list, container, false);
        recyclerView = view.findViewById(R.id.image_recycle);
        textView = view.findViewById(R.id.select_image_resource);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageListAdapter = new ImageListAdapter(getActivity(), imageList);
        imageListAdapter.setOnImageClickedListener(new OnImageClickedListener() {
            @Override
            public void onImageClicked(int position) {
                Log.e("1234", "onImageClicked:" );
            }

            @Override
            public void onImageChecked(int position) {
                Log.e("1234","checked");
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(imageListAdapter);

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
//                        if (config.needCamera)
//                            imageList.add(new Image());
                        imageList.addAll(tempImageList);
                        imageListAdapter.notifyDataSetChanged();
//
//                        folderListAdapter.notifyDataSetChanged();

                        hasFolderGened = true;
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };

        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL,null,mLoaderCallBack);
    }
}
