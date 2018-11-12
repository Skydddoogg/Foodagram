package com.example.foodagramapp.foodagram.Post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Utils.FilePaths;
import com.example.foodagramapp.foodagram.Utils.FileSearch;
import com.example.foodagramapp.foodagram.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class ImageSelectorFragment extends Fragment {

    private static final String TAG = "ImageSelectorFragment";
    private static final int NUM_GRID_COLUMNS = 3;

    private GridView gridView;
    private TextView backBtn;
    private TextView nextScreen;
    private ImageView galleryImage;
    private SlidingUpPanelLayout slidingLayout;
    private ProgressBar mProgressBar;
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        View view = inflater.inflate(R.layout.fragment_image_selector, container, false);
        galleryImage = view.findViewById(R.id.gallery_image_view);
        gridView = view.findViewById(R.id.gallery_grid_view);
        mProgressBar = view.findViewById(R.id.gallery_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        directories = new ArrayList<>();
        backBtn = view.findViewById(R.id.gallery_back_btn);
        slidingLayout = view.findViewById(R.id.sliding_layout);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing the ImageSelectorFragment.");
            }
        });
        nextScreen = view.findViewById(R.id.gallery_top_bar_next);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the AddPostActivity.");
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                startActivity(intent);

            }
        });
        init();
        return view;
    }

    private void init(){
        FilePaths filePaths = new FilePaths();

        //check for other folders inside "/storage/emulated/0/pictures"
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }
        directories.add(filePaths.CAMERA);
        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            Log.d(TAG, "init: directory: " + directories.get(i));
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index);
            directoryNames.add(string);
        }
        setupGridView(filePaths.CAMERA);
    }


    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //use the grid adapter to adapter the images to gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_image_view, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        //set the first image to be displayed when the activity fragment view is inflated
        try {
            setImage(imgURLs.get(0), galleryImage, mAppend);
            mSelectedImage = imgURLs.get(0);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "setupGridView: IndexOutOfBoundsException: " +e.getMessage() );
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));
                setImage(imgURLs.get(position), galleryImage, mAppend);
                mSelectedImage = imgURLs.get(position);
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }


    private void setImage(String imgURL, ImageView image, String append){
        Log.d(TAG, "setImage: setting image");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}































