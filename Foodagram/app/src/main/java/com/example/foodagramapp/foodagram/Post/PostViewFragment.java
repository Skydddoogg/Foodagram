package com.example.foodagramapp.foodagram.Post;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostViewFragment extends Fragment{

    private String postId;
    private String TAG = "PostViewFragment";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView _username, _time, _content, _like, _comment;
    private ImageView _foodImageView, _profileImageView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBackButton();
        postId = "05e6b355-4eab-426e-b4c7-5a2d326fe1fa";
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("post/" + postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Log.d(TAG, "POST OWNER: " + post.getOwner());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_view, container, false);
    }

    void initBackButton(){
        TextView _backBtn = getView().findViewById(R.id.post_view_back_btn);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                Log.d(TAG, "BACK TO PREVIOUS PAGE");
            }
        });
    }

//    void initViews(){
//        _username = getView().findViewById()
//    }
}
