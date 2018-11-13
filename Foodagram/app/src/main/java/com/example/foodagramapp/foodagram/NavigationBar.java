package com.example.foodagramapp.foodagram;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;


public class NavigationBar extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        createBottomNavBar();
    }

    void createBottomNavBar(AHBottomNavigation bottomNavigation, ImageView plusImg){
//        AHBottomNavigation bottomNavigation = getView().findViewById(R.id.bottom_navigation);
//        final ImageView plusImg = getView().findViewById(R.id.imageView);
        String backgroundColor = "#ffffff";
        String iconOnActiveColor = "#ff1c49";
        String iconInActiveColor = "#5a6b84";
        int defaultIconButtonPosition = 0;
        int addImgButtonPosition = 2;
        int notificationButtonPosition = 3;

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
        bottomNavigation.setNotification("5", notificationButtonPosition);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(defaultIconButtonPosition);

        // Disable item
        bottomNavigation.disableItemAtPosition(addImgButtonPosition);

        //REPLACE WITH IMAGEVIEW
        plusImg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do somthing
                        Log.i("NAVBAR", "SELECT UPLOAD IMG");
                    }
                }
        );

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position){
                    case 0:
                        //SELECT HOME BUTTON ON NAV BAR
                        Log.i("NAVBAR", "SELECT HOME");
                        break;
                    case 1:
                        //SELECT DISCOVER BUTTON ON NAV BAR
                        Log.i("NAVBAR", "SELECT DISCOVER");
                        break;
                    case 2:
                        //DISABLE THIS BUTTON BECAUSE REPLACE IT WITH IMAGEVIEW
                        break;
                    case 3:
                        //SELECT NOTIFICATION BUTTON ON NAV BAR
                        Log.i("NAVBAR", "SELECT NOTIFICATION");
                        break;
                    case 4:
                        //SELECT PROFILE BUTTON ON NAV BAR
                        Log.i("NAVBAR", "SELECT PROFILE");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }
}
