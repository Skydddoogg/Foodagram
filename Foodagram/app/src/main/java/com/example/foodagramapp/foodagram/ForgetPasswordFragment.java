package com.example.foodagramapp.foodagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordFragment extends Fragment {

    EditText emailForget;
    Button forgetPasswordButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emailForget = getView().findViewById(R.id.forget_password_email);
        forgetPasswordButton = getView().findViewById(R.id.forget_password_button);

        forgetPasswordButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isEmptyEmailInput(emailForget)){
                            sendEmailResetPassword(emailForget);
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    boolean isEmptyEmailInput(EditText email){
        if(email.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
            Log.i("FORGETPASSWORD", "EMPTY EMAIL");
            return true;
        }else{
            return false;
        }
    }

    void sendEmailResetPassword(EditText email){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "กรุณาคลิกลิ้งค์ที่อีเมล เพื่อรีเซ็ตพาสเวิร์ด", Toast.LENGTH_SHORT).show();
                            Log.i("FORGETPASSWORD", "MAIL HAVE BEEN SENT");
                        }else{
                            Toast.makeText(getActivity(), "ผู้ใช้นี้ไม่มีในระบบ", Toast.LENGTH_SHORT).show();
                            Log.i("FORGETPASSWORD", "EMAIL DOESN'T EXIST");
                        }
                    }
                });
    }
}
