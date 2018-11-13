package com.example.foodagramapp.foodagram;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Like {
    private String userId, postId;
    private DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public Like(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        addDataToDatabase();
    }

    private void addDataToDatabase(){

    }
}
