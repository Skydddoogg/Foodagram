package com.example.foodagramapp.foodagram.Profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.example.foodagramapp.foodagram.Notification.Notification;
import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.Post.PostViewFragment;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class Fragment_profile extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myPostRef, myProfileRef;
    private Adapter_profile profileAdapter;
    private TextView profileNameTextView, usernameTextView, descriptionTextView;
    private ImageView mockAnotherProfile,profileImage;
    private TextView followingTextView, followerTextView;
    private String menuName, foodDescription, owner, location, price, time, menuImage;
    private String  anotherBirthDate, anotherSex , anotherProfileImage;
    private String anotherEmail = "";
    private String anotherUserName = "";
    private String anotherName = "";
    private String anotherUserUid = "";
    private String anotherDescription = "";
    private String name, username, profileDescription;
    private Button editProfileBtn;
    private Model_profile exampleInfo;
    private ArrayList<Post> profileInfo = new ArrayList<Post>();
    private ArrayList<String> likeCountForRender = new ArrayList<String>();
    private ArrayList<String> postIdForRender = new ArrayList<String>();
    private ArrayList<String> commentCountArrayList = new ArrayList<String>();
    private ArrayList<ProfileForFeed> profileArrayList = new ArrayList<ProfileForFeed>();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ConstraintLayout followBlock;
    private Boolean isAnotherUser;
    private CustomLoadingDialog customLoadingDialog;
    private Boolean isFollowed = false;
    private ListView profilePostList;
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
//        initProfileRef();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIComponent();
        customLoadingDialog = new CustomLoadingDialog(getContext());




        Bundle bundle = getArguments();
        if (bundle != null){
            ProfileForFeed profile = bundle.getParcelable("profile");
            anotherBirthDate = profile.getDob();
            anotherUserName = profile.getUsername();
            anotherName = profile.getName();
            anotherProfileImage = profile.getProfile_img_url();
            anotherSex = profile.getSex();
            anotherEmail = profile.getEmail();
            anotherDescription = profile.getVitae();
            anotherUserUid = profile.getUserId();
            //Get user UID and compare it with CurrentUserId
            Log.d("ProfileFragment", "User ID = " + anotherUserUid);
            Log.d("ProfileFragment", "OwnerUser ID = " + mUser.getUid());
                initAnotherPostRef();
                isAnotherUser = true;
                usernameTextView.setText(anotherUserName);
                profileNameTextView.setText(anotherName);
                descriptionTextView.setText(anotherDescription);
                Picasso.get().load(anotherProfileImage).into(profileImage);
            initOwnerFollowingAmount(anotherUserUid);
            initOwnerFollowerAmount(anotherUserUid);
        if(anotherUserUid.equals(mUser.getUid())) {
            editProfileBtn.setText("แก้ไขโปรไฟล์");
            editProfileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_view, new Fragment_editProfile())
                            .commit();
                }
            });
        }else{
            checkFollowed();
            setFollowBtn();
        }


            //IF current user followed antoher user , When click on button , DELETE a row on FOLLOWING[CurrentUser -> anotherPerson]
            //AT THE SAME TIME, delete currentUser in anotherPerson FOLLOWER [anotherPerson -> CurrentUser]



            //IF current user isn't followed , when clicked, Add another user in FOLLOWING [CurrentUser -> anotherPerson]
            //AT THE SAME TIME, add currentUser in to anotherPerson FOLLOWER [anotherPerson -> CurrentUser]



        }else {

            editProfileBtn.setText("แก้ไขโปรไฟล​์");
            initProfileRef();
            initPostRef();
            initOwnerFollowingAmount(mUser.getUid());
            initOwnerFollowerAmount(mUser.getUid());

        }



        //** Setup list , listAdapter
        profilePostList = getView().findViewById(R.id.profile_all_post_list);
        profileAdapter = new Adapter_profile(
                getActivity(),
                R.layout.fragment_profile_item,
                profileInfo,
                likeCountForRender,
                commentCountArrayList,
                postIdForRender,
                profileArrayList
        );

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

        //**

    }

    public void checkFollowed(){
        Log.i("Following", "Current UID" + mUser.getUid());
        DatabaseReference refFollowing = database.getReference("/followingForAUser/" + mUser.getUid());
        refFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){
                    for(DataSnapshot db: dataSnapshot.getChildren()){
                        Log.i("Following", "Another User ID = " + anotherUserUid);
                        Log.i("Following", "Matching ID = " + db.getValue().toString());

                        if (anotherUserUid.equals(db.getValue().toString())){
                            editProfileBtn.setText("ติดตามแล้ว");
                            isFollowed = true;
                            break;
                        }
                        editProfileBtn.setText("ติดตาม");
                    }
                } else {
                    editProfileBtn.setText("ติดตาม");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setFollowBtn(){

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    DatabaseReference refUnfollower = FirebaseDatabase.getInstance().getReference("followerForAUser");
                    DatabaseReference refUnfollowing = FirebaseDatabase.getInstance().getReference("followingForAUser");

                    if (isFollowed) {

                        refUnfollower.child(anotherUserUid)
                                .orderByValue().equalTo(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    String value = snap.getValue(String.class);
                                    String key = snap.getKey();
                                    dataSnapshot.getRef().removeValue();
                                    Log.d(TAG, "KEY = " + key);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        refUnfollowing.child(mUser.getUid())
                                .orderByValue().equalTo(anotherUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    String value = snap.getValue(String.class);
                                    String key = snap.getKey();
                                    dataSnapshot.child(key).getRef().removeValue();
                                    Log.d(TAG, "KEY = " + key);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        isFollowed = false;
                        editProfileBtn.setText("ติดตาม");


                    } else {
                        try {
                            Long commentTimeStamp = System.currentTimeMillis();
                            pushNotification("-", commentTimeStamp);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        refUnfollower.child(anotherUserUid).push().setValue(mUser.getUid());
                        refUnfollowing.child(mUser.getUid()).push().setValue(anotherUserUid);
                        editProfileBtn.setText("ติดตามแล้ว");
                        isFollowed = true;

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void pushNotification(String content, Long commentTimeStamp){
        try {
            DatabaseReference myNotiRef = database.getReference("notification").child(anotherUserUid);
            Notification notification = new Notification("-", mUser.getUid() , "-", "follow", 0.0, commentTimeStamp.doubleValue());
            myNotiRef.push().setValue(notification);
            Log.d(TAG, "PUSH NOTIFICATION");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initUIComponent(){
        editProfileBtn =  getView().findViewById(R.id.profile_edit_profile);
        profileImage = getView().findViewById(R.id.profile_image);
        profileNameTextView = getView().findViewById(R.id.profile_name);
        usernameTextView = getView().findViewById(R.id.profile_username);
        descriptionTextView = getView().findViewById(R.id.profile_description);
        followBlock = getView().findViewById(R.id.profile_follow_block);
        followingTextView = getView().findViewById(R.id.profile_following_amount);
        followerTextView = getView().findViewById(R.id.profile_follower_amount);
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
            profileArrayList.clear();
            profileInfo.clear();
            postIdForRender.clear();
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
                                    profileArrayList.add(dataSnapshot.getValue(ProfileForFeed.class));
                                    profileInfo.add(db.getValue(Post.class));
                                    postIdForRender.add(db.getKey());
                                    fetchLike();
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

    public void initAnotherPostRef(){
            try {
                profileArrayList.clear();
                profileInfo.clear();
                postIdForRender.clear();
                myPostRef = database.getReference().child("post");
                myPostRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot db: dataSnapshot.getChildren()){
                            //** Check data from person's profile

                            if(db.child("owner").getValue().equals(anotherUserUid)) {
                                myProfileRef = database.getReference().child("profile").child(anotherUserUid);
                                myProfileRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        profileArrayList.add(dataSnapshot.getValue(ProfileForFeed.class));
                                        profileInfo.add(db.getValue(Post.class));
                                        postIdForRender.add(db.getKey());
                                        fetchLike();

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

    private void fetchLike() {

        try {
            Query myRef = database.getReference("like");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    likeCountForRender.clear();
                    for(String postId : postIdForRender) {
                        int likeCount = 0;
                        for (DataSnapshot user_id_like_this_post : dataSnapshot.child(postId).child("by").getChildren()) {
                            likeCount++;
                        }
                        likeCountForRender.add(likeCount + "");
                    }
                    fetchComment();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void fetchComment() {

        try {
            Query myRef = database.getReference("comment");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Parcelable state = profilePostList.onSaveInstanceState();
                    commentCountArrayList.clear();
                    for(String postId : postIdForRender) {
                        int commentCount = 0;
                        for (DataSnapshot user_id_like_this_post : dataSnapshot.child(postId).getChildren()) {
                            commentCount++;
                        }
                        commentCountArrayList.add(commentCount + "");
                    }
                    if(commentCountArrayList.size() == profileInfo.size()){
                        profilePostList.setAdapter(profileAdapter);
                        profileAdapter.notifyDataSetChanged();
                        onClickItem();
                    }
                    profilePostList.onRestoreInstanceState(state);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("FeedFragment", e.getLocalizedMessage());
        }

    }

    private void onClickItem(){
        profilePostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Send Post Obj
                PostViewFragment obj;
                FragmentManager fm;
                FragmentTransaction ft;
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", profileAdapter.getItem(position));
                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                obj = new PostViewFragment();
                obj.setArguments(bundle);
                ft.replace(R.id.main_view, obj).addToBackStack(null);
                ft.commit();
            }
        });
    }

    public void initOwnerFollowerAmount(String userId){
        final DatabaseReference profileFollower = database.getReference().child("followerForAUser").child(userId);
        profileFollower.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot db : dataSnapshot.getChildren()){
                    followerTextView.setText("" + dataSnapshot.getChildrenCount());
//                    customLoadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void initOwnerFollowingAmount(String userId){
        final DatabaseReference profileFollowing = database.getReference().child("followingForAUser").child(userId);

        profileFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot db : dataSnapshot.getChildren()){
                    followingTextView.setText("" + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                            Picasso.get().load(db.child("profile_img_url").getValue(String.class)).into(profileImage);
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