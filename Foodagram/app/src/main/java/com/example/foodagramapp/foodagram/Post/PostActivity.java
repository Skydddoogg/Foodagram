package com.example.foodagramapp.foodagram.Post;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Utils.Permissions;
import com.example.foodagramapp.foodagram.Utils.SectionsPagerAdapter;

public class PostActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "PostActivity";
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private String page;
    static ViewPager mViewPager;
    static RadioGroup radioGroup;
    static RadioButton radioCamera;
    static RadioButton radioGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Log.d(TAG, "onCreate: started.");
        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    /**
     * setup viewpager for manager the tabs
     */
    private void setupViewPager() {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new ImageSelectorFragment());

        radioGroup = findViewById(R.id.radio_group_post);
        radioGallery = findViewById(R.id.radio_group_post_gallery);
        radioCamera = findViewById(R.id.radio_group_post_camera);

        mViewPager = findViewById(R.id.viewpager_container);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        radioGroup.check(radioGroup.getChildAt(0).getId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                page = (String) ((RadioButton) group.findViewById(i)).getText();
                if (page.equals("กล้อง")) {
                    mViewPager.setCurrentItem(0);
                    radioGroup.check(radioGroup.getChildAt(0).getId());
                    radioCamera.setTextColor(getResources().getColor(R.color.white));
                    radioGallery.setTextColor(Color.parseColor("#5A6B84"));
                    Log.d(TAG, "GO TO CAMERA");
                } else {
                    mViewPager.setCurrentItem(1);
                    radioGroup.check(radioGroup.getChildAt(1).getId());
                    radioGallery.setTextColor(getResources().getColor(R.color.white));
                    radioCamera.setTextColor(Color.parseColor("#5A6B84"));
                    Log.d(TAG, "GO TO GALLERY");
                }
            }
        });
    }


    public int getTask() {
        Log.d(TAG, "getTask: TASK: " + getIntent().getFlags());
        return getIntent().getFlags();
    }

    /**
     * verifiy all the permissions passed to the array
     * @param permissions
     */
    public void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions.");
        ActivityCompat.requestPermissions(
                PostActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");
        for (int i = 0; i< permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission is it has been verified
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking permission: " + permission);
        int permissionRequest = ActivityCompat.checkSelfPermission(PostActivity.this, permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

}
