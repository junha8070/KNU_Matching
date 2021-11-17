package com.example.knu_matching;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.main.MainActivity;
import com.example.knu_matching.main.PostFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PostRegisterActivity2 extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<postInfo> mDataset;

    private EditText edt_Title, edt_Number, edt_date, edt_post;
    private TextView tv_application;
    private Button btn_success, btn_choice;
    private String str_Title, str_date, str_Number, str_post, str_time, str_Id, str_application;
    private String deletefilename;
    private ArrayList<postInfo> postInfo;
    private FirebaseUser user;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private Uri filePath;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    Date now = new Date();
    String filename = formatter.format(now);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_register2);


        edt_date = findViewById(R.id.edt_date);
        edt_Title = findViewById(R.id.edt_Title);
        edt_Number = findViewById(R.id.edt_Number);
        edt_post = findViewById(R.id.edt_post);

        tv_application = findViewById(R.id.tv_application);
        btn_choice = findViewById(R.id.btn_choice);

        btn_success = findViewById(R.id.btn_success);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");

        Intent intent = getIntent();
        str_Title = intent.getStringExtra("Title");
        str_date = intent.getStringExtra("Date");
        str_Number = intent.getStringExtra("Number");
        str_post = intent.getStringExtra("Post");
        str_Id = intent.getStringExtra("Id");
        str_application = intent.getStringExtra("Application");
        str_time = intent.getStringExtra("Time");

        deletefilename = str_application;
        System.out.println("app" + deletefilename);


        edt_Title.setText(str_Title);
        edt_Number.setText(str_Number);
        edt_date.setText(str_date);
        edt_post.setText(str_post);
        tv_application.setText(str_application);





        btn_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_Title = edt_Title.getText().toString();
                str_date = edt_date.getText().toString();
                str_Number = edt_Number.getText().toString();
                str_post = edt_post.getText().toString();
                str_application = tv_application.getText().toString();

                if (str_Title.trim().equals("") || str_date.trim().equals("") || str_Number.trim().equals("") || str_post.trim().equals("")) {
                    Toast.makeText(PostRegisterActivity2.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                } else {

                    db.collection("Post").document(str_Id)
                            .update("str_Title", str_Title,
                                    "str_date", str_date,
                                    "str_Number", str_Number,
                                    "str_post", str_post,
                                    "str_application", str_application)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void v) {
                            Toast.makeText(PostRegisterActivity2.this, "성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostRegisterActivity2.this, MainActivity.class);
                            startActivity(intent);
                            uploadFile();
                            finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            tv_application.setText(filename);
            delete();

        }
    }

    private void uploadFile() {
        if (filePath != null) {
            StorageReference storageRef = storage.getReferenceFromUrl("gs://knu-matching.appspot.com").child( str_Title + "/" + filename);
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
    private void delete(){
        StorageReference storageRef = storage.getReferenceFromUrl("gs://knu-matching.appspot.com/").child(str_Title + "/" + deletefilename);
        if(filename != str_time) {
            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void v) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }

    }
}