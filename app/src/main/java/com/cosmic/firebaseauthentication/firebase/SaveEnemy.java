package com.cosmic.firebaseauthentication.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.models.EnemyPhoto;
import com.cosmic.firebaseauthentication.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SaveEnemy {

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    private final String enemy_key = databaseReference.push().getKey();
    private final Context context;

    public SaveEnemy(Context context) {
        this.context = context;
    }

    public void insertEnemy(Enemy enemy){
        if (enemy_key != null) {
            enemy.setPost_key(enemy_key);
            databaseReference.child("enemies").child(currentUserID).child(enemy_key).setValue(enemy).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Enemy Added!", Toast.LENGTH_SHORT).show();
                    returnToMainActivity();
                }else{
                    Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void insertEnemyPhotos(EnemyPhoto enemyPhoto){
        String image_key = databaseReference.push().getKey();
        enemyPhoto.setPost_key(enemy_key);
        enemyPhoto.setImage_key(image_key);
        databaseReference.child("images").child(currentUserID).child(enemy_key).child(image_key).setValue(enemyPhoto);
    }

    private void returnToMainActivity(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Animatoo.animateSlideLeft(context);
        Activity activity = (Activity) context;
        activity.finish();
    }
}
