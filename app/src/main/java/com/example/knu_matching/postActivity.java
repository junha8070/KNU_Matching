package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.knu_matching.main.MainActivity;
import com.example.knu_matching.membermanage.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class postActivity extends AppCompatActivity {
    private Button btn_write;
    private EditText edt_Title, edt_Number, edt_date, edt_post;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private String str_Title, str_date, str_Number, str_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btn_write = findViewById(R.id.btn_write);
        edt_date = findViewById(R.id.edt_date);
        edt_Title = findViewById(R.id.edt_Title);
        edt_Number = findViewById(R.id.edt_Number);
        edt_post = findViewById(R.id.edt_post);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");
        mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mProfieDatabaseReference = mDatabaseRef.child("UserAccount");

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_Title = edt_Title.getText().toString();
                str_date = edt_date.getText().toString();
                str_Number = edt_Number.getText().toString();
                str_post = edt_post.getText().toString();
                //System.out.println("test" + strNick + " " + strEmail + " " + strStudentId);

                if (str_Title.trim().equals("") || str_date.trim().equals("") || str_Number.trim().equals("") || str_post.trim().equals("")) {
                    Toast.makeText(postActivity.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
//                        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(postActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//                                    UserAccount account = new UserAccount();
//                                    account.setIdToken(firebaseUser.getUid());
//                                    account.setEmailId(firebaseUser.getEmail());
//                                    account.setStudentId(strStudentId);
//                                    account.setMajor(strMaojr);
//                                    account.setNickName(strNick);
//                                    account.setPassword(strPassword);
//                                    mProfieDatabaseReference.child(firebaseUser.getEmail().replace(".", ">")).setValue(account);
//                                    Toast.makeText(postActivity.this, "성공", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(postActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                } else {
//                                    Toast.makeText(postActivity.this, "실패", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                }
            }
        });
    }
}