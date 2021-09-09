package com.example.knu_matching.membermanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_send, btn_Cancel;
    private EditText edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        mAuth = FirebaseAuth.getInstance();
        btn_Cancel = findViewById(R.id.btn_Cancel);
        btn_send = findViewById(R.id.btn_send);
        edt_email = findViewById(R.id.edt_email);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });


    }

    private void send(){
        String email = edt_email.getText().toString();

        if(email.length() > 0 ){
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(FindPasswordActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(FindPasswordActivity.this, "이메일을 실패.", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }else {
            Toast.makeText(FindPasswordActivity.this, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }
}