package com.example.foodagramapp.foodagram.Profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.example.foodagramapp.foodagram.Profile.Fragment_editProfile;
import com.example.foodagramapp.foodagram.Profile.Adapter_profile;
import com.example.foodagramapp.foodagram.Profile.Model_profile;

import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_profile extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myPostRef, myProfileRef;
    private Adapter_profile profileAdapter;
    private TextView profileNameTextView, usernameTextView, descriptionTextView;
    private ImageView mockAnotherProfile,profileImage;
    private String menuName, foodDescription, owner, location, price, time;
    private String name, username, profileDescription;
    private Button editProfileBtn;
    private Model_profile exampleInfo;
    private ArrayList<Model_profile> profileInfo = new ArrayList<Model_profile>();
//    Model_profile exampleInfo = new Model_profile("Pizza", "SkyDogg" , "50", "IT KMITL");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        profileInfo.add(exampleInfo);
        initPostRef();
        initProfileRef();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIComponent();


        //** Setup list , listAdapter
        final ListView profilePostList = getView().findViewById(R.id.profile_all_post_list);
        profileAdapter = new Adapter_profile(
                getActivity(),
                R.layout.fragment_profile_item,
                profileInfo
        );
        profilePostList.setAdapter(profileAdapter);
        profileAdapter.notifyDataSetChanged();
        //**

    }

    public void initUIComponent(){
        editProfileBtn =  getView().findViewById(R.id.profile_edit_profile);
        profileImage = getView().findViewById(R.id.profile_image);
        profileNameTextView = getView().findViewById(R.id.profile_name);
        usernameTextView = getView().findViewById(R.id.profile_username);
        descriptionTextView = getView().findViewById(R.id.profile_description);
//        mockAnotherProfile = getView().findViewById(R.id.mock_anohter_profile);

        //        mockAnotherProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_editProfile())
                        .commit();
            }
        });

    }
    public void initPostRef(){
        try {
            myPostRef = database.getReference().child("post");
            myPostRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot db: dataSnapshot.getChildren()){
                        Log.i("DBprofile", "" + db.child("owner").getValue());
                        //** Check data from person's profile
                        if(db.child("owner").getValue().equals("test_user_id_1")) {
                            // ** Get profile's post


                            menuName = (String) db.child("menu_name").getValue();
                            foodDescription = (String) db.child("description").getValue();
                            owner = (String) db.child("owner").getValue();
                            location = (String) db.child("location").getValue();
                            price = (String) db.child("menu_price").getValue().toString();

                            exampleInfo = new Model_profile(menuName, owner, price, location);
                            profileInfo.add(exampleInfo);
                            profileAdapter.notifyDataSetChanged();

//

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initProfileRef(){
        try {
            myProfileRef = database.getReference().child("profile");
            myProfileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot db: dataSnapshot.getChildren()){
                        if(db.child("username").getValue().equals("SkyDogg")){
                            name = (String) db.child("name").getValue();
                            username = (String) db.child("username").getValue();
                            profileDescription = (String) db.child("vitae").getValue();

                            profileNameTextView.setText(name);
                            usernameTextView.setText(username);
                            descriptionTextView.setText(profileDescription);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}