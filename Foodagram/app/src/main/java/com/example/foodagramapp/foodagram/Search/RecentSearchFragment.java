package com.example.foodagramapp.foodagram.Search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Discover.BackStackTag.BackStackTag;
import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.User;

import java.util.ArrayList;

public class RecentSearchFragment extends Fragment {
    private EditText search_box;
    private ArrayList<User> user;
    private User user2, user3, user4;
    private SearchAdapter searchAdapter;
    private Bundle bundle;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SearchFragment obj;
    private TextView backFragmentButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        search_box = getActivity().findViewById(R.id.search_box);
        backFragmentButton = (TextView) getActivity().findViewById(R.id.backButton_searchFragment);

        user = new ArrayList<>();
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));
        user.add(new User("Pleng"));

        //Setup Go To Search Fragment Button
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    replaceSearchFragment(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Setup Back To Discover Button
        backFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack(new BackStackTag().BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });


        //Setup Listview
        setupSearchAdapter(setupSearchListView((ListView) getView().findViewById(R.id.recent_search_listView)), user);


    }

    public ListView setupSearchListView(ListView listView) {
        listView.setDivider(null);
        View header = (View) getLayoutInflater().inflate(R.layout.header_recent_search_fragment, null);
        listView.addHeaderView(header);
        return listView;
    }

    public void setupSearchAdapter(ListView searchListView, ArrayList<User> arrayList) {
        searchAdapter = new SearchAdapter(
                getActivity(),
                R.layout.search_adapter,
                arrayList
        );
        searchListView.setAdapter(searchAdapter);
    }

    private void replaceSearchFragment(CharSequence s) {
        bundle = new Bundle();
        bundle.putString("bundleSearchBox", s + "");
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        obj = new SearchFragment();
        obj.setArguments(bundle);
        ft.replace(R.id.main_view, obj).addToBackStack(new BackStackTag().BACK_STACK_RECENT_SEARCH_TAG);
        search_box.getText().clear();
        ft.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_search, container, false);
    }
}
