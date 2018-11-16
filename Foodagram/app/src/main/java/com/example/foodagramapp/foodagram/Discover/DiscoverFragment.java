package com.example.foodagramapp.foodagram.Discover;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.foodagramapp.foodagram.OnlineUser;
import com.example.foodagramapp.foodagram.Tag.Tag;
import com.example.foodagramapp.foodagram.Post;
import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Search.SearchFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DiscoverFragment extends Fragment {
    private RecyclerView recyclerView;
    private Bitmap[] bitmaps;
    private String ONLINE_USER = new OnlineUser().ONLINE_USER;

    private EditText searchBox;

    //ArrayList
    private ArrayList<String> postOwnerId = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Profile> owernProfile = new ArrayList<>();

    //DB
    private Query myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchBox = getActivity().findViewById(R.id.search_button);
        onClickSearchBox();
        fetchPost();
    }


    private void fetchPost() {
        myRef = database.getReference("post").limitToFirst(10);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                postOwnerId.clear();
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    if (!obj.child("owner").getValue(String.class).equals(ONLINE_USER)) {
                        posts.add(obj.getValue(Post.class));
                        postOwnerId.add(obj.child("owner").getValue() + "");
                    }
                }
                fetchProfile();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchProfile() {
        myRef = database.getReference("profile");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                owernProfile.clear();
                for (String ownerId : postOwnerId) {
                    owernProfile.add(dataSnapshot.child(ownerId).getValue(Profile.class));
                }
                randomItem();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void renderPost() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.discoverRecycleViewMian);

        mAdapter = new DiscoverAdapter(posts, owernProfile, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void randomItem() {
        Collections.shuffle(posts);
        renderPost();
    }

    private void onClickSearchBox() {
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new SearchFragment())
                        .addToBackStack(new Tag().DISCOVER_FRAGMENT)
                        .commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }


}
