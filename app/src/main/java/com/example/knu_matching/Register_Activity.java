package com.example.knu_matching;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private EditText edt_Email, edt_password;
    private Button btn_finish, btn_certificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("knu_Matching");

        edt_Email = findViewById(R.id.edt_Email);
        edt_password = findViewById(R.id.edt_Password);
        btn_finish = findViewById(R.id.btn_registerButton);
        btn_certificate = findViewById(R.id.btn_knuID);

        btn_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoveActivity();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = edt_Email.getText().toString();
                String strPassword = edt_password.getText().toString();
