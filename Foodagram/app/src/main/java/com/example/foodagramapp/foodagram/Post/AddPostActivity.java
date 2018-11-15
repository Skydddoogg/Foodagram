package com.example.foodagramapp.foodagram.Post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Utils.Extension;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.example.foodagramapp.foodagram.Post.ImageSelectorFragment.bmap;
import static com.example.foodagramapp.foodagram.Post.ImageSelectorFragment.getBitmap;

public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "AddPostActivity";
    private final int PLACE_PICKER_REQUEST = 1;

    Bitmap bitmap = getBitmap();
    private ImageView image;
    private TextView backBtn;
    private EditText postDescription;
    private EditText postMenu;
    private EditText postPrice;
    private Button postShareButton;
    private ConstraintLayout postLocationBar;
    private TextView postTextLocation;
    private GoogleApiClient mGoogleApiClient;
    private String mAppend = "file:/";
    private Intent intent;
    private String imgUrl;
    private String selectedFileName;
    private CustomLoadingDialog customLoadingDialog;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        customLoadingDialog = new CustomLoadingDialog(this);
        Bundle p = getIntent().getExtras();
        selectedFileName = p.getString("name");
        initViews();
        setImage();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        postLocationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddPostActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        postShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customLoadingDialog.showDialog();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                StorageReference ref = storageRef.child("post_images/"+selectedFileName);
                UploadTask uploadTask = ref.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customLoadingDialog.dismissDialog();
                        Extension.toast(AddPostActivity.this, "Upload failed");
                        Log.d(TAG, e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        customLoadingDialog.dismissDialog();
                        Extension.toast(AddPostActivity.this, "Upload success");
                        Log.d(TAG, taskSnapshot.getMetadata().toString());
                    }
                });

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing the activity");
                onBackPressed();
            }
        });
    }

    private void initViews() {
        postDescription = findViewById(R.id.post_description);
        postMenu = findViewById(R.id.post_menu);
        postPrice = findViewById(R.id.post_price);
        postLocationBar = findViewById(R.id.post_location_bar);
        postTextLocation = findViewById(R.id.post_text_location);
        postShareButton = findViewById(R.id.post_share_button);
        backBtn = findViewById(R.id.post_back_btn);
    }

    private void setImage(){
        ImageView image = findViewById(R.id.post_image_view);
        Log.d(TAG, Integer.toString(bitmap.getAllocationByteCount()));
        image.setImageBitmap(bitmap);

//        if (intent.hasExtra(getString(R.string.selected_image))) {
//            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
//            Log.d(TAG, "setImage: got new image url: " + imgUrl);
//            UniversalImageLoader.setImage(imgUrl, image, null, mAppend);
//        } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
////            bitmap = getIntent().getParcelableExtra(getString(R.string.selected_bitmap));
////            Log.d(TAG, "setImage: got new bitmap");
//            Log.d(TAG, String.valueOf(bitmap.equals(null)));
//            image.setImageBitmap(bitmap);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(postLocationBar, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                String placeName = String.format("%s", place.getName());
//                String latitude = String.valueOf(place.getLatLng().latitude);
//                String longitude = String.valueOf(place.getLatLng().longitude);
//                String address = String.format("%s", place.getAddress());
//                stBuilder.append("Name: ");
                stBuilder.append(placeName);
//                stBuilder.append("\n");
//                stBuilder.append("Latitude: ");
//                stBuilder.append(latitude);
//                stBuilder.append("\n");
//                stBuilder.append("Logitude: ");
//                stBuilder.append(longitude);
//                stBuilder.append("\n");
//                stBuilder.append("Address: ");
//                stBuilder.append(address);
                postTextLocation.setText(stBuilder.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        bmap = null;
        super.onBackPressed();
    }

}
