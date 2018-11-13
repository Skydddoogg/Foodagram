package com.example.foodagramapp.foodagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodagramapp.foodagram.Post.PostActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button loginButton;
    private TextView forgetPassword;
    private TextView noAccount;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        email = getView().findViewById(R.id.login_username);
        password = getView().findViewById(R.id.login_password);
        loginButton = getView().findViewById(R.id.login_login_button);
        forgetPassword = getView().findViewById(R.id.login_text_forgot_password);
        noAccount = getView().findViewById(R.id.login_text_click_here);

        checkCurrentLogin();

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isEmptyInput(email, password)){
                            loginToFirebase(email, password);
                        }
                    }
                }
        );

        forgetPassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reDirectToFragment(new ForgetPasswordFragment());
                    }
                }
        );

        noAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reDirectToFragment(new RegisterFragment());
                    }
                }
        );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    void checkCurrentLogin(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            reDirectToActivity();
        }
    }

    boolean isEmptyInput(EditText username, EditText password){
        boolean result;
        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "กรุณากรอก Email และ Password", Toast.LENGTH_SHORT).show();
            Log.i("LOGIN","EMPTY USERNAME/PASSWORD");
            result = true;
        }else{
            result = false;
        }

        return result;
    }

    void loginToFirebase(EditText email, EditText password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        if(user.isEmailVerified()){
                            reDirectToActivity();
                            Log.i("LOGIN", "GO TO FRAGMENT");
                        }else{
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getActivity(), "Please verify your Email.", Toast.LENGTH_SHORT).show();
                            Log.i("LOGIN", "PLEASE VERIFY EMAIL");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "รหัสผ่านไม่ถูกต้อง / ไม่มีผู้ใช้นี้ในระบบ", Toast.LENGTH_SHORT).show();
                Log.d("REGISTER","ERROR : " + e.getMessage());
            }
        });
    }

    void reDirectToFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, fragment)
                .addToBackStack(null)
                .commit();
    }

    void reDirectToActivity(){
        Intent mIntent = new Intent(this.getActivity(), PostActivity.class);
        startActivity(mIntent);
    }
}
