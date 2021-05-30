package com.cosmic.firebaseauthentication.firebase;

import com.cosmic.firebaseauthentication.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void insertNewUser(User user){
        databaseReference.child("users").child(user.getUid()).setValue(user);
    }
}
