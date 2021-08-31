package com.example.knu_matching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login_Activity extends AppCompatActivity {

    private Button btn_login, btn_IdFind, btn_PwdFind, btn_Register;
    private EditText edt_Email, edt_Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_IdFind = findViewById(R.id.btn_IdFind);
        btn_PwdFind = findViewById(R.id.btn_PwdFind);
        btn_Register = findViewById(R.id.btn_Register);

        edt_Email = findViewById(R.id.edt_Email);
        edt_Pwd = findViewById(R.id.edt_pwd);

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });

    }
}