package com.example.foodagramapp.foodagram.Feed;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.foodagramapp.foodagram.Post;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    private DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    static private ListView listView;
    //จำนวนที่ต้องการโหลดโพสต์ X/ครั้ง


    final private String ONLINE_USER = "test_user_id_1";
    static private int TOTAL_POST;
    static private int CURRENT_VISIBLE_ITEM;
    static private int LIMIT_POST_PER_SCROLL = 4;
    static private int INVENTORY_POST;
    private SwipeRefreshLayout pullToRefresh;
    private boolean firstRender = true;

    //Array
    static private ArrayList<String> followingForAUser;
    static private ArrayList<Post> posts = new ArrayList<>();
    static private ArrayList<Post> posts_for_render = new ArrayList<>();
    static private ArrayList<String> likeCount = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.feed_listView);
        followingForAUser = new ArrayList<>();
        fetchData();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


    private void fetchData() {
        fetchFollowingForAUser();
//        fetchProfile();

    }

    private void fetchFollowingForAUser() {
        myRef = database.getReference("followingForAUser").child(ONLINE_USER);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    followingForAUser.add(obj.getValue(String.class));
                }

                //When New Follow and Unfollow
                fetchPost();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage().toString());
            }
        });

    }

    private void fetchPost() {
        myRef = database.getReference("post");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int position = 0; position < followingForAUser.size(); position++) {
                    if (position == 0) {
                        posts.clear();
                    }
                    for (DataSnapshot obj : dataSnapshot.child(followingForAUser.get(position)).getChildren()) {
                        posts.add(obj.getValue(Post.class));
                    }
                }
                if (firstRender) {
                    firstRenderPost();
                    firstRender = false;
                }
                onScrollToLastItem();
                pullToRefresh();
                TOTAL_POST = posts.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage().toString());
            }
        });

    }

    private void fetchLikeCount() {
        Log.i("DETECT", posts_for_render+"");

    }


    private void onScrollToLastItem() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (lastInScreen == totalItemCount) {
                    if (limitPost()) {
                        updateUI();
                    }
                }
            }
        });
    }


    private boolean limitPost() {

        if (CURRENT_VISIBLE_ITEM >= TOTAL_POST) {
            return false;
        }

        if (TOTAL_POST - CURRENT_VISIBLE_ITEM > LIMIT_POST_PER_SCROLL) {
            for (int position = 0; position < LIMIT_POST_PER_SCROLL; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        } else {
            for (int position = 0; position < INVENTORY_POST; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        }

        INVENTORY_POST = TOTAL_POST - CURRENT_VISIBLE_ITEM;
        return true;
    }

    private void firstRenderPost() {
        posts_for_render.clear();
        CURRENT_VISIBLE_ITEM = 0;
        if (TOTAL_POST - CURRENT_VISIBLE_ITEM > LIMIT_POST_PER_SCROLL) {
            for (int position = 0; position < LIMIT_POST_PER_SCROLL; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        } else {
            for (int position = 0; position < TOTAL_POST; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        }
    }

    private void updatePost() {
        posts_for_render.clear();
        CURRENT_VISIBLE_ITEM = 0;
        if (TOTAL_POST - CURRENT_VISIBLE_ITEM > LIMIT_POST_PER_SCROLL) {
            for (int position = 0; position < LIMIT_POST_PER_SCROLL; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        } else {
            for (int position = 0; position < TOTAL_POST; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        }
    }

    private void pullToRefresh() {
        pullToRefresh = getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updatePost();
                pullToRefresh.setRefreshing(false);
                updateUI();
            }
        });
    }


    private void updateUI() {
        listView.setDivider(null);
        final FeedAdapter feedAdapter = new FeedAdapter(
                getActivity(),
                R.layout.feed_crad_view,
                posts_for_render


        );

        // Save the ListView state (= includes scroll position) as a Parceble
        Parcelable state = listView.onSaveInstanceState();
        // e.g. set new items
        listView.setAdapter(feedAdapter);
        // Restore previous state (including selected item index and scroll position)
        listView.onRestoreInstanceState(state);
    }


}
