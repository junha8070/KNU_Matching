package com.example.knu_matching.Post;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.MainActivity;
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
    private EditText edt_Title, edt_Number, edt_post, edt_link;
    private FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseRef;
    private String str_Title, str_StartDate, str_Number, str_post, str_Nickname, str_email, str_EndDate, str_filename, str_Id, str_url;
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView application, edt_date, tv_EndDate;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri filePath, downloadUri;
    Intent intent;
    // 현재 날짜/시간
    LocalDateTime Now = LocalDateTime.now();
    private DatePickerDialog.OnDateSetListener callbackMethod, callbackMethod2;
    // 포맷팅
    String formatedNow = Now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"));
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
        edt_link = findViewById(R.id.edt_link);

        application = findViewById(R.id.application);
        btn_choice = findViewById(R.id.btn_choice);

        intent = getIntent();
        str_url = intent.getStringExtra("Link");
        edt_link.setText(str_url);



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
                str_StartDate = edt_date.getText().toString();
                str_EndDate = tv_EndDate.getText().toString();
                str_Number = edt_Number.getText().toString();
                str_post = edt_post.getText().toString();
                str_filename = application.getText().toString();
                str_Nickname = ((MainActivity)MainActivity.context).strNick;
                str_email = mFirebaseAuth.getCurrentUser().getEmail();

                if(filePath == null){
                    Post post = new Post();
                    post.setStr_link(edt_link.getText().toString());
                    post.setStr_Title(str_Title);
                    post.setStr_Number(str_Number);
                    post.setStr_StartDate(str_StartDate);
                    post.setStr_EndDate(str_EndDate);
                    post.setStr_post(str_post);
                    post.setStr_time(formatedNow);
                    post.setStr_email(str_email);
                    post.setStr_Nickname(str_Nickname);
                    db.collection("Post").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            db.collection("Post").document(documentReference.getId()).update("str_Id",documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                    Toast.makeText(getApplicationContext(),"게시물을 올렸습니다.",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"오류가 발생하였습니다.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else if(filePath != null){
                    Uri file = filePath;
                    StorageReference riversRef = storageRef.child(mFirebaseAuth.getUid()).child(getFileName(file));
                    UploadTask uploadTask = riversRef.putFile(file);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUri = task.getResult();
                                Post post = new Post();
                                post.setStr_link(edt_link.getText().toString());
                                post.setStr_Title(str_Title);
                                post.setStr_Number(str_Number);
                                post.setStr_StartDate(str_StartDate);
                                post.setStr_EndDate(str_EndDate);
                                post.setStr_post(str_post);
                                post.setStr_filename(str_filename);
                                post.setStr_time(formatedNow);
                                post.setUri(downloadUri.toString());
                                post.setStr_email(str_email);
                                post.setStr_Nickname(str_Nickname);

                                db.collection("Post").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        db.collection("Post").document(documentReference.getId()).update("str_Id",documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                                Toast.makeText(getApplicationContext(),"게시물을 올렸습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"오류가 발생하였습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
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
            str_filename = getFileName(filePath);
            Log.d(TAG, "uri:" + getFileName(filePath));
            System.out.println("파일명 : " + filePath.getLastPathSegment());
            application.setText(str_filename);
        }
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


