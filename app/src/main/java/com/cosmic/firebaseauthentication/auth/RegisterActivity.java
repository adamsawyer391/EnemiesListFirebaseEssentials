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
import com.cosmic.firebaseauthentication.firebase.RegisterUser;
import com.cosmic.firebaseauthentication.models.User;
import com.cosmic.firebaseauthentication.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private EditText etEmail, etPassword, etConfirmPassword;
    private Button registerButton;
    private TextView loginLink;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        instantiateViews();
        manageClickViews();
    }

    private void instantiateViews(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etPasswordConfirm);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
    }

    //attach the on click listeners to the create user button and to the login textview
    private void manageClickViews(){
        registerButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }

    //provide for tranistion from this activity to another
    @SuppressWarnings("rawtypes")
    private void activityTransition(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    /**
     * Each if statements checks to see if a certain condition is valid. If not, we return false and
     * further user action is needed. We check for a standard set of inputs:
     * --are all the fields fileld out
     * --does the password and confirm password match?
     * --is the password longer the six characters (is it a strong password)
     * --is there a lower case letter
     * --is there an upper case letter?
     * --is there a number?
     * --is there a special character?
     * If all these conditions are met, we return true
     */
    private boolean validateInputs(){
        RegisterHelperMethods registerHelperMethods = new RegisterHelperMethods();
        String message;
        mEmail = etEmail.getText().toString();
        mPassword = etPassword.getText().toString();
        String mConfirmPassword = etConfirmPassword.getText().toString();
        if (mEmail.equals("") || mPassword.equals("") || mConfirmPassword.equals("")){
            message = getString(R.string.please_fill_out_all_fields);
            displayToast(message);
            return false;
        }
        if (!mPassword.equals(mConfirmPassword)){
            message = getString(R.string.unmatched_passwords);
            displayToast(message);
            return false;
        }
        if (mPassword.length() < 6){
            message = getString(R.string.password_length);
            displayToast(message);
            return false;
        }
        if (!registerHelperMethods.containsLowerCase(mPassword)){
            message = getString(R.string.password_lowercase);
            displayToast(message);
            return false;
        }
        if (!registerHelperMethods.containsUpperCase(mPassword)){
            message = getString(R.string.password_uppercase);
            displayToast(message);
            return false;
        }
        if (!registerHelperMethods.containsNumber(mPassword)){
            message = getString(R.string.password_number);
            displayToast(message);
            return false;
        }
        if (!registerHelperMethods.containsSpecialCharacter(mPassword)){
            message = getString(R.string.password_special_character);
            displayToast(message);
            return false;
        }
        return true;
    }

    //is this user email already in use. If so, they must select a different email
    private void checkIfEmailIsInUse(String email){
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult().getSignInMethods()).size() == 1){
                displayToast(getString(R.string.email_in_use));
            }else{
                createNewUser();
            }
        });
    }

    //we create the user in the authentication table and then insert the user into the realtime database
    private void createNewUser(){
        firebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                User user = new User("01/01/1900", "First ", "Name", currentUserID, mEmail, "01/01/1900");
                RegisterUser registerUser = new RegisterUser();
                registerUser.insertNewUser(user);
                activityTransition(MainActivity.class);
            }else{
                displayToast(getString(R.string.error_default));
            }
        });
    }

    private void displayToast(String message){
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, message, duration).show();
    }

    //View.OnClickListener override implemented method
    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()){
            case "button":
                if (validateInputs()){
                    checkIfEmailIsInUse(mEmail);
                }
                break;
            case "login":
                activityTransition(LoginActivity.class);
                break;
        }
    }
}
