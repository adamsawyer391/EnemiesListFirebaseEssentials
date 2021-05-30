package com.cosmic.firebaseauthentication.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cosmic.firebaseauthentication.loader.SingleEnemyLoaderListener;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleEnemyRepository {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String currentUserID = firebaseAuth.getCurrentUser().getUid();

    public static Context mContext;
    static SingleEnemyRepository singleEnemyRepository;
    public static SingleEnemyLoaderListener singleEnemyLoaderListener;

    public static String enemy_id;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public static SingleEnemyRepository getInstance(Context context, String id){
        mContext = context;
        enemy_id = id;
        if (singleEnemyRepository == null){
            singleEnemyRepository = new SingleEnemyRepository();
        }
        singleEnemyLoaderListener = (SingleEnemyLoaderListener) mContext;
        return singleEnemyRepository;
    }

    public MutableLiveData<ArrayList<Enemy>> getEnemy(){
        loadEnemy();
        MutableLiveData<ArrayList<Enemy>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(enemies);
        return mutableLiveData;
    }

    private void loadEnemy(){
        databaseReference.child("enemies").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enemies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    if (key.equals(enemy_id)){
                        enemies.add(dataSnapshot.getValue(Enemy.class));
                    }
                }
                singleEnemyLoaderListener.onSingleEnemyLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
