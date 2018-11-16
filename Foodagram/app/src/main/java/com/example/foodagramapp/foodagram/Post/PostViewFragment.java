package com.example.foodagramapp.foodagram.Post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Comment.CommentFragment;
import com.example.foodagramapp.foodagram.Profile.Fragment_profile;
import com.example.foodagramapp.foodagram.Profile.ProfileForFeed;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.FirebaseException;
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
    private DatabaseReference databaseReferenceForUsername;
    private TextView _username, _time, _content, _like, _commentStatus, _commentViewBtn;
    private ImageView _menuImageView, _profileImageView;
    private Post post;
    private ProfileForFeed profile, testProfile;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initButtons();
        initViews();

        database = FirebaseDatabase.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null){
            post = bundle.getParcelable("post");
            _content.setText(post.getDescription());

            Double timestamp = post.getTimestamp();
            _time.setText(getCountOfDays(timestamp.longValue()));

            // Get username
            databaseReferenceForUsername = database.getReference("profile/" + post.getOwner());
            databaseReferenceForUsername.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    profile = dataSnapshot.getValue(ProfileForFeed.class);
                    _username.setText(profile.getUsername());
                    Picasso.get().load(profile.getProfile_img_url()).into(_profileImageView);

                    Bundle bundleForProfile = new Bundle();
                    bundleForProfile.putParcelable("profile", profile);
                    final Fragment_profile frag = new Fragment_profile();
                    frag.setArguments(bundleForProfile);
                    _profileImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, frag).commit();
                            Log.d(TAG, "GO TO PROFILE");
                        }
                    });

                    Log.d(TAG, "THE READ SUCCEEDED");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "THE READ FAILED: " + databaseError.getMessage());
                }
            });

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
                Bundle bundleForPostId = new Bundle();
                bundleForPostId.putString("postId", post.getPostId());
                bundleForPostId.putString("postOwner", post.getOwner());
                CommentFragment frag = new CommentFragment();
                frag.setArguments(bundleForPostId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, frag).commit();
                Log.d(TAG, "POST ID = " + bundleForPostId.getString("postId"));
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

    public String getCountOfDays(long time) {
        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

}
