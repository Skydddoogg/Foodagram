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
import android.widget.TextView;
import android.widget.Toast;
import com.example.foodagramapp.foodagram.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    private EditText displayName;
    private EditText password;
    private EditText email;
    private Button registerButton;
    private TextView haveAccount;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayName = getView().findViewById(R.id.register_name);
        password = getView().findViewById(R.id.register_password);
        email = getView().findViewById(R.id.register_email);
        registerButton = getView().findViewById(R.id.register_register_button);
        haveAccount = getView().findViewById(R.id.register_text_click_here);

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isUsableInput(displayName, password, email)){
                            registerToFirebase(email, password, displayName);
                        }
                    }
                }
        );

        haveAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reDirectFragment(new LoginFragment());
                    }
                }
        );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public boolean isUsableInput(EditText displayName, EditText password, EditText email){
        boolean result;
        if(displayName.getText().toString().isEmpty()
                || password.getText().toString().isEmpty()
                || email.getText().toString().isEmpty()) {
            result = false;
            Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
            Log.i("REGISTER", "EMPTY DISPLAYNAME/PASSWORD/EMAIL");
        }else if(password.getText().toString().length() <6){
            result = false;
            Toast.makeText(getActivity(), "Password ต้องมีความยาวมากกว่า 6 ตัวอักษร", Toast.LENGTH_SHORT).show();
            Log.i("REGISTER", "PASSWORD MUST BE AT LEAST 6 CHARS");
        }else{
            result = true;
        }
        return result;
    }

    public void registerToFirebase(final EditText email, EditText password, final EditText displayName){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        sendVerifiedEmail(authResult.getUser());
                        FirebaseAuth.getInstance().signOut();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_view, new LoginFragment())
                                .addToBackStack(null)
                                .commit();

                        addUserDisplayName(displayName, email);
                        Toast.makeText(getActivity(), "การลงทะเบียนเสร็จสิ้น กรุณายืนยันตัวที่อีเมล", Toast.LENGTH_SHORT).show();
                        Log.i("REGISTER","REGISTER SUCCESS");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "การลงทะเบียนล้มเหลว", Toast.LENGTH_SHORT).show();
                Log.i("REGISTER","Error : " + e.getMessage());
            }

        });
    }

    void sendVerifiedEmail(FirebaseUser user){
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("REGISTER", "SENT VERIFY EMAIL");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("REGISTER", "Error : " + e.getMessage());
            }
        });
    }

    void addUserDisplayName(EditText displayName, EditText email){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User( displayName.getText().toString(), email.getText().toString(), "", "", "", "");
        mDatabase.child("profile").child(byteArrayToHexString(email)).setValue(user);
        Log.i("HASH", byteArrayToHexString(email));

    }

    String byteArrayToHexString(EditText email) {
        byte[] bytes = email.getText().toString().getBytes();
        String result = "";
        for (int i=0; i < bytes.length; i++) {
            result +=
                    Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    void reDirectFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, fragment)
                .addToBackStack(null)
                .commit();
    }

}
