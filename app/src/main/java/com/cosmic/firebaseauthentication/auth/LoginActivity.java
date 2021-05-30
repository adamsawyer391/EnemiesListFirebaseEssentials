package com.cosmic.firebaseauthentication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.cosmic.firebaseauthentication.R;
import com.cosmic.firebaseauthentication.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView registerLink, forgotPassword;
    private EditText etEmail, etPassword;
    private Button loginButton;
    private String mEmail, mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instantiateViews();
        manageClickViews();
    }

    private void instantiateViews(){
        registerLink = findViewById(R.id.registerLink);
        forgotPassword = findViewById(R.id.forgotPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginButton);
    }

    private void manageClickViews(){
        loginButton.setOnClickListener(this);
        registerLink.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    private void signIn(){
        firebaseAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                activityTransition(MainActivity.class);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private void activityTransition(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    private boolean validateInputs(){
        mEmail = etEmail.getText().toString();
        mPassword = etPassword.getText().toString();
        if (mEmail.equals("") || mPassword.equals("")){
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()){
            case "button":
                if (validateInputs()){
                    signIn();
                }
                break;
            case "forgot":
                activityTransition(ForgotPasswordActivity.class);
                break;
            case "register":
                activityTransition(RegisterActivity.class);
                break;
        }
    }
}
