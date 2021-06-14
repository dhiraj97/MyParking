package com.example.parkmania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkmania.models.Users;
import com.example.parkmania.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtPhone, edtPlateNumber;
    private Button btnSignUp, btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private String userID;
    private Users newUser;
    private UserViewModel userViewModel;
    private String email,pass,plateNumber,phone,name;
    private final String TAG = this.getClass().getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
        
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();

            }
        });


    }

    private void createNewUser() {
        email = edtEmail.getText().toString();
        pass = edtPassword.getText().toString();
        name = edtName.getText().toString();
        phone = edtPhone.getText().toString();
        plateNumber = edtPlateNumber.getText().toString();

        if(TextUtils.isEmpty(name)) {
            edtName.setError("Name Cannot be empty!!");
            return;
        }
        if(TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone Cannot be empty!!");
            return;
        }
        if(TextUtils.isEmpty(plateNumber)) {
            edtPlateNumber.setError("Plate Number Cannot be empty!!");
            return;
        }
        if(pass.length() < 6) {
            edtPassword.setError("Password must be >=6 charachters");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid Email!!");
            return;
        }

        //Checking if email is not blank and regex for email
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(!pass.isEmpty()){
                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    //Create an object of User and adding to userView Model
                                    saveDataToDB();
                                    Toast.makeText(SignUpActivity.this,"User Created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));

                                } else {
                                    Toast.makeText(SignUpActivity.this,"Sign Up Failed!!", Toast.LENGTH_SHORT).show();
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

    private void saveDataToDB() {

        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPassword(pass);
        newUser.setPlateNumber(plateNumber);

        Log.d(TAG, "saveDataToDB newUser: "+newUser.toString());

        userViewModel.addUser(newUser);
    }

    private void initialize() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtPlateNumber = findViewById(R.id.edtPlateNumber);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        userViewModel =UserViewModel.getInstance(getApplication());

        newUser = new Users();

        // Getting instance of firebase auth
        mAuth = FirebaseAuth.getInstance();
    }

}