package com.example.foodagramapp.foodagram.Profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Profile.Fragment_editProfile;
import com.example.foodagramapp.foodagram.Profile.Adapter_profile;
import com.example.foodagramapp.foodagram.Profile.Model_profile;

import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class Fragment_profile extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myPostRef, myProfileRef, profileNameRef;
    private Adapter_profile profileAdapter;
    private TextView profileNameTextView, usernameTextView, descriptionTextView;
    private ImageView mockAnotherProfile,profileImage;
    private String menuName, foodDescription, owner, location, price, time, menuImage;
    private String name, username, profileDescription;
    private Button editProfileBtn;
    private Model_profile exampleInfo;
    private ArrayList<Model_profile> profileInfo = new ArrayList<Model_profile>();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ConstraintLayout followBlock;
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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        initProfileRef();
        initPostRef();



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIComponent();

        Bundle bundle = getArguments();
        if (bundle != null){
            ProfileForFeed profile = bundle.getParcelable("profile");

        }


        //** Setup list , listAdapter
        final ListView profilePostList = getView().findViewById(R.id.profile_all_post_list);
        profileAdapter = new Adapter_profile(
                getActivity(),
                R.layout.fragment_profile_item,
                profileInfo
        );
        profilePostList.setAdapter(profileAdapter);
        profilePostList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean listIsAtTop()   {
                if(profilePostList.getChildCount() == 0) return true;
                return profilePostList.getChildAt(0).getTop() == 0;
            }
            int mLastFirstVisibleItem = 0;
            private LinearLayout lBelow;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                final int currentFirstVisibleItem = profilePostList.getFirstVisiblePosition();


                if (currentFirstVisibleItem > mLastFirstVisibleItem ) {

                        animateHideProfileHeader();

                } else if (firstVisibleItem == 0 && listIsAtTop()) {

                        animateShowProfileHeader();

                }
                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }



        });

        profileAdapter.notifyDataSetChanged();
        //**

    }

    public void initUIComponent(){
        editProfileBtn =  getView().findViewById(R.id.profile_edit_profile);
        profileImage = getView().findViewById(R.id.profile_image);
        profileNameTextView = getView().findViewById(R.id.profile_name);
        usernameTextView = getView().findViewById(R.id.profile_username);
        descriptionTextView = getView().findViewById(R.id.profile_description);
        followBlock = getView().findViewById(R.id.profile_follow_block);
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
                    for(final DataSnapshot db: dataSnapshot.getChildren()){
                        //** Check data from person's profile

                        if(db.child("owner").getValue().equals(mUser.getUid())) {
                            myProfileRef = database.getReference().child("profile").child(mUser.getUid());
                            myProfileRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    owner = (String) dataSnapshot.child("username").getValue();
                                    menuName = (String) db.child("menuName").getValue();
                                    foodDescription = (String) db.child("description").getValue();
                                    location = (String) db.child("address").getValue();
                                    price = (String) db.child("menuPrice").getValue().toString();
                                    menuImage = (String) db.child("menuImageURL").getValue();
                                    exampleInfo = new Model_profile(menuName, owner, price, location, foodDescription, menuImage);
                                    profileInfo.add(exampleInfo);
                                    profileAdapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            // ** Get profile's post



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
                        if(db.getKey().equals(mUser.getUid())){
                            username = (String) db.child("username").getValue();
                            name = (String) db.child("name").getValue();
                            profileDescription = (String) db.child("vitae").getValue();

                            profileNameTextView.setText(username);
                            usernameTextView.setText(name);
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

    public void animateHideProfileHeader(){
        followBlock.animate()
                .translationY(followBlock.getHeight()/2)
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        followBlock.setVisibility(View.GONE);
                    }
                });
        descriptionTextView.animate()
                .translationY(descriptionTextView.getHeight()/2)
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        descriptionTextView.setVisibility(View.GONE);
                    }
                });
        usernameTextView.animate()
                .translationY(usernameTextView.getHeight()/2)
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        usernameTextView.setVisibility(View.GONE);
                    }
                });
        profileNameTextView.animate()
                .translationY(profileNameTextView.getHeight()/2)
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        profileNameTextView.setVisibility(View.GONE);
                    }
                });
        profileImage.animate()
                .translationY(profileImage.getHeight()/2)
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        profileImage.setVisibility(View.GONE);
                    }
                });
    }

    public void animateShowProfileHeader(){

        followBlock.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        followBlock.setVisibility(View.VISIBLE);
                    }
                });
        descriptionTextView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        descriptionTextView.setVisibility(View.VISIBLE);
                    }
                });
        usernameTextView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        usernameTextView.setVisibility(View.VISIBLE);
                    }
                });
        profileNameTextView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        profileNameTextView.setVisibility(View.VISIBLE);
                    }
                });
        profileImage.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        profileImage.setVisibility(View.VISIBLE);
                    }
                });

    }


}