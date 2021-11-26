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
public class EditPost_Activity extends AppCompatActivity {
    private Button btn_success, btn_choice;
    private EditText edt_Title, edt_Number, edt_post, edt_link;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseRef;
    String str_title, str_count, str_total, str_StartDate, str_EndDate, str_filename, str_content, str_comment, str_email, str_Id, str_time, str_application, str_link;
    private String deletefilename;
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tv_application, edt_date, tv_EndDate;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri filePath, downloadUri;
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
        setContentView(R.layout.activity_post_register2);

        btn_success = findViewById(R.id.btn_success);
        edt_date = findViewById(R.id.edt_date);
        edt_Title = findViewById(R.id.edt_Title);
        tv_EndDate = findViewById(R.id.tv_EndDate);
        edt_Number = findViewById(R.id.edt_Number);
        edt_post = findViewById(R.id.edt_post);
        tv_application = findViewById(R.id.tv_application);
        btn_choice = findViewById(R.id.btn_choice);
        edt_link = findViewById(R.id.edt_link);


        Intent intent = getIntent();
        str_Id = intent.getStringExtra("Str_Id");
        str_title = intent.getStringExtra("Title");
        str_StartDate = intent.getStringExtra("StartDate");
        str_EndDate = intent.getStringExtra("EndDate");
        str_total = intent.getStringExtra("Number");
        str_content = intent.getStringExtra("Post");
        str_application = intent.getStringExtra("Filename");
        System.out.println("파일이름" + str_application);
        str_time = intent.getStringExtra("Time");
        str_email = intent.getStringExtra("Email");
        str_link = intent.getStringExtra("Link");

        edt_Title.setText(str_title);
        edt_Number.setText(str_total);
        edt_date.setText(str_StartDate);
        tv_EndDate.setText(str_EndDate);
        edt_post.setText(str_content);
        tv_application.setText(str_application);
        edt_link.setText(str_link);

        //deletefilename = str_application;


        this.InitializeListener();

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditPost_Activity.this, callbackMethod, Now.getYear() - 1, Now.getDayOfMonth(), now.getDate());
                dialog.show();
            }
        });

        tv_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditPost_Activity.this, callbackMethod2, Now.getYear() - 1, Now.getDayOfMonth(), now.getDate());
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


        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_title = edt_Title.getText().toString();
                str_StartDate = edt_date.getText().toString();
                str_EndDate = tv_EndDate.getText().toString();
                str_total = edt_Number.getText().toString();
                str_content = edt_post.getText().toString();
                str_filename = tv_application.getText().toString();
                str_link = edt_link.getText().toString();
                //str_email = mFirebaseAuth.getCurrentUser().getEmail();

                Uri file = filePath;
                System.out.println("유얼아이" + file);
                if (file!=null) {

                    StorageReference riversRef = storageRef.child(mFirebaseAuth.getUid()).child(str_filename);
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

                                if (str_title.trim().equals("") || str_StartDate.trim().equals("") || str_EndDate.trim().equals("") || str_total.trim().equals("") || str_content.trim().equals("")) {
                                    Toast.makeText(EditPost_Activity.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                                } else {

                                    db.collection("Post").document(str_Id)
                                            .update("str_Title", str_title,
                                                    "str_StartDate", str_StartDate,
                                                    "str_EndDate", str_EndDate,
                                                    "str_Number", str_total,
                                                    "str_post", str_content,
                                                    "str_filename", str_filename,
                                                    "uri", downloadUri.toString(),
                                                    "str_link",str_link)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void v) {
                                                    Toast.makeText(EditPost_Activity.this, "성공", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(EditPost_Activity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    //uploadFile();
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditPost_Activity.this, "실패", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


                            }
                        }
                    });
                }
                else{
                    db.collection("Post").document(str_Id)
                            .update("str_Title", str_title,
                                    "str_StartDate", str_StartDate,
                                    "str_EndDate", str_EndDate,
                                    "str_Number", str_total,
                                    "str_post", str_content,
                                    "str_link",str_link)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    Toast.makeText(EditPost_Activity.this, "성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditPost_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    //uploadFile();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditPost_Activity.this, "실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            str_filename = getFileName(filePath);
            Log.d(TAG, "uri:" + getFileName(filePath));
            System.out.println("파일명 : " + filePath.getLastPathSegment());
            tv_application.setText(str_filename);
            // delete();
        }
    }

    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
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

    private void delete() {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://knu-matching.appspot.com/").child(str_filename + "/" + deletefilename);
        if (str_filename != str_time) {
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


