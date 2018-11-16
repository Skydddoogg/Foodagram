package com.example.foodagramapp.foodagram.Post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import com.example.foodagramapp.foodagram.Utils.Extension;
import com.example.foodagramapp.foodagram.Utils.FilePaths;
import com.example.foodagramapp.foodagram.Utils.FileSearch;
import com.example.foodagramapp.foodagram.Utils.GridImageAdapter;
import com.fenchtose.nocropper.CropperCallback;
import com.fenchtose.nocropper.CropperView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImageSelectorFragment extends Fragment {

    private static final String TAG = "ImageSelectorFragment";
    private static final int NUM_GRID_COLUMNS = 3;

    CropperView cropperView;
    Bitmap previewBmap;

    CropperCallback callback = new CropperCallback() {
        @Override
        public void onCropped(Bitmap bitmap) {
            bmap = bitmap;
        }
    };

    private int angle = 0;
    static Bitmap bmap;
    private GridView gridView;
    private TextView backBtn;
    private TextView nextScreen;
    private TextView imageNotFound;
    private ImageView btnSnap;
    private ImageView btnRotate;
    private SlidingUpPanelLayout slidingLayout;
    private ProgressBar mProgressBar;
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private boolean isSnappedToCenter = false;
    private String selectedFileName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        View view = inflater.inflate(R.layout.fragment_image_selector, container, false);
        initView(view);
        initGridView();
        return view;
    }

    private void initGridView(){
        FilePaths filePaths = new FilePaths();
        directories = new ArrayList<>();

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

    private void initView(View view) {
        // Set resources directory
        cropperView = view.findViewById(R.id.gallery_image_view_bitmap);
        cropperView.setMaxZoom(1.5f);
        cropperView.setMinZoom(0.8f);
        gridView = view.findViewById(R.id.gallery_grid_view);
        mProgressBar = view.findViewById(R.id.gallery_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        backBtn = view.findViewById(R.id.gallery_back_btn);
        slidingLayout = view.findViewById(R.id.sliding_layout);
        nextScreen = view.findViewById(R.id.gallery_next_btn);
        btnSnap = view.findViewById(R.id.snap_button);
        btnRotate = view.findViewById(R.id.rotate_button);
        imageNotFound = view.findViewById(R.id.gallery_image_not_found);

        // Add listener
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing the ImageSelectorFragment.");
            }
        });

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
                Extension.toast(getActivity(), "กรุณาเลือกรูปภาพ");
            }
        });

        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSnappedToCenter) {
                    cropperView.cropToCenter();
                } else {
                    cropperView.fitToCenter();
                }
                isSnappedToCenter = !isSnappedToCenter;
            }
        });

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angle >= 270) {
                    angle %= 270;
                } else {
                    angle += 90;
                }
                Log.d(TAG, "Image angle: " + String.valueOf(angle));
                cropperView.setImageBitmap(rotateBitmap(previewBmap, angle));
            }
        });

    }

    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);
        Collections.sort(imgURLs, Collections.reverseOrder());

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //use the grid adapter to adapter the images to gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_image_view, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        //set the first image to be displayed when the activity fragment view is inflated
        try {
            setImage(imgURLs.get(0), mAppend);
        } catch (IndexOutOfBoundsException e) {
            imageNotFound.setVisibility(View.VISIBLE);
            btnRotate.setVisibility(View.INVISIBLE);
            btnSnap.setVisibility(View.INVISIBLE);
            nextScreen.setVisibility(View.INVISIBLE);
            Log.e(TAG, "setupGridView: IndexOutOfBoundsException: " +e.getMessage() );
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));
                setImage(imgURLs.get(position), mAppend);
                File f = new File(imgURLs.get(position));
                selectedFileName = f.getName();
                Log.d(TAG, "Selected image file name: " + selectedFileName);
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    private void setImage(String imgURL, String append){
        Log.d(TAG, "setImage: setting image");
        Log.d(TAG, "URL: " + imgURL);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.loadImage(append + imgURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                previewBmap = loadedImage;
                cropperView.setImageBitmap(previewBmap);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void cropImage() {
        try {
            cropperView.getCroppedBitmapAsync(callback);
            Log.d(TAG, String.valueOf(callback.equals(null)));
            if (bmap != null) {
                Bundle bundle = new Bundle();
                bundle.putString("name", selectedFileName);
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } catch (NullPointerException e) {
            Extension.toast(getActivity(), "Error cropping");
        }
    }

    public static Bitmap getBitmap() {
        return bmap;
    }

    private Bitmap rotateBitmap(Bitmap mBitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}
