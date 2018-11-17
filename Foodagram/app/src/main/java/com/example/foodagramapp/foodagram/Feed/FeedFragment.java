package com.example.foodagramapp.foodagram.Feed;

import android.os.Bundle;
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

import com.example.foodagramapp.foodagram.OnlineUser;
import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.Profile.ProfileForFeed;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    //DB
    private Query myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //ArrayList
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private ArrayList<Post> postArrayListBuffer = new ArrayList<>();
    private ArrayList<String> likeCountArrayList = new ArrayList<>();
    private ArrayList<ProfileForFeed> profile = new ArrayList<>();
    private ArrayList<String> postId = new ArrayList<>();
    private ArrayList<String> commentCountArrayList = new ArrayList<>();


    //List View
    private ListView listView;
    private FeedAdapter feedAdapter;

    //Pull To Refresh
    private SwipeRefreshLayout pullToRefresh;


    //Load More
    private int current_visible_item = 0;
    private int total_post = 0;
    private int limit_post = 2;
    private int inventory_post = 0;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.feed_listView);
        pullToRefresh();
        fetchFollowingForAUser();
        if (getActivity() != null) {
            feedAdapter = new FeedAdapter(getActivity(),
                    R.layout.feed_crad_view,
                    postArrayList,
                    likeCountArrayList,
                    profile,
                    commentCountArrayList
            );
        }
    }

    private void pullToRefresh() {
        pullToRefresh = getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFollowingForAUser();
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    private void fetchFollowingForAUser() {
        try {
            myRef = database.getReference("followingForAUser").child(new OnlineUser().ONLINE_USER);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (final DataSnapshot user_id : dataSnapshot.getChildren()) {
                        fetchPost(user_id.getValue(String.class));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void fetchPost(String user_id) {
        try {
            myRef = database.getReference("post");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postId.clear();
                    postArrayListBuffer.clear();
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        postArrayListBuffer.add(post.getValue(Post.class));
                        postId.add(post.getKey());
                        fetchProfile(post.child("owner").getValue(String.class));
                    }
                    total_post = postArrayListBuffer.size();
                    onScrollLoadItem();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }


    private void onScrollLoadItem() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (lastInScreen == totalItemCount) {
                    loadMore();
                }
            }
        });
    }


    private void loadMore() {
        if (current_visible_item >= total_post) {
            return;
        }
        if (total_post - current_visible_item > limit_post) {
            for (int position = 0; position < limit_post; position++) {
                postArrayList.add(postArrayListBuffer.get(current_visible_item));
                current_visible_item++;
            }
        } else {
            for (int position = 0; position < inventory_post; position++) {
                postArrayList.add(postArrayListBuffer.get(current_visible_item));
                current_visible_item++;
            }
        }
        inventory_post = total_post - current_visible_item;
        feedAdapter.notifyDataSetChanged();
    }

    private void fetchProfile(String user_id) {
        try {
            myRef = database.getReference("profile").child(user_id);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    profile.add(dataSnapshot.getValue(ProfileForFeed.class));
                    fetchLike();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void fetchLike() {
        try {
            myRef = database.getReference("like");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    likeCountArrayList.clear();
                    for (String post_id : postId) {
                        int likeCount = 0;
                        for (DataSnapshot user_id_like_this_post : dataSnapshot.child(post_id).child("by").getChildren()) {
                            likeCount++;
                        }
                        likeCountArrayList.add(likeCount + "");
                    }
                    fetchComment();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void fetchComment() {
        try {
            myRef = database.getReference("comment");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    commentCountArrayList.clear();
                    for (final String post_id : postId) {
                        int commentCount = 0;
                        for (DataSnapshot user_id_like_this_post : dataSnapshot.child(post_id).getChildren()) {
                            commentCount++;
                        }
                        commentCountArrayList.add(commentCount + "");
                    }
                    renderUI();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("FeedFragment", e.getLocalizedMessage());
        }

    }


    private void renderUI() {
        feedAdapter.notifyDataSetChanged();
        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(feedAdapter);
        listView.onRestoreInstanceState(state);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }
}
