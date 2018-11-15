package com.example.foodagramapp.foodagram.Firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.foodagramapp.foodagram.Dialog.CustomLoadingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.UUID;

public class FirebaseStorageAide{

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private CustomLoadingDialog customLoadingDialog;
    private String TAG = "FirebaseStorageAide";

    public FirebaseStorageAide(Context context){
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference();
        this.customLoadingDialog = new CustomLoadingDialog(context);
    }

    public String uploadImage(Bitmap bitmap, String destination_directory){
        customLoadingDialog.showDialog();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String refMessage = destination_directory + "/" + UUID.randomUUID().toString() + timestamp.toString() + ".jpg";
        StorageReference ref = this.storageRef.child(refMessage);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        customLoadingDialog.dismissDialog();
                        Log.d(TAG, "UPLOADED");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customLoadingDialog.dismissDialog();
                        Log.d(TAG, "FAILED TO UPLOAD AN IMAGE");
                    }
                });

        return refMessage;

    }


}
