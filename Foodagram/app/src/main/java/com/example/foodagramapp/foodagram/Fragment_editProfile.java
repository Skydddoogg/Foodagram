package com.example.foodagramapp.foodagram;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.*;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_editProfile extends Fragment {
private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    String monthStr;
    String dayStr;
    EditText nameField;
    EditText usernameField;
    EditText descriptionField;
    EditText emailField;
    EditText sexField;
    EditText birthDateField;
    TextView headerUsernameField;
    int mYear,mMonth,mDay;
    private String[] sex;
    // Write a message to the database






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sex = new String[]{"ชาย", "หญิง"};

        initUIComponent();
        initSexField();
        initBirthDateField();
        initSaveBtn();
        initBackBtn();

        myRef = database.getReference().child("profile");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.cl    ass);
                for(DataSnapshot db : dataSnapshot.getChildren()){

                    //** Test mock username
                        if(db.child("username").getValue().equals("SkyDogg")) {
                        DataSnapshot dbProfile = db.child("username");

                        String name = (String) db.child("name").getValue();
                        String username = (String) db.child("username").getValue();
                        String description  = (String) db.child("vitae").getValue();
                        String email = (String) db.child("email").getValue();
                        String sex = (String) db.child("sex").getValue();
                        String birthDate = (String) db.child("dob").getValue();
//                        writeNewUser(name, username, description, email, sex, birthDate, db);
                        nameField.setHint(name);
                        usernameField.setHint(username);
                        descriptionField.setHint(description);

                        sexField.setHint(sex);
                        birthDateField.setHint(birthDate);
                        headerUsernameField.setText(username);


                    }
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
//    private void writeNewUser(String name, String userId, String description, String email, String sex, String birthDate, DataSnapshot db) {
//        Model_editProfile editedProfile = new Model_editProfile(name, userId, description, email, sex, birthDate);
//        Log.i("Write", "Write success" );
//
//
//
//    }

    public void initUIComponent(){
        nameField = (EditText) getView().findViewById(R.id.edit_profile_name);
        usernameField = (EditText) getView().findViewById(R.id.edit_profile_username);
        descriptionField = (EditText) getView().findViewById(R.id.edit_profile_description);


        headerUsernameField = (TextView) getView().findViewById(R.id.edit_profile_header_username);

    }

    public void initSexField(){
        sexField = (EditText) getView().findViewById(R.id.edit_profile_sex);
        sexField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSexpickerDialog();

            }
        });

        sexField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    initSexpickerDialog();

                }
            }
        });

    }
    public void initBirthDateField(){
        birthDateField = (EditText) getView().findViewById(R.id.edit_profile_birth_date);
        birthDateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                datePickerPopup(birthDateField);
                birthDateField.setShowSoftInputOnFocus(false);
            }
        });
        birthDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    datePickerPopup(birthDateField);
                    birthDateField.setShowSoftInputOnFocus(false);
                }
            }
        });


    }

    public void initSexpickerDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());
        builder.setTitle("เลือกเพศของคุณ");
        builder.setItems(sex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = sex[which];
                sexField.setHint(selected);
            }
        });

        builder.create();

// สุดท้ายอย่าลืม show() ด้วย
        builder.show();
        sexField.setShowSoftInputOnFocus(false);

    }
    public void initSaveBtn(){
        TextView saveBtn = (TextView) getView().findViewById(R.id.edit_profile_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //** Test save value mock
                myRef.child("test_user_id_1").child("username").setValue(usernameField.getText().toString());
                myRef.child("test_user_id_1").child("name").setValue(nameField.getText().toString());
                myRef.child("test_user_id_1").child("vitae").setValue(descriptionField.getText().toString());
                myRef.child("test_user_id_1").child("sex").setValue(sexField.getText().toString());
                myRef.child("test_user_id_1").child("bod").setValue(birthDateField.getText().toString());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_profile())
                        .commit();
            }
        });
    }
    public void initBackBtn(){
        TextView backBtn = (TextView) getView().findViewById(R.id.edit_profile_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new Fragment_profile())
                        .commit();
            }
        });

    }
    public void datePickerPopup(final EditText field){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                if(month < 10){
                    monthStr = "0" + (month + 1);
                }else{
                    monthStr = (month + 1) + "";
                }

                if(date < 10){
                    dayStr = "0" + date;

                }else{
                    dayStr = date  + "";
                }

                mYear = year;
                mMonth = month;
                mDay = date;
                field.setHint(year+"-"+monthStr+"-"+dayStr);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(0);


        datePickerDialog.show();
    }

}
