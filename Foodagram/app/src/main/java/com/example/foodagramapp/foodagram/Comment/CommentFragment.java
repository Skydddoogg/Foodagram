package com.example.foodagramapp.foodagram.Comment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class CommentFragment extends Fragment {

    private String CURRUNT_USER = "4EU0qwzeuhOBJdsE0vsuTBNYN2A2";
    private String CURRENT_POST = "test_post_id_13";
    private String thumbnailUrl;
    private ImageView userThumbnail;
    private TextView postButton, backButton;
    private EditText commentForm;
    private Bundle bundle;
    private ArrayList<Comment> comment = new ArrayList<Comment>();
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private Comment commentClass = new Comment();
//    private FragmentProfile fragmentProfile;
    private ListView commentList;
    private CommentAdapter commentAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef, myCommentRef, myfetchCommentRef, myfetchProfileRef, myThumbnailRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchComment();
        profileThumbnail();
        postButton = getView().findViewById(R.id.comment_form_submit);
        backButton = getView().findViewById(R.id.backButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentForm = getView().findViewById(R.id.comment_form);
                String commentText = commentForm.getText().toString();
                Long commentTimeStamp = System.currentTimeMillis();

                Log.d("checkBug", "-------------------------------------------->>> POST : " + commentText);
                pushComment(commentText, commentTimeStamp);

                commentForm.getText().clear();

            }
        });

        // Back Stack Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BACK", "Back to menu");
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
                Log.i("HISTORY", "ERROR CANNOT RETREIVE HISTORY FROM FIREBASE");
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
                    profiles.add(dataSnapshot.child(user_id.getUser_id()).getValue(Profile.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HISTORY", "ERROR CANNOT RETREIVE HISTORY FROM FIREBASE");
            }
        });

    }

    // render Comment Adapter

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

                Log.i("checkBug", commentAdapter.getItem(position).getUser_id());
            }
        });
    }

    // push comment to database

    private void pushComment(final String commentText, final Long commentTimeStamp){
        myCommentRef = database.getReference("comment").child(CURRENT_POST);
        commentClass = new Comment(CURRUNT_USER, commentText, commentTimeStamp.doubleValue());
        myCommentRef.push().setValue(commentClass);
        Log.d("checkBug", "-------------------------------------------->>> PUSH");
    }

    private void  profileThumbnail(){
        userThumbnail = (ImageView) getView().findViewById(R.id.comment_form_user_thumbnail);
        myThumbnailRef = database.getReference("profile").child(CURRUNT_USER);
        myThumbnailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thumbnailUrl = dataSnapshot.child("profile_img_url").getValue(String.class);
                Picasso.get().load(thumbnailUrl).into(userThumbnail);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HISTORY", "ERROR CANNOT RETREIVE HISTORY FROM FIREBASE");
            }
        });
    }

}
