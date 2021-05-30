package com.cosmic.firebaseauthentication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.cosmic.firebaseauthentication.R;
import com.cosmic.firebaseauthentication.adapter.EnemyAdapter;
import com.cosmic.firebaseauthentication.auth.LoginActivity;
import com.cosmic.firebaseauthentication.loader.EnemyLoaderListener;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.utils.RecyclerItemClick;
import com.cosmic.firebaseauthentication.viewmodel.EnemyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EnemyLoaderListener {

    private static final String TAG = "MainActivity";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String currentUserID, enemy_record_key;
    private EnemyViewModel enemyViewModel;
    private ArrayList<Enemy> enemy_list = new ArrayList<>();
    private EnemyAdapter enemyAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this method should always be called first before any other actions are performed
        checkAuthenticationStatus();

        //performs function associated with floating action button
        //calls activity transition and passes the activity AddEnemyActivity
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> activityTransition(AddEnemyActivity.class));

        //initialize the view model
        enemyViewModel = new ViewModelProvider(this).get(EnemyViewModel.class);
        enemyViewModel.getEnemyViewModel(this);

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        enemyAdapter = new EnemyAdapter(MainActivity.this, enemyViewModel.getEnemies().getValue());
        recyclerView.setAdapter(enemyAdapter);

        recyclerTouchListener();
    }

    //get position with RecyclerItemClick helper class
    //in our case, we want the key of the record of the child view we have selected
    private void recyclerTouchListener(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClick(this, new RecyclerItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                enemy_record_key = enemy_list.get(position).getPost_key();
                activityTransition(ViewEnemyActivity.class);
            }
        }));
    }

    @SuppressWarnings("rawtypes")
    private void activityTransition(Class activity){
        Intent intent = new Intent(this, activity);
        if (activity.equals(ViewEnemyActivity.class)){
            intent.putExtra("enemy_record", enemy_record_key);
        }
        startActivity(intent);
        if (activity.equals(LoginActivity.class)){
            finish();
        }
        Animatoo.animateSlideLeft(this);
    }

    //is the user logged in? get the current user. if that user is null, switch to the login activity. else, assign a
    //value to the currentUserID variable
    private void checkAuthenticationStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            activityTransition(LoginActivity.class);
        }else{
            currentUserID = firebaseUser.getUid();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    //this is the override method related to our interface 'EnemyLoaderListener' this is where we listen to and observer changes made on our firebase backend
    @Override
    public void onEnemiesLoaded() {
        enemyViewModel.getEnemies().observe(this, new Observer<ArrayList<Enemy>>() {
            @Override
            public void onChanged(ArrayList<Enemy> enemies) {
                Log.d(TAG, "onChanged: enemies: " + enemies);
                enemy_list = enemies;
                enemyAdapter.notifyDataSetChanged();
            }
        });
    }
}