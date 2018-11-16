package com.example.foodagramapp.foodagram;

import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikeAction {
    private String userId, postId, likeCount;
    private DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private boolean checkLikeStatus = true;
    private ArrayList<String> userIdBuffer = new ArrayList<>();
    private ImageView button;


    public LikeAction(String userId, String postId, String likeCount, ImageView button) {
        this.userId = userId;
        this.postId = postId;
        this.likeCount = likeCount;
        this.button = button;
    }

    public void setColorButton() {
        myRef = database.getReference("like").child(postId).child("by");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIdBuffer.clear();
                if(!likeCount.equals("NaN")) {
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        String userIdDb = obj.getValue(String.class).toString();
                        userIdBuffer.add(obj.getValue(String.class));
                        if (userIdDb.equals(userId)) {
                            button.setColorFilter(Color.parseColor("#FF1C49"));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void checkLike() {
        myRef = database.getReference("like").child(postId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIdBuffer.clear();
                for (DataSnapshot obj : dataSnapshot.child("by").getChildren()) {
                    String userIdDb = obj.getValue().toString();
                    userIdBuffer.add(userIdDb);
                    if (userIdDb.equals(userId)) {
                        checkLikeStatus = false;
                        return;
                    }

                }

                if(checkLikeStatus){
                    addDataInDatabase();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addDataInDatabase() {
        userIdBuffer.add(userId);
        myRef = database.getReference("like").child(postId).child("by");
        myRef.setValue(userIdBuffer);
    }

    private int toInt(String str) {
        return Integer.parseInt(str);
    }
}
