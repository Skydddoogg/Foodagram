package com.example.foodagramapp.foodagram.Search;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Discover.BackStackTag.BackStackTag;
import com.example.foodagramapp.foodagram.Post;
import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText search_box;
    private ArrayList<User> user;
    private User user2, user3, user4;
    private SearchAdapter searchAdapter;
    private Bundle bundle;
    private boolean isLoadMore = false;
    private Handler mHandler = new Handler();
    private String searchBoxChar;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        search_box = getActivity().findViewById(R.id.search_box);

        user = new ArrayList<>();

        TextView backFragmentButton = (TextView) getActivity().findViewById(R.id.backButton_searchFragment);
        backFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack(new BackStackTag().BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });
        final ListView searchListView = (ListView) getView().findViewById(R.id.search_listView);
        searchAdapter = new SearchAdapter(
                getActivity(),
                R.layout.search_adapter,
                user
        );

        if (getArguments() != null) {
            bundle = getArguments();
            searchBoxChar = (bundle.getString("bundleSearchBox"));

        }

        searchListView.setDivider(null);
        //When scroling to last item

        search_box.setText(searchBoxChar);
        search_box.requestFocus();
        searchAdapter.getFilter().filter(searchBoxChar);
        searchListView.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !isLoadMore) {
                    isLoadMore = true;

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                            searchAdapter.notifyDataSetChanged();
                            isLoadMore = false;

                        }
                    }, 1500);

                }
            }
        });
        searchListView.setAdapter(searchAdapter);
        searchFunction();



    }

    private void searchFunction(){
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
                if (s.toString().isEmpty()) {
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack(new BackStackTag().BACK_STACK_RECENT_SEARCH_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
