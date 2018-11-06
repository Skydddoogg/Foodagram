package com.example.foodagramapp.foodagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.foodagramapp.foodagram.Post.CameraFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new CameraFragment())
                    .commit();
        }
    }
}