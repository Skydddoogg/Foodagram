package com.example.foodagramapp.foodagram.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.foodagramapp.foodagram.R;

public class Extension extends Fragment {

    public static void goTo(FragmentActivity act, Fragment other) {
        act.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, other)
                .addToBackStack(null)
                .commit();
    }

    public static void toast(FragmentActivity act, String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }
}


