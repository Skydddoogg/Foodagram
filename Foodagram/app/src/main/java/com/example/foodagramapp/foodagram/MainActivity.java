package com.example.foodagramapp.foodagram;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.foodagramapp.foodagram.Discover.DiscoverFragment;
import com.example.foodagramapp.foodagram.Feed.FeedFragment;
import com.example.foodagramapp.foodagram.Notification.Notification;
import com.example.foodagramapp.foodagram.Post.PostActivity;
import com.example.foodagramapp.foodagram.Post.PostViewFragment;
import com.example.foodagramapp.foodagram.Profile.Fragment_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    static ViewPager mViewPager;
    private FeedFragment feedFragment;
    private DiscoverFragment discoverFragment;
    private Fragment_profile fragment_profile;
    private NotificationFragment notificationFragment;
    static AHBottomNavigation bottomNavigation;
    static int notificationButtonPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new FeedFragment())
                    .commit();
        }
        createBottomNavBar();
        initFragments();
    }

    void createBottomNavBar() {

//        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        mViewPager = findViewById(R.id.viewpager_container);
//        mViewPager.setAdapter(adapter);
//        mViewPager.setCurrentItem(0);

        try {
            bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.bringToFront();
            bottomNavigation.invalidate();
            final ImageView plusImg = findViewById(R.id.imageView);
            String backgroundColor = "#ffffff";
            String iconOnActiveColor = "#ff1c49";
            String iconInActiveColor = "#5a6b84";
            int defaultIconButtonPosition = 0;
            int addImgButtonPosition = 2;
            notificationButtonPosition = 3;

            // Add icon to navbar (null because we don't need icon title)
            bottomNavigation.addItem(new AHBottomNavigationItem(null, R.drawable.ic_home));
            bottomNavigation.addItem(new AHBottomNavigationItem(null, R.drawable.ic_dis));
            bottomNavigation.addItem(new AHBottomNavigationItem(null, R.drawable.ic_plus_sign));
            bottomNavigation.addItem(new AHBottomNavigationItem(null, R.drawable.ic_noti));
            bottomNavigation.addItem(new AHBottomNavigationItem(null, R.drawable.ic_profile));

            // Set navbar's background color
            bottomNavigation.setDefaultBackgroundColor(Color.parseColor(backgroundColor));

            // Change icon's color
            bottomNavigation.setAccentColor(Color.parseColor(iconOnActiveColor));
            bottomNavigation.setInactiveColor(Color.parseColor(iconInActiveColor));

            // Set navbar's status
            bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

            // Add or remove notification for each item
            try {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("notification/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getValue(Notification.class).getViewed() == new Double(0)) {
                                bottomNavigation.setNotification(" ", notificationButtonPosition);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }

            // Set current item programmatically
            bottomNavigation.setCurrentItem(defaultIconButtonPosition);

            // Disable item
            bottomNavigation.disableItemAtPosition(addImgButtonPosition);

            //REPLACE WITH IMAGEVIEW
            plusImg.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                            Log.i("NAVBAR", "SELECT UPLOAD IMG");
                        }
                    }
            );

            // Set listeners
            bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    switch (position) {
                        case 0:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, feedFragment)
                                    .addToBackStack(null)
                                    .commit();
                            Log.i("NAVBAR", "SELECT HOME");
                            break;
                        case 1:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, discoverFragment)
                                    .addToBackStack(null)
                                    .commit();
//                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
                            Log.i("NAVBAR", "SELECT DISCOVER");
                            break;
                        case 2:
                            //DISABLE THIS BUTTON BECAUSE REPLACE IT WITH IMAGEVIEW
                            break;
                        case 3:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, notificationFragment)
                                    .addToBackStack(null)
                                    .commit();
                            Log.i("NAVBAR", "SELECT NOTIFICATION");
                            break;
                        case 4:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, fragment_profile)
                                    .addToBackStack(null)
                                    .commit();
                            Log.i("NAVBAR", "SELECT PROFILE");
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
                @Override
                public void onPositionChange(int y) {
                    // Manage the new y position
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void initFragments(){
        feedFragment = new FeedFragment();
        discoverFragment = new DiscoverFragment();
        fragment_profile = new Fragment_profile();
        notificationFragment = new NotificationFragment();
    }

}