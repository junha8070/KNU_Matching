package com.example.knu_matching;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.example.knu_matching.main.MainActivity;
import com.example.knu_matching.membermanage.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Value;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class postActivity extends AppCompatActivity {
    private Button btn_write, btn_choice;
    private EditText edt_Title, edt_Number, edt_date, edt_post;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private String str_Title, str_date, str_Number, str_post, str_Nickname, str_email, str_Id;
    private FirebaseUser user;
    private TextView application;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri filePath;

    // 현재 날짜/시간
    LocalDateTime Now = LocalDateTime.now();
    // 포맷팅
    String formatedNow = Now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
    Date now = new Date();
    String filename = formatter.format(now) + ".png";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btn_write = findViewById(R.id.btn_write);
        edt_date = findViewById(R.id.edt_date);
        edt_Title = findViewById(R.id.edt_Title);
        edt_Number = findViewById(R.id.edt_Number);
        edt_post = findViewById(R.id.edt_post);

        application = findViewById(R.id.application);
        btn_choice = findViewById(R.id.btn_choice);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");
        mFirebaseAuth = FirebaseAuth.getInstance();


        btn_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });


        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_Title = edt_Title.getText().toString();
                str_date = edt_date.getText().toString();
                str_Number = edt_Number.getText().toString();
                str_post = edt_post.getText().toString();

                if (str_Title.trim().equals("") || str_date.trim().equals("") || str_Number.trim().equals("") || str_post.trim().equals("")) {
                    Toast.makeText(postActivity.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();

                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    str_email = user.getEmail();
                    str_Id = user.getUid();

                    db.collection("Account").document(user.getEmail().replace(".",">"))
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                UserAccount userAccount = task.getResult().toObject(UserAccount.class);
                                str_Nickname = userAccount.getNickName();
                                postInfo postInfo = new postInfo(str_Title, str_date, str_Number, str_post, formatedNow, str_Nickname, str_email, str_Id);
                                update(postInfo);
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                uploadFile();
                                finish();

                            }
                        }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(postActivity.this, "postActivity 오류",Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });
    }


    private void update(postInfo postInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Post").add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference document) {
                        Toast.makeText(postActivity.this, "성공", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            application.setText(filename);
        }
    }

    private void uploadFile() {
        if (filePath != null) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://knu-matching.appspot.com").child( str_Title+ "/" + filename);
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
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

}


