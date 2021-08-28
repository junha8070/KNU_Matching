package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("knu_Matching");

        edt_Email = findViewById(R.id.edt_Email);
        edt_password = findViewById(R.id.edt_Password);
        btn_finish = findViewById(R.id.btn_registerButton);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = edt_Email.getText().toString();
                String strPassword = edt_password.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener
                        (Register_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    UserAccount account = new UserAccount();
                                    account.setIdToken(firebaseUser.getUid());
                                    account.setEmailId(firebaseUser.getEmail());
                                    account.setPassword(strPassword);

                                    mDatabaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                    Toast.makeText(Register_Activity.this, "성공",Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Register_Activity.this, "실패",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }
}