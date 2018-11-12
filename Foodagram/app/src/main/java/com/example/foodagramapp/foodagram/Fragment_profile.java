package com.example.foodagramapp.foodagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Fragment_profile extends Fragment {

    ArrayList<Model_profile> profileInfo = new ArrayList<Model_profile>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Button editProfileBtn = (Button) getView().findViewById(R.id.profile_edit_profile);
        ImageView profileImage = getView().findViewById(R.id.profile_image);

        ListView profilePostList = getView().findViewById(R.id.profile_all_post_list);
        Adapter profileAdapter = new Adapter_profile(
                getActivity(),
                R.layout.fragment_profile_item,
                profileInfo
        );


//        profilePostList.setAdapter(profileAdapter);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_editProfile())
                        .addToBackStack(null)
                        .commit();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");


}
