package com.example.foodagramapp.foodagram.Profile;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.AuthenticationActivity;
import com.example.foodagramapp.foodagram.LoginFragment;
import com.example.foodagramapp.foodagram.MainActivity;
import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.example.foodagramapp.foodagram.Post.ImageSelectorFragment;
import com.example.foodagramapp.foodagram.Profile.Fragment_profile;

import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Utils.Extension;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.foodagramapp.foodagram.Post.ImageSelectorFragment.getBitmap;


public class Fragment_editProfile extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference myRef;
    String monthStr;
    String dayStr;
    EditText nameField;
    EditText usernameField;
    EditText descriptionField;

    EditText sexField;
    EditText birthDateField;
    TextView emailField;
    TextView headerUsernameField;
    int mYear,mMonth,mDay;
    private String[] sex;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button logoutBtn;

    private Bitmap bitmap = getBitmap();
    private CircleImageView profile_image;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private StorageReference storageRef = storage.getReference();
    private String downloadImageURL;
    private CustomLoadingDialog customLoadingDialog;
    private String TAG = "ProfileEditFragment";
    private boolean checkUploadImg = false;

    // Write a message to the database







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sex = new String[]{"ชาย", "หญิง"};
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        customLoadingDialog = new CustomLoadingDialog(this.getContext());
        initUIComponent();
        initSexField();
        initBirthDateField();
        initSaveBtn();
        initBackBtn();
        initLogoutBtn();
        initChangeProfileImgButton();

        try {
            myRef = database.getReference().child("profile");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.cl    ass);
                    for(DataSnapshot db : dataSnapshot.getChildren()){

                        //** Test mock username
                        if(db.getKey().equals(mUser.getUid())) {
                            DataSnapshot dbProfile = db.child("username");

                            String name = (String) db.child("name").getValue();
                            String username = (String) db.child("username").getValue();
                            String description  = (String) db.child("vitae").getValue();
                            String email = (String) db.child("email").getValue();
                            String sex = (String) db.child("sex").getValue();
                            String birthDate = (String) db.child("dob").getValue();
                            String profileImageUrl = (String) db.child("profile_img_url").getValue();
//                        writeNewUser(name, username, description, email, sex, birthDate, db);
                            nameField.setText(name);
                            usernameField.setText(username);
                            descriptionField.setText(description);

                            sexField.setText(sex);
                            birthDateField.setText(birthDate);
                            headerUsernameField.setText(username);
                            emailField.setText(email);
                            if (bitmap != null){
                                checkUploadImg = true;
                                profile_image.setImageBitmap(bitmap);
                            } else {
                                Picasso.get().load(profileImageUrl ).into(profile_image);
                            }

                        }
                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }




    }
//    private void writeNewUser(String name, String userId, String description, String email, String sex, String birthDate, DataSnapshot db) {
//        Model_editProfile editedProfile = new Model_editProfile(name, userId, description, email, sex, birthDate);
//        Log.i("Write", "Write success" );
//
//
//
//    }

    public void initUIComponent(){
        nameField = getView().findViewById(R.id.edit_profile_name);
        usernameField = getView().findViewById(R.id.edit_profile_username);
        descriptionField = getView().findViewById(R.id.edit_profile_description);
        emailField = getView().findViewById(R.id.edit_profile_email);
        headerUsernameField = getView().findViewById(R.id.edit_profile_header_username);
        profile_image = getView().findViewById(R.id.profile_image);

    }

    void initChangeProfileImgButton(){
        TextView _change_profile_img = getView().findViewById(R.id.edit_profile_change_profile_img_btn);
        _change_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ImageSelectorFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void initSexField(){
        sexField = (EditText) getView().findViewById(R.id.edit_profile_sex);
        sexField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSexpickerDialog();

            }
        });


        sexField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    initSexpickerDialog();

                }
            }
        });

    }
    public void initBirthDateField(){
        birthDateField = (EditText) getView().findViewById(R.id.edit_profile_birth_date);
        birthDateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                datePickerPopup(birthDateField);
                birthDateField.setShowSoftInputOnFocus(false);
            }
        });
        birthDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    datePickerPopup(birthDateField);
                    birthDateField.setShowSoftInputOnFocus(false);
                }
            }
        });


    }

    public void initLogoutBtn(){
        logoutBtn = getView().findViewById(R.id.edit_profile_logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent mIntent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivity(mIntent);
            }
        });

    }
    public void initSexpickerDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());
        builder.setTitle("เลือกเพศของคุณ");
        builder.setItems(sex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = sex[which];
                sexField.setText(selected);
            }
        });

        builder.create();

// สุดท้ายอย่าลืม show() ด้วย
        builder.show();
        sexField.setShowSoftInputOnFocus(false);

    }
    public void initSaveBtn(){
        TextView saveBtn = (TextView) getView().findViewById(R.id.edit_profile_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //** Test save value mock
                try {
                    customLoadingDialog.showDialog();

                    myRef.child(mUser.getUid()).child("username").setValue(usernameField.getText().toString());
                    myRef.child(mUser.getUid()).child("name").setValue(nameField.getText().toString());
                    myRef.child(mUser.getUid()).child("vitae").setValue(descriptionField.getText().toString());
                    myRef.child(mUser.getUid()).child("sex").setValue(sexField.getText().toString());
                    myRef.child(mUser.getUid()).child("dob").setValue(birthDateField.getText().toString());
                    myRef.child(mUser.getUid()).child("email").setValue(emailField.getText().toString());


                    if (checkUploadImg) {


                        // Generate reference message for uploading image
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String destination_directory = "profile_images";
                        String refMessage = destination_directory + "/" + UUID.randomUUID().toString() + timestamp.toString() + ".jpg";

                        // Compress image
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
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
                                    myRef.child(mUser.getUid()).child("profile_img_url").setValue(downloadImageURL);
                                    customLoadingDialog.dismissDialog();
                                    Log.d(TAG, "UPLOADED AN IMAGE");
                                } else {
                                    customLoadingDialog.dismissDialog();
                                    Extension.toast(getActivity(), "เกิดข้อผิดพลาดในการอัปโหลดรูป");
                                    Log.d(TAG, "FAILED TO UPLOAD AN IMAGE");
                                }
                            }
                        });
                    } else {
                        customLoadingDialog.dismissDialog();
                    }


                } catch (Exception e){
                    e.printStackTrace();
                }


                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_profile())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    public void initBackBtn(){
        TextView backBtn = (TextView) getView().findViewById(R.id.edit_profile_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_profile())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }
    public void datePickerPopup(final EditText field){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                if(month < 10){
                    monthStr = "0" + (month + 1);
                }else{
                    monthStr = (month + 1) + "";
                }

                if(date < 10){
                    dayStr = "0" + date;

                }else{
                    dayStr = date  + "";
                }

                mYear = year;
                mMonth = month;
                mDay = date;
                field.setText(year+"-"+monthStr+"-"+dayStr);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(0);


        datePickerDialog.show();
    }

}