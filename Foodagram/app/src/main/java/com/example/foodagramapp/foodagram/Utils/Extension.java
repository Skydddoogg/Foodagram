package com.example.foodagramapp.foodagram.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class Extension extends Fragment {

    public static void toast(FragmentActivity act, String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }
}


