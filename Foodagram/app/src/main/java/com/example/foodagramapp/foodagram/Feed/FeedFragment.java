package com.example.foodagramapp.foodagram.Feed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.foodagramapp.foodagram.Post;
import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Search.SearchAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private DatabaseReference myRef,myRef2;
    private ArrayList<Post> value = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<String> followingForAUserArray = new ArrayList<>();
    private int sizeOfFollowingForAUserArray = 0;
    private int position = 0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getFollowingForAUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    private void showPost(){
        final ListView listView = (ListView) getView().findViewById(R.id.feed_listView);
                listView.setDivider(null);
                final FeedAdapter feedAdapter = new FeedAdapter(
                        getActivity(),
                        R.layout.feed_crad_view,
                        value
                );
                listView.setAdapter(feedAdapter);
    }

    private void getFollowingForAUser(){
        //Child ให้ใส่ User ที่เป็นคน Login เพื่อดู User คนนี้กดติดตามใคร
        myRef = database.getReference("followingForAUser").child("test_user_id_1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingForAUserArray.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    followingForAUserArray.add(child.getValue(String.class));
                }
                getPost();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void getPost(){
        sizeOfFollowingForAUserArray = followingForAUserArray.size();
            myRef = database.getReference("post");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(position = 0; position < sizeOfFollowingForAUserArray; position++) {
                        showPost();
                        if(position == 0){
                            value.clear();
                        }
                        for (DataSnapshot child : dataSnapshot.child(followingForAUserArray.get(position)).getChildren()) {
                            value.add(child.getValue(Post.class));
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FeedFragment",databaseError.getMessage());
                }
            });


    }






}
