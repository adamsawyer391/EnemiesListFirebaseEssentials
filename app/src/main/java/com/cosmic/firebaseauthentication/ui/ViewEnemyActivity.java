package com.cosmic.firebaseauthentication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cosmic.firebaseauthentication.R;
import com.cosmic.firebaseauthentication.adapter.EnemyImageAdapter;
import com.cosmic.firebaseauthentication.firebase.SaveEnemy;
import com.cosmic.firebaseauthentication.loader.EnemyImageLoaded;
import com.cosmic.firebaseauthentication.loader.SingleEnemyLoaderListener;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.models.EnemyPhoto;
import com.cosmic.firebaseauthentication.viewmodel.EnemyImageViewModels;
import com.cosmic.firebaseauthentication.viewmodel.SingleEnemyViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ViewEnemyActivity extends AppCompatActivity implements EnemyImageLoaded, SingleEnemyLoaderListener {

    private String passedEnemyRecordKey;
    private EnemyImageViewModels enemyImageViewModels;
    private SingleEnemyViewModel singleEnemyViewModel;
    private RecyclerView recyclerView;
    private EnemyImageAdapter enemyImageAdapter;
    private ArrayList<EnemyPhoto> enemy_photos;
    private Enemy enemy = new Enemy();

    private TextView tvName, tvReason, tvDate, tvAlias, tvPriority, tvDangerLevel, tvStatus, title;
    private ImageView add_image;
    private Bitmap bitmap;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String currentUserID = firebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enemy);

        getIntentExtras();
    }

    //wherein we receive the intent extra from main activity
    //it will be the firebase record key for our Enemy object.
    private void getIntentExtras(){
        Intent intent = getIntent();
        if (intent != null){
            passedEnemyRecordKey = intent.getExtras().getString("enemy_record");
            connectToEnemyImageViewModel();
            connectToEnemyViewModel();
            instantiateUI();
        }
    }

    private void instantiateUI(){
        tvName = findViewById(R.id.tvName);
        tvReason = findViewById(R.id.tvReason);
        tvDate = findViewById(R.id.tvDate);
        tvAlias = findViewById(R.id.tvAlias);
        tvPriority = findViewById(R.id.tvPriority);
        tvDangerLevel = findViewById(R.id.tvDangerLevel);
        tvStatus = findViewById(R.id.tvStatus);
        title = findViewById(R.id.title);
        add_image = findViewById(R.id.add_image);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void connectToEnemyImageViewModel(){
        enemyImageViewModels = new ViewModelProvider(this).get(EnemyImageViewModels.class);
        enemyImageViewModels.enemyImageViewModel(this, passedEnemyRecordKey);
    }

    private void connectToEnemyViewModel(){
        singleEnemyViewModel = new ViewModelProvider(this).get(SingleEnemyViewModel.class);
        singleEnemyViewModel.connectToSingleEnemyViewModel(this, passedEnemyRecordKey);
    }

    private void setupRecyclerview(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        enemyImageAdapter = new EnemyImageAdapter(this, enemy_photos);
        recyclerView.setAdapter(enemyImageAdapter);
    }

    private void populateUI(){
        tvName.setText(enemy.getName());
        tvReason.setText(enemy.getReason());
        tvDate.setText(enemy.getDate_added());
        tvAlias.setText(enemy.getAlias());
        tvDangerLevel.setText(enemy.getDanger_level());
        tvPriority.setText(enemy.getPriority());
        tvStatus.setText(enemy.getStatus());
        title.setText(enemy.getName());
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                if (data != null){
                    Uri uri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        uploadImageToFirebase();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void uploadImageToFirebase(){
        //basic parameters for storage reference: the path and the file key
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String file = UUID.randomUUID().toString();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(currentUserID);

        //byte array output stream to turn bitmap into a byte array output stream
        //the upload task sends the bitmap for firebase storage as a stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        //the upload task. the process of transferring a bitmap stream to firebase storage
        //add an on success, on failure and on progress listener
        UploadTask uploadTask = storageReference.child(file).putBytes(bytes);
        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
        uploadTask.addOnProgressListener(snapshot -> {

        });
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.child(file).getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            String image_key = databaseReference.push().getKey();
            EnemyPhoto enemyPhoto = new EnemyPhoto(url, file, passedEnemyRecordKey, image_key);
            databaseReference.child("images").child(currentUserID).child(passedEnemyRecordKey).child(image_key).setValue(enemyPhoto);
            enemyImageAdapter.notifyDataSetChanged();
        }));
    }

    @Override
    public void onImageLoaded() {
        enemyImageViewModels.getPhotos().observe(this, new Observer<ArrayList<EnemyPhoto>>() {
            @Override
            public void onChanged(ArrayList<EnemyPhoto> enemyPhotos) {
                enemy_photos = enemyPhotos;
                setupRecyclerview();
                enemyImageAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSingleEnemyLoaded() {
        singleEnemyViewModel.getEnemy().observe(this, new Observer<ArrayList<Enemy>>() {
            @Override
            public void onChanged(ArrayList<Enemy> enemies) {
                enemy = enemies.get(0);
                populateUI();
            }
        });
    }
}
