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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Tag.Tag;
import com.example.foodagramapp.foodagram.OnlineUser;
import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private EditText search_box;
    final private String ONLINE_USER = new OnlineUser().ONLINE_USER;
    private TextView backToDiscover;
    private boolean firstSearch = true;
    //List View
    private ListView searchListView;
    private SearchAdapter adapter;

    //DB
    private DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Array
    private ArrayList<Profile> profileBuffer = new ArrayList<>();
    private ArrayList<Profile> following = new ArrayList<>();
    private ArrayList<String> follower = new ArrayList<>();
    private ArrayList<String> recentSearchBuffer = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        search_box = getActivity().findViewById(R.id.search_box);
        search_box.requestFocus();

        searchListView = (ListView) getView().findViewById(R.id.search_listView);
        searchListView.setDivider(null);
        backToDiscover = (TextView) getActivity().findViewById(R.id.backButton_searchFragment);
        backToDiscover();
        try {
            firebaseUserSearch();

        } catch (Exception e) {
            Log.e("DETECT", e.getLocalizedMessage());
        }

    }

    private void backToDiscover() {
        //Setup Back To Discover Button
        backToDiscover = (TextView) getActivity().findViewById(R.id.back_to_discover);
        backToDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack(new Tag().DISCOVER_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });
    }

    private void filterProfile() {
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adapter.notifyDataSetChanged();
    }

    private void fetchFollowing() {
        myRef = database.getReference("followingForAUser").child(ONLINE_USER);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following.clear();
                if(dataSnapshot.getChildren() != null) {
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        myRef = database.getReference("profile").child(obj.getValue(String.class));
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                following.add(dataSnapshot.getValue(Profile.class));
                                renderUI();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firebaseUserSearch() {
        myRef = database.getReference("profile");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileBuffer.clear();
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    if (!obj.getKey().equals(ONLINE_USER)) {
                        profileBuffer.add(obj.getValue(Profile.class));
                        fectFollower(obj.getKey());
                    }

                }
                renderUI();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fectFollower(final String usrId) {
        myRef = database.getReference("followerForAUser").child(usrId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                follower.clear();
                int count = 0;
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    count++;
                }
                follower.add(count + "");
                fetchFollowing();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    private void onClickItem(){
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Query myRef = database.getReference("profile").orderByChild("email").equalTo(adapter.getItem(position).getEmail());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot obj : dataSnapshot.getChildren()) {
                            //Send User Id
                            // SleepForm obj;
                            FragmentManager fm;
                            FragmentTransaction ft;
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", obj.getKey());
//                            fm = getActivity().getSupportFragmentManager();
//                            ft = fm.beginTransaction();
//                            obj = new SleepForm();
//                            obj.setArguments(bundle);
//                            ft.replace(R.id.main_view, obj);
//                            ft.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    private void renderUI() {
        adapter = new SearchAdapter(
                getActivity(),
                R.layout.search_adapter,
                profileBuffer,
                following
        );

        searchListView.setAdapter(adapter);
        filterProfile();
        onClickItem();
        adapter.notifyDataSetChanged();


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
