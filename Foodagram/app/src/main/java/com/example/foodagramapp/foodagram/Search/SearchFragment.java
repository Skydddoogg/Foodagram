package com.example.foodagramapp.foodagram.Search;

import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.User;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText search_box;
    private ArrayList<User> user;
    private User user2, user3, user4;
    private SearchAdapter searchAdapter;
    private Bundle bundle;
    private String searchBoxChar;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String BACK_STACK_RECENT_SEARCH_TAG = "recent_search_fragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        search_box = getActivity().findViewById(R.id.search_box);

        user = new ArrayList<>();
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));
        user.add(new User("Pleng"));
        user.add(new User("Sky"));
        user.add(new User("Mask"));
        user.add(new User("Doctor"));
        user.add(new User("Bank"));
        user.add(new User("Mew"));

        TextView backFragmentButton = (TextView) getActivity().findViewById(R.id.backButton_searchFragment);
        backFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        search_box.setText(searchBoxChar);
        search_box.requestFocus();
        searchAdapter.getFilter().filter(searchBoxChar);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
                if (s.toString().isEmpty()) {
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack(BACK_STACK_RECENT_SEARCH_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchListView.setAdapter(searchAdapter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
