package com.cosmic.firebaseauthentication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.cosmic.firebaseauthentication.R;
import com.cosmic.firebaseauthentication.firebase.SaveEnemy;
import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.models.EnemyPhoto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class AddEnemyActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private EditText etName, etAlias, etReason;
    private Button buttonAddPic;
    private Button buttonChangePic;
    private Bitmap bitmap;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_enemy);

        instantiateUI();
    }

    private void instantiateUI(){
        imageView = findViewById(R.id.enemy_image);
        etName = findViewById(R.id.etName);
        etAlias = findViewById(R.id.etAlias);
        etReason = findViewById(R.id.etReason);
        buttonAddPic = findViewById(R.id.addImageButton);
        buttonChangePic = findViewById(R.id.changeImageButton);
        buttonChangePic.setVisibility(View.INVISIBLE);
        buttonAddPic.setOnClickListener(this);
        buttonChangePic.setOnClickListener(this);
        Button saveEnemyButton = findViewById(R.id.saveEnemyButton);
        saveEnemyButton.setOnClickListener(v -> uploadImageToFirebase());
    }

    private void saveEnemy(Enemy enemy){
        enemy.setName(etName.getText().toString().trim());
        enemy.setAlias(etAlias.getText().toString().trim());
        enemy.setReason(etReason.getText().toString().trim());
        enemy.setDate_added(currentDate());
        enemy.setStatus("WANTED");
        enemy.setTimestamp(getTimestamp(currentDate()));
        enemy.setDanger_level("MEDIUM");
        enemy.setLast_known_location("Unknown");
        enemy.setPriority("MEDIUM");

        EnemyPhoto enemyPhoto = new EnemyPhoto(enemy.getImage_url(), enemy.getImage_filename(), "", "");
        SaveEnemy saveEnemy = new SaveEnemy(this);
        saveEnemy.insertEnemy(enemy);
        saveEnemy.insertEnemyPhotos(enemyPhoto);
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
        uploadTask.addOnFailureListener(Throwable::printStackTrace);
        uploadTask.addOnProgressListener(snapshot -> {

        });
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.child(file).getDownloadUrl().addOnSuccessListener(uri -> {
            url = uri.toString();
            Enemy enemy = new Enemy("", url, "", "", "", "", file, "","", "", "", 1);
            saveEnemy(enemy);
        }));
    }

    private String currentDate(){
        return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date());
    }

    private long getTimestamp(String date){
        Date d;
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyy", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            d = simpleDateFormat.parse(date);
            assert d != null;
            return d.getTime();
        }catch (ParseException e){
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
        if (buttonAddPic.getVisibility() == View.VISIBLE){
            buttonAddPic.setVisibility(View.INVISIBLE);
            buttonChangePic.setVisibility(View.VISIBLE);
        }
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
                        imageView.setImageBitmap(bitmap);
                        inputStream.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSlideLeft(this);
    }
}
