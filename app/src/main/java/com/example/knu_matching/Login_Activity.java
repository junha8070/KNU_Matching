package com.example.knu_matching;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Login_Activity extends AppCompatActivity {

    private Button btn_login, btn_IdFind, btn_PwdFind;
    private EditText edt_Email, edt_Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_IdFind = findViewById(R.id.btn_IdFind);
        btn_PwdFind = findViewById(R.id.btn_PwdFind);

        edt_Email = findViewById(R.id.edt_Email);
        edt_Pwd = findViewById(R.id.edt_pwd);


    }
}