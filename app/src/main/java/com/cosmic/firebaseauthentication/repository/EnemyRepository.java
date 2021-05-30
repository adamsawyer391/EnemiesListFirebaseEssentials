package com.cosmic.firebaseauthentication.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cosmic.firebaseauthentication.loader.EnemyLoaderListener;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EnemyRepository {

    public static Context mContext;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    public static EnemyLoaderListener enemyLoaderListener;
    public static EnemyRepository enemyRepository;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String currentUserID = firebaseAuth.getCurrentUser().getUid();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static EnemyRepository getInstance(Context context) {
        mContext = context;
        if (enemyRepository == null){
            enemyRepository = new EnemyRepository();
        }
        enemyLoaderListener = (EnemyLoaderListener) mContext;
        return enemyRepository;
    }

    public MutableLiveData<ArrayList<Enemy>> getEnemies(){
        loadEnemies();
        MutableLiveData<ArrayList<Enemy>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(enemies);
        return mutableLiveData;
    }

    private void loadEnemies(){
        databaseReference.child("enemies").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enemies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    enemies.add(dataSnapshot.getValue(Enemy.class));
                }
                enemyLoaderListener.onEnemiesLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
