package com.example.foodagramapp.foodagram.Post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.foodagramapp.foodagram.Post.ImageSelectorFragment.bmap;
import static com.example.foodagramapp.foodagram.Post.ImageSelectorFragment.getBitmap;

public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "AddPostActivity";
    private final int PLACE_PICKER_REQUEST = 1;

    Bitmap bitmap = getBitmap();
    private ImageView image;
    private TextView backBtn;
    private TextView postTextLocation;
    private EditText postDescription;
    private EditText postMenu;
    private EditText postPrice;
    private Button postShareButton;
    private ConstraintLayout postLocationBar;
    private ArrayList<String> locationList = new ArrayList<>();
//    private String mAppend = "file:/";
//    private Intent intent;
//    private String imgUrl;
//    private UploadTask uploadImageTask;
//    private String imageRef;
//    private String selectedFileName;
    private String downloadImageURL;
    private Post post;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private CustomLoadingDialog customLoadingDialog;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        customLoadingDialog = new CustomLoadingDialog(this);

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
                if (!isValidPost()) {
                    Extension.toast(AddPostActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน");
                } else {
                    try {
                        customLoadingDialog.showDialog();

                        // Generate reference message for uploading image
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String destination_directory = "post_images";
                        String refMessage = destination_directory + "/" + UUID.randomUUID().toString() + timestamp.toString() + ".jpg";

                        // Compress image
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        // Upload to firebase storage
                        final StorageReference ref = storageRef.child(refMessage);
                        UploadTask uploadTask = ref.putBytes(data);
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUrl = task.getResult();
                                    downloadImageURL = downloadUrl.toString(); // Image URL
                                    addPostToDB(downloadImageURL);
                                    customLoadingDialog.dismissDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("post", post);
                                    PostViewFragment frag = new PostViewFragment();
                                    frag.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.main_view, frag).commit();
                                    Log.d(TAG, "GO TO POST VIEW");
                                } else {
                                    customLoadingDialog.dismissDialog();
                                    Extension.toast(AddPostActivity.this, "Failed to upload an image");
                                    Log.d(TAG, "FAILED TO UPLOAD AN IMAGE");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }});

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

    private boolean isValidPost() {
        boolean condition1 = !postDescription.getText().toString().equals("");
        boolean condition2 = !postMenu.getText().toString().equals("");
        boolean condition3 = !postPrice.getText().toString().equals("");
        boolean condition4 = !postTextLocation.getText().toString().equals("");

        Log.d(TAG, "1: " + String.valueOf(condition1));
        Log.d(TAG, "2: " + String.valueOf(condition2));
        Log.d(TAG, "3: " + String.valueOf(condition3));
        Log.d(TAG, "4: " + String.valueOf(condition4));

        if (condition1 && condition2 && condition3 && condition4) {
            return true;
        } else {
            return false;
        }
    }

    private void setImage() {
        image = findViewById(R.id.post_image_view);
        image.setImageBitmap(bitmap);
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
                String placeName = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                locationList.add(0, placeName);
                locationList.add(1, latitude);
                locationList.add(2, longitude);
                locationList.add(3, address);
                postTextLocation.setText(placeName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        bmap = null;
        super.onBackPressed();
    }

    private void addPostToDB(String downloadImageURL) {
        Log.d(TAG, "here");
        mAuth = FirebaseAuth.getInstance();
        String postId = String.valueOf(UUID.randomUUID());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        post = new Post();
        post.setDescription(postDescription.getText().toString());
        post.setPlaceName(locationList.get(0));
        post.setLatitude(locationList.get(1));
        post.setLongitude(locationList.get(2));
        post.setAddress(locationList.get(3));
        post.setMenuImageURL(downloadImageURL);
        post.setMenuName(postMenu.getText().toString());
        post.setMenuPrice(Double.parseDouble(postPrice.getText().toString()));
        post.setTimestamp(System.currentTimeMillis());
        post.setOwner(mAuth.getCurrentUser().getUid());
        mDatabase.child("post").child(postId).setValue(post);
    }

}
