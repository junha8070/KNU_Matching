package com.example.knu_matching;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.knu_matching.main.FirstFragment;
import com.example.knu_matching.main.MainActivity;
import com.example.knu_matching.membermanage.RegisterActivity;
import com.example.knu_matching.membermanage.Student_Certificate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class postRegisterActivity extends AppCompatActivity {


    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<postInfo> mDataset;

    private TextView tv_Title, tv_Number, tv_date, tv_post;
    private Button btn_list, btn_change, btn_delete;
    private String str_Title, str_date, str_Number, str_post, str_time, str_Id;
    private ArrayList<postInfo> postInfo;



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

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");

        Intent intent = getIntent();
        str_Title = intent.getStringExtra("Title");
        str_date = intent.getStringExtra("Date");
        str_Number = intent.getStringExtra("Number");
        str_post = intent.getStringExtra("Post");
        str_Id = intent.getStringExtra("Id");

        tv_Title.setText(str_Title);
        tv_Number.setText(str_Number);
        tv_date.setText(str_date);
        tv_post.setText(str_post);


        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                db.collection("Post").document("Uid").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(postRegisterActivity.this, "성공", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(postRegisterActivity.this, "실패", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });




                
            }
        });





    }


}


