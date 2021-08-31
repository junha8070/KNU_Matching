package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mRootDatabaseref;
    private EditText edt_Email, edt_password, edt_Nickname, edt_StudentID;
    private Button btn_finish, btn_check_nick;
    private String strID, strEmail, strPassword, strNick, nickName;
    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mRootDatabaseref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mProfieDatabaseReference = mRootDatabaseref.child("profile");
        DatabaseReference mNickNameDatabaseReference = mRootDatabaseref.child("nickNameList");

        edt_StudentID = findViewById(R.id.edt_StudentID);
        edt_Email = findViewById(R.id.edt_Email);
        edt_password = findViewById(R.id.edt_Password);
        edt_Nickname = findViewById(R.id.edt_nickname);
        btn_check_nick = findViewById(R.id.btn_check_nick);
        btn_finish = findViewById(R.id.btn_registerButton);

        btn_check_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName = edt_Nickname.getText().toString();
                mNickNameDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.haschild(nickList).getValue() != null) {
                            state = false;
                            System.out.println("test1"+nickName);

                            System.out.println("test"+snapshot);
                            Toast.makeText(RegisterActivity.this, "실패패패패", Toast.LENGTH_SHORT).show();
                        } else if (snapshot.child(nickName).getValue() == null) {
                            state = true;
                            Toast.makeText(RegisterActivity.this, "성공공공공", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strID = edt_StudentID.getText().toString();
                strEmail = edt_Email.getText().toString();
                strPassword = edt_password.getText().toString();
                strNick = edt_Nickname.getText().toString();
                if (strEmail.trim().equals("") || strPassword.trim().equals("") || strNick.trim().equals("") || strID.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "빈칸을 채워주세요:(", Toast.LENGTH_SHORT).show();
                } else {
                    if (state == false) {
                        Toast.makeText(RegisterActivity.this, "닉네임 중복여부를 확인해주세요:(", Toast.LENGTH_SHORT).show();
                    }
                    if (state == true) {
                        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    UserNickList nicklist = new UserNickList();
                                    UserAccount account = new UserAccount();
                                    nicklist.setNickname(strNick);
                                    account.setIdToken(firebaseUser.getUid());
                                    account.setEmailId(firebaseUser.getEmail());
                                    account.setNickName(strNick);
                                    account.setPassword(strPassword);
                                    mDatabaseref.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                    Toast.makeText(RegisterActivity.this, "성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}