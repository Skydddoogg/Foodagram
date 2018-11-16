package com.example.foodagramapp.foodagram.Post;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostViewFragment extends Fragment{

    private String postId;
    private String TAG = "PostViewFragment";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView _username, _time, _content, _like, _commentStatus, _commentViewBtn;
    private ImageView _menuImageView, _profileImageView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initButtons();
        initViews();

        Bundle bundle = getArguments();
        if (bundle != null){
            Post post = bundle.getParcelable("post");
            _content.setText(post.getDescription());
            _time.setText(Double.toString(post.getTimestamp()));
            _username.setText(post.getOwner());
            Picasso.get().load(post.getMenuImageURL()).into(_menuImageView);
            Log.d(TAG, "POST OWNER: " + post.getOwner());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_view, container, false);
    }

    void initButtons(){
        initBackButton();
        initEditButton();
        initViewCommentButton();
    }

    void initViews(){
        _username = getView().findViewById(R.id.post_view_username);
        _commentStatus = getView().findViewById(R.id.post_view_comment_status);
        _content = getView().findViewById(R.id.post_view_content);
        _time = getView().findViewById(R.id.post_view_time_stamp);
        _like = getView().findViewById(R.id.post_view_like_status);
        _menuImageView = getView().findViewById(R.id.post_view_menu_image);
        _profileImageView = getView().findViewById(R.id.post_view_user_image);
    }

    void initViewCommentButton(){
        TextView _viewCommentBtn = getView().findViewById(R.id.post_view_comment_view_btn);
        _viewCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "VIEW COMMENTS");
            }
        });
    }

    void initBackButton(){
        TextView _backBtn = getView().findViewById(R.id.post_view_back_btn);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "BACK TO PREVIOUS PAGE");
            }
        });
    }

    void initEditButton(){
        TextView _editBtn = getView().findViewById(R.id.post_view_edit_btn);
        _editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "EDIT POST");
            }
        });
    }

}
