package com.example.projectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    FirebaseAuth mAuth;
    private TextView text_sign_up_screen;
    private EditText edit_email, edit_password;
    private Button bt_sign_in;
    private ProgressBar progressBar;
    String[] messages = {"Fill all fields"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        StartComponents();

        text_sign_up_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormSignUp.class);
                startActivity(intent);
            }
        });

        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, messages[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else{
                    UserAuthenticate(view);
                }
            }
        });
    }

    private void UserAuthenticate(View view) {
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainScreen();
                        }
                    }, 3000);
                } else {
                    String error;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Invalid email or password.";
                    }
                    catch (Exception e) {
                        error = "Error to log in.";
                    }
                    Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser userCurrent = mAuth.getCurrentUser();

        if (userCurrent != null) {
            MainScreen();
        }
    }

    private void MainScreen() {
        Intent intent = new Intent(FormLogin.this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    private void StartComponents() {
        text_sign_up_screen = findViewById(R.id.text_sign_up);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        bt_sign_in = findViewById(R.id.bt_log_ing);
        progressBar = findViewById(R.id.progress_bar);
    }
}