package com.example.foodagramapp.foodagram.PostView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodagramapp.foodagram.Comment.Comment;
import com.example.foodagramapp.foodagram.R;

import java.util.ArrayList;


public class PostViewFragment extends Fragment {

    private ArrayList<Comment> comments = new ArrayList<Comment>();

    public PostViewFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        ImageView img = getView().findViewById(R.id.imageView);
//        img.setImageResource(R.drawable.food_pic);
//
//        CircularImageView circularImageView = getView().findViewById(R.id.post_user_thumbnail);
//        circularImageView.setImageResource(R.drawable.food_pic);

    }
}
