package com.example.foodagramapp.foodagram.Feed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.Search.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private FeedAdapter feedAdapter;
    private ArrayList<String> postStore;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postStore = new ArrayList<>();
        postStore.add("TTTTT");
        postStore.add("TTTTT");
        postStore.add("TTTTT");
        postStore.add("TTTTT");
        postStore.add("TTTTT");
        final ListView listView = (ListView) getView().findViewById(R.id.feed_listView);
        feedAdapter = new FeedAdapter(
                getActivity(),
                R.layout.feed_crad_view,
                postStore
        );
        listView.setDivider(null);
        listView.setAdapter(feedAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }
}
