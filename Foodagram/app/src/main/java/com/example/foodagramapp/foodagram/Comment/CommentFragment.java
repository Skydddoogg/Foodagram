package com.example.foodagramapp.foodagram.Comment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentFragment extends Fragment {

    private String ONLINE_USER = "test_user_id_1";
    private TextView postButton;
    private EditText commentForm;
//    Array
    private ArrayList<Profile> profiles = new ArrayList<>();
    private ArrayList<Comment> comment = new ArrayList<Comment>();
    private Comment commentClass = new Comment();

//    Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef, myCommentRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchComment();
        fetchProfile();

        postButton = getView().findViewById(R.id.comment_form_submit);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentForm = getView().findViewById(R.id.comment_form);
                String commentText = commentForm.getText().toString();
                Long commentTimeStamp = System.currentTimeMillis();

                pushComment(commentText, commentTimeStamp);

                commentForm.getText().clear();
                Log.d("checkBug", "--------------------------------------------- fuck Post -----------------------------------------");

            }
        });
        Log.d("checkBug", "--------------------------------------------- fuck onActivityCreated -----------------------------------------");
    }

    public void fetchComment(){
        myRef = database.getReference("/comment/test_post_id_1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comment.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    comment.add(child.getValue(Comment.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HISTORY", "ERROR CANNOT RETREIVE HISTORY FROM FIREBASE");
            }
        });
    }


    private void fetchProfile() {
        //Child ให้ใส่ User ที่เป็นคน Login เพื่อดู User คนนี้กดติดตามใคร
        myRef = database.getReference().child("profile");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profiles.clear();
                for (Comment user_id : comment) {
                    profiles.add(dataSnapshot.child(user_id.getUser_id()).getValue(Profile.class));
                    Log.d("checkBug", "--------------------------------------------- fuck -----------------------------------------");
                }
                profiles.clear();

                renderPost();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage());
            }
        });
    }

    private void renderPost() {
        ListView commentList = (ListView) getView().findViewById(R.id.comment_list);
        commentList.setDivider(null);
        final CommentAdapter commentAdapter = new CommentAdapter(
                getActivity(),
                R.layout.fragment_comment_item,
                comment,
                profiles
        );
        commentList.setAdapter(commentAdapter);
    }


    private void pushComment(final String commentText, final Long commentTimeStamp){
        myCommentRef = database.getReference("/comment/test_post_id_1");
        myCommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentClass = new Comment(ONLINE_USER, commentText, commentTimeStamp.doubleValue());
                myCommentRef.push().setValue(commentClass);

                Log.d("checkBug", "--------------------------------------------- fuck push -----------------------------------------");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HISTORY", "ERROR CAN'T RETREIVE USER PROFILE FROM FIREBASE");
            }
        });
    }
}
