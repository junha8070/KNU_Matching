package com.example.knu_matching;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.O)
public class postRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private TextView tv_Title, tv_Number, tv_date, tv_post;
    private Button btn_list, btn_change, btn_delete, btn_comment;
    private EditText edt_comment;
    private String str_Title, str_date, str_Number, str_post, str_time, str_Nickname, str_email, str_comment, str_Id;
    private FirebaseUser user;

    LocalDateTime now = LocalDateTime.now();
    String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));



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

        user = FirebaseAuth.getInstance().getCurrentUser();
        str_email = user.getEmail();


        db.collection("Post").document(str_Id).collection("Comment").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<postInfo2> postList2 = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "==>" + document.getData());
                                postList2.add(new postInfo2(
                                        document.getData().get("str_email").toString(),
                                        document.getData().get("str_comment").toString(),
                                        document.getData().get("str_Nickname").toString(),
                                        document.getData().get("str_time").toString()
                                ));
                                System.out.println("이메일 " +document.getData().get("str_email").toString());
                            }
                            RecyclerView recyclerView = postRegisterActivity.this.findViewById(R.id.recycleView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(postRegisterActivity.this));
                            RecyclerView.Adapter mAdapter2 = new AdapterActivity2(postRegisterActivity.this, postList2);
                            recyclerView.setAdapter(mAdapter2);
                        } else {
                            Log.d(TAG, "error", task.getException());
                        }
                    }
                });




        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_comment = edt_comment.getText().toString();

                db.collection("Account").document(user.getEmail().replace(".",">"))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            UserAccount userAccount = task.getResult().toObject(UserAccount.class);
                            str_Nickname = userAccount.getNickName();
                            postInfo2 postInfo2 = new postInfo2(str_email, str_comment, str_Nickname, formatedNow);
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
                db.collection("Post").document(str_Id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        System.out.println("이메일6"+str_email);
        System.out.println("이메일2"+mFirebaseAuth.getCurrentUser().getEmail());

        if(mFirebaseAuth.getCurrentUser().getEmail().equals(str_email)==false){
            System.out.println("이메일5"+mFirebaseAuth.getCurrentUser().getEmail());
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


