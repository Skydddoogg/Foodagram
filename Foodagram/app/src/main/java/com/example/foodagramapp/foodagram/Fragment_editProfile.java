package com.example.foodagramapp.foodagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Fragment_editProfile extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        EditText nameField = (EditText) getView().findViewById(R.id.edit_profile_name);
        EditText usernameField = (EditText) getView().findViewById(R.id.edit_profile_username);
        EditText descriptionField = (EditText) getView().findViewById(R.id.edit_profile_description);
        EditText emailField = (EditText) getView().findViewById(R.id.edit_profile_email);
        EditText sexField = (EditText) getView().findViewById(R.id.edit_profile_sex);
        EditText birthDate = (EditText) getView().findViewById(R.id.edit_profile_birth_date);
        TextView saveBtn = (TextView) getView().findViewById(R.id.edit_profile_save);
        TextView backBtn = (TextView) getView().findViewById(R.id.edit_profile_back);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_profile())
                        .commit();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


}
