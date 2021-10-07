package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.knu_matching.main.MainActivity;
import com.example.knu_matching.main.PostFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PostRegisterActivity2 extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<postInfo> mDataset;

    private EditText edt_Title, edt_Number, edt_date, edt_post;
    private Button btn_success;
    private String str_Title, str_date, str_Number, str_post, str_time, str_Id;
    private ArrayList<postInfo> postInfo;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_register2);


        edt_date = findViewById(R.id.edt_date);
        edt_Title = findViewById(R.id.edt_Title);
        edt_Number = findViewById(R.id.edt_Number);
        edt_post = findViewById(R.id.edt_post);

        btn_success = findViewById(R.id.btn_success);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");

        Intent intent = getIntent();
        str_Title = intent.getStringExtra("Title");
        str_date = intent.getStringExtra("Date");
        str_Number = intent.getStringExtra("Number");
        str_post = intent.getStringExtra("Post");
        str_Id = intent.getStringExtra("Id");

        edt_Title.setText(str_Title);
        edt_Number.setText(str_Number);
        edt_date.setText(str_date);
        edt_post.setText(str_post);

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_Title = edt_Title.getText().toString();
                str_date = edt_date.getText().toString();
                str_Number = edt_Number.getText().toString();
                str_post = edt_post.getText().toString();

                if (str_Title.trim().equals("") || str_date.trim().equals("") || str_Number.trim().equals("") || str_post.trim().equals("")) {
                    Toast.makeText(PostRegisterActivity2.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                } else {

                    db.collection("Post").document(str_Id)
                            .update("str_Title", str_Title,
                                    "str_date", str_date,
                                    "str_Number", str_Number,
                                    "str_post", str_post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void v) {
                            Toast.makeText(PostRegisterActivity2.this, "성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostRegisterActivity2.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostRegisterActivity2.this, "실패", Toast.LENGTH_SHORT).show();
                        }
                    });

                }



            }
        });
    }
}