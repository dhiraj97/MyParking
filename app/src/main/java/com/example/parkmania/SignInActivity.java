package com.example.parkmania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignUp, btnSignIn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initialize();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void initialize() {
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginUser() {
        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();

        //Checking if email is not blank and regex for email
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(!pass.isEmpty()){
               mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                           transitionToHome();
                           Toast.makeText(SignInActivity.this,"Sign In Successful!", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(SignInActivity.this,"Sign In Failed!", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            } else {
                edtPassword.setError("Cannot Leave Password Empty!");
            }
        } else if(email.isEmpty()) {
            edtEmail.setError("Cannot Leave Field Empty!");
        } else {
            edtEmail.setError("Use the right email pattern!");
        }
    }

    private void transitionToHome() {
        startActivity(new Intent(this, HomeActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            transitionToHome();
        }
    }
}