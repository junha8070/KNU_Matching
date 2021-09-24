package com.example.knu_matching.main;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

public class ChangePassWord extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference dbRef = db.collection("Account").document(user.getEmail().replace(".", ">"));
    private EditText edt_original_pwd, edt_new_pwd, edt_re_pwd;
    Button btn_back, btn_change_pwd;
    String original_Password, new_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        init();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                original_Password = edt_original_pwd.getText().toString();
                new_Password = edt_new_pwd.getText().toString();
                System.out.println("체인지" + (((MainActivity) MainActivity.context).strPassword).equals(original_Password));
                if (original_Password.equals(new_Password)) {
                    Toast.makeText(ChangePassWord.this, "기존 비밀번호와 같습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (new_Password.equals(edt_re_pwd.getText().toString()) == false) {
                        Toast.makeText(ChangePassWord.this, "새로운 비밀번호와 재입력 비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if ((((MainActivity) MainActivity.context).strPassword).equals(original_Password) == true) {
                            change_password(original_Password, new_Password);
                        } else {
                            Toast.makeText(ChangePassWord.this, "기존 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void init() {
        edt_original_pwd = findViewById(R.id.edt_original_pwd);
        edt_new_pwd = findViewById(R.id.edt_new_pwd);
        edt_re_pwd = findViewById(R.id.edt_re_pwd);
        btn_back = findViewById(R.id.btn_back);
        btn_change_pwd = findViewById(R.id.btn_change_pwd);
    }
// TODO: DB비밀번호도 같이 변경하게 하기 20210916_23_06
    private void change_password(String original_Password, String new_Password) {

        user.updatePassword(new_Password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(dbRef,"password",edt_new_pwd.getText().toString());
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "비밀번호 변경 성공");
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "비밀번호 변경 실패", e);
                                }
                            });
                        }
                    }
                });
    }
}