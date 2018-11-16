package com.example.foodagramapp.foodagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.example.foodagramapp.foodagram.Discover.DiscoverFragment;
import com.example.foodagramapp.foodagram.Feed.FeedFragment;
import com.example.foodagramapp.foodagram.Search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new SearchFragment())
                    .commit();
        }
        NavigationBar nav = new NavigationBar();
        nav.createBottomNavBar((AHBottomNavigation) findViewById(R.id.bottom_navigation), (ImageView) findViewById(R.id.imageView));
    }
}
