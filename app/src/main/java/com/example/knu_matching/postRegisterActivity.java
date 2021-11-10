package com.example.knu_matching;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.knu_matching.main.MainActivity;
import com.example.knu_matching.membermanage.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class postRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<postInfo> mDataset;

    private TextView tv_Title, tv_Number, tv_date, tv_post;
    private Button btn_list, btn_change, btn_delete, btn_comment;
    private EditText edt_comment;
    private String str_Title, str_date, str_Number, str_post, str_time, str_Nickname, str_email, str_comment, str_Id;
    private ArrayList<postInfo> postInfo;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_register);

        tv_date = findViewById(R.id.edt_date);
        tv_Title = findViewById(R.id.edt_Title);
        tv_Number = findViewById(R.id.edt_Number);
        tv_post = findViewById(R.id.edt_post);

        btn_change = findViewById(R.id.btn_change);
        btn_list = findViewById(R.id.btn_list);
        btn_delete = findViewById(R.id.btn_delete);
        btn_comment = findViewById(R.id.btn_comment);

        edt_comment = findViewById(R.id.edt_comment);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");

        Intent intent = getIntent();
        str_Id = intent.getStringExtra("Id");
        str_Title = intent.getStringExtra("Title");
        str_date = intent.getStringExtra("Date");
        str_Number = intent.getStringExtra("Number");
        str_post = intent.getStringExtra("Post");
        System.out.println("uid 출력"+str_email);

        tv_Title.setText(str_Title);
        tv_Number.setText(str_Number);
        tv_date.setText(str_date);
        tv_post.setText(str_post);


        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_comment = edt_comment.getText().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                str_email = user.getEmail();

                db.collection("Account").document(user.getEmail().replace(".",">"))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            UserAccount userAccount = task.getResult().toObject(UserAccount.class);
                            str_Nickname = userAccount.getNickName();
                            postInfo2 postInfo2 = new postInfo2(str_email, str_comment, str_Nickname);
                            update(postInfo2);
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(postRegisterActivity.this, "postActivity 오류",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Post").document(str_Nickname).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Toast.makeText(postRegisterActivity.this, "성공", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(postRegisterActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(postRegisterActivity.this, PostRegisterActivity2.class);
                intent.putExtra("Title",str_Title.toString());
                intent.putExtra("Date", str_date.toString());
                intent.putExtra("Number", str_Number.toString());
                intent.putExtra("Post", str_post.toString());
                intent.putExtra("Id", str_Id.toString());
                startActivity(intent);
            }
        });

        System.out.println("이메일2"+mFirebaseAuth.getCurrentUser().getEmail());

        if(mFirebaseAuth.getCurrentUser().getEmail().equals(str_email)==false){
            btn_change.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }

    }

    private void update(postInfo2 postInfo2) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Post").document(str_Id).collection("Comment").add(postInfo2)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(postRegisterActivity.this, "성공", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}


