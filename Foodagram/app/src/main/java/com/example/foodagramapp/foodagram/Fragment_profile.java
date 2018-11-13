package com.example.foodagramapp.foodagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_profile extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private Adapter_profile profileAdapter;

    ArrayList<Model_profile> profileInfo = new ArrayList<Model_profile>();
    Model_profile exampleInfo = new Model_profile("Pizza", "I kwai oat" , "11/00/2018");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileInfo.add(exampleInfo);
        myRef = database.getReference().child("post");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot db: dataSnapshot.getChildren()){

                    //** Check data from person's profile
                    if(db.getKey().equals("test_user_id_1")) {
                        // ** Get profile's post
                        for(DataSnapshot dbProfile : db.getChildren()) {


                            String menu_name = (String) dbProfile.child("menu_name").getValue();
                            String description = (String) dbProfile.child("description").getValue();
                            String owner = (String) dbProfile.child("owner").getValue();
                            String location = (String) dbProfile.child("location").getValue();
//                            String time = (String) dbProfile.child("timestamp").getValue();
                            exampleInfo = new Model_profile(menu_name, owner, "50");
                            profileInfo.add(exampleInfo);
                            profileAdapter.notifyDataSetChanged();

//
//
//                        Log.i("Profile post", "" + db.getKey());
//                        Log.i("Profile post", "" + db.child("test_post_id_" + "2").child("menu_name").getValue());
//                        Log.i("Profile post", "" + db.child("test_post_id_" + "2").child("description").getValue());
//                        Log.i("Profile post", "" + db.child("test_post_id_" + "2").child("location").getValue());
//                        Log.i("Profile post", "" + db.child("test_post_id_" + "2").child("menu_price").getValue());
//                        Log.i("Profile post", "" + db.child("test_post_id_" + "2").child("owner").getValue());
//                        Log.i("TT", "" + menu_name + db.child("test_post_id_2").getValue());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button editProfileBtn = (Button) getView().findViewById(R.id.profile_edit_profile);
        ImageView profileImage = getView().findViewById(R.id.profile_image);

        final ListView profilePostList = getView().findViewById(R.id.profile_all_post_list);
        profileAdapter = new Adapter_profile(
                getActivity(),
                R.layout.fragment_profile_item,
                profileInfo
        );


        profilePostList.setAdapter(profileAdapter);
        profileAdapter.notifyDataSetChanged();
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

    }




}
