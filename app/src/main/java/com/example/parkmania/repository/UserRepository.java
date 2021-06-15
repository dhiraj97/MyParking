package com.example.parkmania.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.parkmania.SignInActivity;
import com.example.parkmania.models.Users;
import com.example.parkmania.session_manager.Session;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final String TAG  = this.getClass().getCanonicalName();
    private final FirebaseFirestore db;
    private final String COLLECTION_NAME = "Users";

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void addUser(Users user, Context context){
        try{
            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getName());
            data.put("email", user.getEmail());
            data.put("phone", user.getPhone());
            data.put("plateNumber", user.getPlateNumber());

            db.collection(COLLECTION_NAME)
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Document added successfully " + documentReference.getId());
                            Session session = new Session(context);
//                            session.setValues(documentReference.getId());
                            Log.d(TAG, "onSuccess: "+ session.getUserId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Error creating document on Firestore" + e.getLocalizedMessage() );
                        }
                    });

        }catch(Exception ex){
            Log.e(TAG, "addUser: " + ex.getLocalizedMessage() );
        }
    }
}
