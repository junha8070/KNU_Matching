package com.example.knu_matching.Post;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import io.grpc.Context;

@RequiresApi(api = Build.VERSION_CODES.O)
public class postActivity extends AppCompatActivity {
    private Button btn_write, btn_choice;
    private EditText edt_Title, edt_Number, edt_post;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private String str_Title, str_date, str_Number, str_post, str_Nickname, str_email, str_Id, str_application, str_EndDate, str_filename;
    private FirebaseUser user;
    private TextView application, edt_date, tv_EndDate;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri filePath, downloadUri;
    int year;
    int month;
    int datOfMonth;
    // 현재 날짜/시간
    LocalDateTime Now = LocalDateTime.now();
    private DatePickerDialog.OnDateSetListener callbackMethod, callbackMethod2;
    // 포맷팅
    String formatedNow = Now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    Date now = new Date();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btn_write = findViewById(R.id.btn_write);
        edt_date = findViewById(R.id.edt_date);
        edt_Title = findViewById(R.id.edt_Title);
        tv_EndDate = findViewById(R.id.tv_EndDate);
        edt_Number = findViewById(R.id.edt_Number);
        edt_post = findViewById(R.id.edt_post);

        application = findViewById(R.id.application);
        btn_choice = findViewById(R.id.btn_choice);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");
        mFirebaseAuth = FirebaseAuth.getInstance();

        this.InitializeListener();

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(postActivity.this, callbackMethod, Now.getYear()-1,Now.getDayOfMonth(), now.getDate());
                dialog.show();
            }
        });

        tv_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(postActivity.this, callbackMethod2, Now.getYear()-1,Now.getDayOfMonth(), now.getDate());
                dialog.show();
            }
        });

        btn_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });


        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_Title = edt_Title.getText().toString();
                str_date = edt_date.getText().toString();
                str_EndDate = tv_EndDate.getText().toString();
                str_Number = edt_Number.getText().toString();
                str_post = edt_post.getText().toString();
                str_application = application.getText().toString();

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
                                System.out.println("유얼아이"+uploadFile());
                                postInfo postInfo = new postInfo(str_Title, str_date, str_EndDate, str_Number, str_post, formatedNow, str_Nickname, str_email, str_Id, str_filename, downloadUri);
                                update(postInfo);
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
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
            str_filename = getFileName(filePath);
            Log.d(TAG, "uri:" + getFileName(filePath));
            System.out.println("파일명 : " + filePath.getLastPathSegment());
            application.setText(str_filename);
        }
    }

    private Uri uploadFile() {
        Uri file = filePath;
        Uri temp;
        StorageReference riversRef = storageRef.child(str_Id).child(getFileName(file));
        UploadTask uploadTask = riversRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                System.out.println("경로1:"+riversRef.getDownloadUrl());
                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    System.out.println("경로2:"+downloadUri);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
        return downloadUri;
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                edt_date.setText(year + ". " + month + ". " + dayOfMonth + ". ");
            }
        };
        callbackMethod2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                tv_EndDate.setText(year + ". " + month + ". " + dayOfMonth + ". ");
            }
        };
    }

    // URI에서 파일명 얻기
//    private String getFileNameFromUri(Uri uri) {
//        String fileName = "";
//
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//            Log.i(TAG, "Display Name: " + fileName);
//
//        }
//        cursor.close();
//
//        return fileName;
//    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}


