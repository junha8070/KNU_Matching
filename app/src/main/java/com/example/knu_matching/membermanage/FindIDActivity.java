package com.example.knu_matching.membermanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FindIDActivity extends AppCompatActivity {
    private EditText edt_name;
    private Button btn_FindloginId, btn_Cancel;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean nickname_state;
    String strStudentId, strEmail;


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
                strStudentId = edt_name.getText().toString();
                System.out.println("test");
                if (strStudentId.length() == 0) {
                    Toast.makeText(FindIDActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    edt_name.requestFocus();
                    return;
                }
                System.out.println("test   " + strStudentId);
                db.collection("Account")
                        .whereEqualTo("studentId", strStudentId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        UserAccount userAccount = document.toObject(UserAccount.class);
                                        Log.d("db디버깅", userAccount.getNickName());
                                        if (strStudentId.equals(userAccount.getStudentId())) {
                                            Toast.makeText(FindIDActivity.this, document.getId().replace(">", "."), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }

                                    Toast.makeText(FindIDActivity.this, "존재하지 않는 회원입니다", Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            }
                        });
//                mProfieDatabaseReference.orderByChild("studentId").equalTo(strStudentId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            System.out.println("test   " +strStudentId);
//                            System.out.println("test_state" + snapshot);
//                            System.out.println("test_state" + snapshot.toString());
//                            String temp = snapshot.toString();
//                            int start = temp.indexOf("emailId=");
//                            int End = temp.indexOf("}} }");
//                            strEmail = snapshot.toString().substring(start+8,End);
//                            nickname_state = true;
//                            System.out.println("test_state" + nickname_state);
//
//                            String key = mProfieDatabaseReference.getKey();
//
//                            System.out.println("test_state" + key);
//
//                            Toast.makeText(FindIDActivity.this, strEmail, Toast.LENGTH_LONG).show();
//
//                        } else {
//                            System.out.println("test   " +strStudentId);
//                            nickname_state = false;
//                            System.out.println("test_state" +nickname_state);
//                            Toast.makeText(FindIDActivity.this, "존재하지 않는 별명", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

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