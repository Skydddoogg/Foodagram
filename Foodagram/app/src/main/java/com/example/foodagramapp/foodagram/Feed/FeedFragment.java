package com.example.foodagramapp.foodagram.Feed;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.example.foodagramapp.foodagram.OnlineUser;
import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.Post.PostViewFragment;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;


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
    private ArrayList<String> userIdFollowing = new ArrayList<>();


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

    //Dialog
    private CustomLoadingDialog customLoadingDialog;

    //Text View
    private TextView followPlease;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.feed_listView);
        followPlease = (TextView) getView().findViewById(R.id.follow_please);
        customLoadingDialog = new CustomLoadingDialog(getContext());
        customLoadingDialog.showDialog();
        postArrayList.clear();
        pullToRefresh();
        fetchFollowingForAUser();
        if (getActivity() != null) {
            feedAdapter = new FeedAdapter(getActivity(),
                    R.layout.feed_crad_view,
                    postArrayListBuffer,
                    likeCountArrayList,
                    profile,
                    commentCountArrayList,
                    postId
            );
        }
    }

    private void pullToRefresh() {
        pullToRefresh = getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFollowingForAUser();
                feedAdapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    private void fetchFollowingForAUser() {
        try {
            myRef = database.getReference("followingForAUser").child(new OnlineUser().ONLINE_USER);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userIdFollowing.clear();
                    for (final DataSnapshot user_id : dataSnapshot.getChildren()) {
                        userIdFollowing.add(user_id.getValue(String.class));
                    }

                    if (userIdFollowing.size() == 0) {
                        customLoadingDialog.dismissDialog();
                    } else {
                        followPlease.setVisibility(View.INVISIBLE);
                    }
                    fetchPost();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void fetchPost() {
        try {
            myRef = database.getReference("post");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postId.clear();
                    postArrayListBuffer.clear();
                    for (String userId : userIdFollowing) {
                        for (DataSnapshot post : dataSnapshot.getChildren()) {
                            if (post.child("owner").getValue(String.class).equals(userId)) {
                                postArrayListBuffer.add(post.getValue(Post.class));
                                postId.add(post.getKey());
                                fetchProfile(post.child("owner").getValue(String.class));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
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
            profile.clear();
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
        } catch (Exception e) {
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void fetchLike() {
        try {
            myRef = database.getReference("like");
            myRef.addValueEventListener(new ValueEventListener() {
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
        } catch (Exception e) {
            Log.e("FeedFragment", e.getLocalizedMessage());
        }
    }

    private void onClickItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Send Post Obj
                PostViewFragment obj;
                FragmentManager fm;
                FragmentTransaction ft;
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", feedAdapter.getItem(position));
                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction();
                obj = new PostViewFragment();
                obj.setArguments(bundle);
                ft.replace(R.id.main_view, obj).addToBackStack(null);
                ft.commit();
            }
        });

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
        } catch (Exception e) {
            Log.e("FeedFragment", e.getLocalizedMessage());
        }

    }


    private void renderUI() {
        if (getActivity() != null) {
            if (postArrayListBuffer != null) {
                customLoadingDialog.dismissDialog();
            }
            Parcelable state = listView.onSaveInstanceState();
            listView.setAdapter(feedAdapter);
            feedAdapter.notifyDataSetChanged();
            onClickItem();
            listView.onRestoreInstanceState(state);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }
}