package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindIDActivity extends AppCompatActivity {
    private EditText edt_name;
    private Button btn_FindloginId, btn_Cancel;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    boolean nickname_state;
    String strNick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_idactivity);
        setTitle("로그인 아이디 찾기");

        edt_name = findViewById(R.id.edt_name);
        btn_FindloginId = findViewById(R.id.btn_FindloginId);
        btn_Cancel = findViewById(R.id.btn_Cancel);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");
        mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mProfieDatabaseReference = mDatabaseRef.child("UserAccount");


        btn_FindloginId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strNick = edt_name.getText().toString();
                System.out.println("test");
                if(strNick.length() == 0){
                    Toast.makeText(FindIDActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    edt_name.requestFocus();
                    return;
                }
                System.out.println("test   " +strNick);
                mProfieDatabaseReference.orderByChild("nickName").equalTo(strNick).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            System.out.println("test   " +strNick);
                            nickname_state = true;
                            System.out.println("test_state" + nickname_state);

                            String key = mProfieDatabaseReference.getKey();

                            System.out.println("test_state" + key);

                            Toast.makeText(FindIDActivity.this, "", Toast.LENGTH_LONG).show();

                        } else {
                            System.out.println("test   " +strNick);
                            nickname_state = false;
                            System.out.println("test_state" +nickname_state);
                            Toast.makeText(FindIDActivity.this, "존재하지 않는 별명", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindIDActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}