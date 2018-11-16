package com.example.foodagramapp.foodagram.Comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodagramapp.foodagram.OnlineUser;
import com.example.foodagramapp.foodagram.Profile.ProfileForFeed;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentFragment extends Fragment {

    private OnlineUser onlineUser = new OnlineUser();
    private String CURRENT_USER = onlineUser.ONLINE_USER;
    private String CURRENT_POST;
    private String thumbnailUrl;
    private ImageView userThumbnail;
    private TextView postButton, backButton;
    private EditText commentForm;
    private Bundle bundle;
    private ArrayList<Comment> comment = new ArrayList<Comment>();
    private ArrayList<ProfileForFeed> profiles = new ArrayList<ProfileForFeed>();
    private Comment commentClass = new Comment();
    private ListView commentList;
    private CommentAdapter commentAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef, myCommentRef, myfetchCommentRef, myfetchProfileRef, myThumbnailRef;
    private String TAG = "CommentFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            CURRENT_POST = bundle.getString("postId");
            Log.d(TAG, "POST ID = " + CURRENT_POST);
        }

        fetchComment();
        profileThumbnail();
        postButton = getView().findViewById(R.id.comment_form_submit);
        backButton = getView().findViewById(R.id.backButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentForm = getView().findViewById(R.id.comment_form);
                String commentText = commentForm.getText().toString();

                if(commentText.isEmpty()){
                    Toast.makeText(getActivity(), "ยังไม่พิมพ์เลย กลับไปพิมพ์ก่อน",Toast.LENGTH_SHORT).show();
                }
                else{
                    Long commentTimeStamp = System.currentTimeMillis();
                    pushComment(commentText, commentTimeStamp);
                }

                commentForm.getText().clear();

            }
        });

        // Back Stack Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "BACK TO MENU");
            }
        });
    }

    public void fetchComment(){
        myfetchCommentRef = database.getReference("comment").child(CURRENT_POST);
        myfetchCommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comment.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    comment.add(child.getValue(Comment.class));
                    fetchProfile();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    private void fetchProfile(){
        myfetchProfileRef = database.getReference("profile");
        myfetchProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                renderPost();
                profiles.clear();
                for (Comment user_id : comment) {
                    profiles.add(dataSnapshot.child(user_id.getUser_id()).getValue(ProfileForFeed.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }

    private void renderPost() {
        commentList = (ListView) getView().findViewById(R.id.comment_list);
        commentList.setDivider(null);
        commentAdapter = new CommentAdapter(
                getActivity(),
                R.layout.fragment_comment_item,
                comment,
                profiles
        );
        commentList.setAdapter(commentAdapter);

        onClickItem();
    }

    private void onClickItem(){
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {bundle = new Bundle();
//                bundle.putString("userId", commentAdapter.getItem(position).getUser_id());
//
//                fragmentProfile = new FragmentProfile();
//
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, fragmentProfile.setArguments(bundle)).addToBackStack(null).commit();
//                Log.d("MENU", "Select" + menu.get(position));

                Log.i(TAG, commentAdapter.getItem(position).getUser_id());
            }
        });
    }

    private void pushComment(final String commentText, final Long commentTimeStamp){
        myCommentRef = database.getReference("comment").child(CURRENT_POST);
        commentClass = new Comment(CURRENT_USER, commentText, commentTimeStamp.doubleValue());
        myCommentRef.push().setValue(commentClass);
        Log.d(TAG, "PUSH COMMENT");
    }

    private void  profileThumbnail(){
        userThumbnail = (ImageView) getView().findViewById(R.id.comment_form_user_thumbnail);
        myThumbnailRef = database.getReference("profile").child(CURRENT_USER);
        myThumbnailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thumbnailUrl = dataSnapshot.child("profile_img_url").getValue(String.class);
                Picasso.get().load(thumbnailUrl).into(userThumbnail);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, databaseError.getMessage());
            }
        });
    }

}