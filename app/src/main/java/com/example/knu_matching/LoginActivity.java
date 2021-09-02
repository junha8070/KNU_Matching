package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private Button btn_login, btn_IdFind, btn_PwdFind, btn_Register;
    private EditText edt_Email, edt_Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");

        btn_login = findViewById(R.id.btn_login);
        btn_IdFind = findViewById(R.id.btn_IdFind);
        btn_PwdFind = findViewById(R.id.btn_PwdFind);
        btn_Register = findViewById(R.id.btn_Register);

        edt_Email = findViewById(R.id.edt_email);
        edt_Pwd = findViewById(R.id.edt_pwd);

        btn_PwdFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_IdFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindIDActivity.class);
                startActivity(intent);
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String edtEmail = edt_Email.getText().toString();
                String edtPassword = edt_Pwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(edtEmail,edtPassword).addOnCompleteListener
                        (LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this, "이메일이나 비밀번호 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}