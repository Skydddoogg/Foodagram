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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FeedFragment extends Fragment {
    private DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    static private ListView listView;
    //จำนวนที่ต้องการโหลดโพสต์ X/ครั้ง


    final private String ONLINE_USER = new OnlineUser().ONLINE_USER;
    static private int TOTAL_POST;
    static private int CURRENT_VISIBLE_ITEM = 0;
    static private int LIMIT_POST_PER_SCROLL = 4;
    static private int INVENTORY_POST;
    private SwipeRefreshLayout pullToRefresh;
    private boolean firstRender = true;
    private FeedAdapter feedAdapter;
    //Array
    static private ArrayList<String> followingForAUser;
    static private ArrayList<Post> posts = new ArrayList<>();
    static private ArrayList<Post> posts_for_render = new ArrayList<>();
    static private ArrayList<String> postId = new ArrayList<>();
    static private ArrayList<String> postId_for_render = new ArrayList<>();
    final private ArrayList<String> likeCout = new ArrayList<>();
    static private ArrayList<ProfileForFeed> profiles = new ArrayList<>();
    static private ArrayList<String> comments = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() != null) {
            listView = (ListView) getActivity().findViewById(R.id.feed_listView);
            followingForAUser = new ArrayList<>();
            pullToRefresh = new SwipeRefreshLayout(getContext());
            pullToRefresh = getActivity().findViewById(R.id.pullToRefresh);
        }
        fetchFollowingForAUser();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("FeedFragment", String.valueOf(isVisibleToUser));
        if (isVisibleToUser) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        } else {
            clearStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


    private void fetchFollowingForAUser() {
        myRef = database.getReference("followingForAUser").child(ONLINE_USER);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingForAUser.clear();
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    followingForAUser.add(obj.getValue(String.class));
                }

                //When New Follow and Unfollow
                fetchPost();

//                feedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage().toString());
            }
        });

    }

    private void fetchPost() {
        Query myRef = database.getReference("post");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int position = 0; position < followingForAUser.size(); position++) {
                    if (position == 0) {
                        posts.clear();
                        postId.clear();

                    }
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        if (obj.child("owner").getValue(String.class).equals(followingForAUser.get(position))) {
                            posts.add(obj.getValue(Post.class));
                            postId.add(obj.getKey());
                        }
                    }
                }
                TOTAL_POST = posts.size();
                Log.d("FeedFragment", Integer.toString(TOTAL_POST));
                onScrollToLastItem();
                pullToRefresh();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage().toString());
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
                for (Post user_id : posts_for_render) {
                    profiles.add(dataSnapshot.child(user_id.getOwner()).getValue(ProfileForFeed.class));
                }
                fetchComment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage());
            }
        });
    }

    private void fetchComment() {
        //Child ให้ใส่ User ที่เป็นคน Login เพื่อดู User คนนี้กดติดตามใคร
        myRef = database.getReference().child("comment");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.clear();
                for (String str : postId_for_render) {
                    int count = 0;
                    for (DataSnapshot obj : dataSnapshot.child(str).getChildren()) {
                        count++;
                    }
                    comments.add(count + "");
                }
                renderPost();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FeedFragment", databaseError.getMessage());
            }
        });
    }


    private void fetchLikeCount() {
        myRef = database.getReference("like");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likeCout.clear();
                for (int position = 0; position < postId_for_render.size(); position++) {
                    String postIdStr = dataSnapshot.child(postId_for_render.get(position)).getKey().toString();
                    int count = 0;
                    for (DataSnapshot obj : dataSnapshot.child(postIdStr).child("by").getChildren()) {
                        count++;
                    }
                    likeCout.add(count + "");


                }

                fetchProfile();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void sortPost() {
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                if (o1.getTimestamp() == o2.getTimestamp())
                    return 0;

                if (o1.getTimestamp() < o2.getTimestamp())
                    return 1;
                else
                    return -1;
            }
        });
    }

    private boolean loadMore() {
        if (CURRENT_VISIBLE_ITEM >= TOTAL_POST) {
            return false;
        }
        if (TOTAL_POST - CURRENT_VISIBLE_ITEM > LIMIT_POST_PER_SCROLL) {
            for (int position = 0; position < LIMIT_POST_PER_SCROLL; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                //Add Post Id Str to postId_for_render
                postId_for_render.add(postId.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        } else {
            for (int position = 0; position < INVENTORY_POST; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                //Add Post Id Str to postId_for_render
                postId_for_render.add(postId.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        }
        INVENTORY_POST = TOTAL_POST - CURRENT_VISIBLE_ITEM;
        return true;
    }

    private void updatePost() {
        posts_for_render.clear();
        postId_for_render.clear();
        CURRENT_VISIBLE_ITEM = 0;
        if (TOTAL_POST - CURRENT_VISIBLE_ITEM > LIMIT_POST_PER_SCROLL) {
            for (int position = 0; position < LIMIT_POST_PER_SCROLL; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                //Add Post Id Str to postId_for_render
                postId_for_render.add(postId.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        } else {
            for (int position = 0; position < TOTAL_POST; position++) {
                posts_for_render.add(posts.get(CURRENT_VISIBLE_ITEM));
                //Add Post Id Str to postId_for_render
                postId_for_render.add(postId.get(CURRENT_VISIBLE_ITEM));
                CURRENT_VISIBLE_ITEM++;
            }
        }
    }

    //EVENT
    private void pullToRefresh() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                clearStack();
                feedAdapter.clear();
                updatePost();
                fetchLikeCount();
                pullToRefresh.setRefreshing(false);
            }
        });
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
                    if (loadMore()) {
                        fetchLikeCount();
                    }
                }
            }
        });
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
                ft.replace(R.id.main_view, obj);
                ft.commit();
            }
        });

    }

    //RENDER UI
    private void renderPost() {
        listView.setDivider(null);
        if (getActivity() != null) {
            feedAdapter = new FeedAdapter(
                    getActivity(),
                    R.layout.feed_crad_view,
                    posts_for_render,
                    likeCout,
                    profiles,
                    postId_for_render,
                    ONLINE_USER,
                    comments
            );


            // Save the ListView state (= includes scroll position) as a Parceble
            Parcelable state = listView.onSaveInstanceState();
            // e.g. set new items
            listView.setAdapter(feedAdapter);
            // Restore previous state (including selected item index and scroll position)
            listView.onRestoreInstanceState(state);
            onClickItem();

            feedAdapter.notifyDataSetChanged();
        }
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getFragmentManager().popBackStackImmediate();
            }
        }
        //Here we are removing all the fragment that are shown here
        if (getFragmentManager().getFragments() != null && getFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }


}
