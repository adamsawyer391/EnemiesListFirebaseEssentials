package com.cosmic.firebaseauthentication.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cosmic.firebaseauthentication.loader.EnemyImageLoaded;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.models.EnemyPhoto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EnemyImageRepository {

    private static final String TAG = "EnemyImageRepository";

    public static Context mContext;
    public static EnemyImageLoaded enemyImageLoaded;
    public static EnemyImageRepository enemyImageRepositoryInstance;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String currentUserID = firebaseAuth.getCurrentUser().getUid();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private ArrayList<EnemyPhoto> enemyPhotos = new ArrayList<>();
    public static String record_key;

    public static EnemyImageRepository getInstance(Context context, String post_id){
        mContext = context;
        record_key = post_id;
        if (enemyImageRepositoryInstance == null){
            enemyImageRepositoryInstance = new EnemyImageRepository();
        }
        enemyImageLoaded = (EnemyImageLoaded) mContext;
        return enemyImageRepositoryInstance;
    }

    public MutableLiveData<ArrayList<EnemyPhoto>> getImages(){
        loadImages();
        MutableLiveData<ArrayList<EnemyPhoto>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(enemyPhotos);
        return mutableLiveData;
    }

    private void loadImages(){
        databaseReference.child("images").child(currentUserID).child(record_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enemyPhotos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    enemyPhotos.add(dataSnapshot.getValue(EnemyPhoto.class));
                    Log.d(TAG, "onDataChange: enemy photos : " + enemyPhotos);
                }
                enemyImageLoaded.onImageLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
