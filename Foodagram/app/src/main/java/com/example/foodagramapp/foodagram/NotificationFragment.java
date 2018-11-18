package com.example.foodagramapp.foodagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.foodagramapp.foodagram.Comment.CommentFragment;

import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.example.foodagramapp.foodagram.Notification.Notification;
import com.example.foodagramapp.foodagram.Notification.NotificationAdapter;
import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.Post.PostViewFragment;
import com.example.foodagramapp.foodagram.Profile.Fragment_profile;
import com.example.foodagramapp.foodagram.Profile.ProfileForFeed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotificationFragment extends Fragment {
    private CustomLoadingDialog customLoadingDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customLoadingDialog = new CustomLoadingDialog(getContext());
        customLoadingDialog.showDialog();
        getDataToListView();
        setViewedFields();
        MainActivity.bottomNavigation.setNotification("", MainActivity.notificationButtonPosition);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    void getDataToListView() {
        try {
            final ArrayList<Notification> notifications = new ArrayList<Notification>();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final Query myRef = database.getReference("/notification/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).limitToLast(10);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notifications.clear();
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
                        DatabaseReference myRef2 = database.getReference("/profile/" + child.getValue(Notification.class).getFrom());
                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    ListView notificationList = getView().findViewById(R.id.notification_list);
                                    NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity(), R.layout.fragment_notification_item, notifications);
                                    notificationList.setAdapter(notificationAdapter);
                                    final String uid = child.getValue(Notification.class).getFrom();

                                    notifications.add(new Notification(
                                            child.getValue(Notification.class).getPostId(),
                                            dataSnapshot.child("username").getValue(String.class),
                                            child.getValue(Notification.class).getType(),
                                            child.getValue(Notification.class).getContent(),
                                            child.getValue(Notification.class).getTimestamp(),
                                            child.getValue(Notification.class).getViewed(),
                                            dataSnapshot.child("profile_img_url").getValue(String.class),
                                            child.getValue(Notification.class).getFrom()
                                    ));

                                    customLoadingDialog.dismissDialog();
                                    Collections.sort(notifications, new Comparator<Notification>() {
                                        @Override
                                        public int compare(Notification notification, Notification t1) {
                                            if(notification.getTimestamp() > t1.getTimestamp()){
                                                return -1;
                                            }
                                            if(notification.getTimestamp() < t1.getTimestamp()){
                                                return 1;
                                            }
                                            return 0;
                                        }
                                    });
                                    notificationList.setOnItemClickListener(
                                            new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                                    //Initialziation Class for pass bundle to fragments
                                                    final FirebaseDatabase db = FirebaseDatabase.getInstance();
                                                    DatabaseReference myRefItem = db.getReference("post/" + notifications.get(i).getPostId());
                                                    myRefItem.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Post postOnClick = dataSnapshot.getValue(Post.class);
                                                            Bundle bundleForPostId = new Bundle();

                                                            switch (notifications.get(i).getType()) {
                                                                case "follow":
                                                                    DatabaseReference myRefItem2 = db.getReference("profile/" + uid);
                                                                    myRefItem2.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            customLoadingDialog.dismissDialog();
                                                                            ProfileForFeed profile = dataSnapshot.getValue(ProfileForFeed.class);
                                                                            Bundle bundle = new Bundle();
                                                                            bundle.putParcelable("profile", profile);
                                                                            Fragment_profile fragment_profile = new Fragment_profile();
                                                                            fragment_profile.setArguments(bundle);
                                                                            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_view, fragment_profile).commit();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                            customLoadingDialog.dismissDialog();

                                                                        }
                                                                    });
                                                                    break;
                                                                case "comment":
                                                                    bundleForPostId.putString("postId", postOnClick.getPostId());
                                                                    bundleForPostId.putString("postOwner", postOnClick.getOwner());
                                                                    CommentFragment frag = new CommentFragment();
                                                                    frag.setArguments(bundleForPostId);
                                                                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_view, frag).commit();
                                                                    break;
                                                                case "like":
                                                                    bundleForPostId.putParcelable("post", postOnClick);
                                                                    PostViewFragment post = new PostViewFragment();
                                                                    post.setArguments(bundleForPostId);
                                                                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_view, post).commit();
                                                                    break;
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                            customLoadingDialog.dismissDialog();
                                                        }
                                                    });

                                                }
                                            }
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                customLoadingDialog.dismissDialog();
                            }
                        });
                    }
                    Log.i("NOTI", "FIREBASE RETRIVED");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    customLoadingDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setViewedFields(){
        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = rootRef.child("notification/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    customLoadingDialog.dismissDialog();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.child("viewed").getRef().setValue(new Double(1));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    customLoadingDialog.dismissDialog();
                }
            };
            usersRef.addListenerForSingleValueEvent(eventListener);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
