package com.example.knu_matching.Post;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Post_Visitor_Activity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private TextView tv_Title, tv_Number, tv_date, tv_post, tv_application, tv_count;
    private Button btn_list, btn_change, btn_delete, btn_comment, btn_down, btn_participate;
    private EditText edt_comment;
    private String str_participate_Nickname, str_participate_Major, str_participate_StudentId, str_participate_EmailId, str_participate_EmailId2, str_participate_Uid;
    private String str_Title, str_date, str_Number, str_post, str_time, str_email, str_Nickname2, str_email2, str_comment2, str_Id, str_application, str_uid;
    private FirebaseUser user;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private int count = 0;

    LocalDateTime now = LocalDateTime.now();
    String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_register);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        tv_date = findViewById(R.id.edt_date);
        tv_Title = findViewById(R.id.edt_Title);
        tv_Number = findViewById(R.id.edt_Number);
        tv_post = findViewById(R.id.edt_post);
        tv_application = findViewById(R.id.edt_application);
        tv_count = findViewById(R.id.tv_count);
        tv_count.setText(count + "");

        btn_change = findViewById(R.id.btn_change);
        btn_list = findViewById(R.id.btn_list);
        btn_delete = findViewById(R.id.btn_delete);
        btn_comment = findViewById(R.id.btn_comment);
        btn_down = findViewById(R.id.btn_down);
        btn_participate = findViewById(R.id.btn_participate);

        edt_comment = findViewById(R.id.edt_comment);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");

        Intent intent = getIntent();
        str_Id = intent.getStringExtra("Id");
        str_Title = intent.getStringExtra("Title");
        str_date = intent.getStringExtra("Date");
        str_Number = intent.getStringExtra("Number");
        str_post = intent.getStringExtra("Post");
        str_application = intent.getStringExtra("Application");
        str_time = intent.getStringExtra("Time");
        str_email = intent.getStringExtra("Email");
        str_uid = intent.getStringExtra("Uid");


        tv_Title.setText(str_Title);
        tv_Number.setText(str_Number);
        tv_date.setText(str_date);
        tv_post.setText(str_post);
        tv_application.setText(str_application);


        db.collection("Post").document(str_Id).collection("Comment").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<postInfo2> postList2 = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "==>" + document.getData());
                                postList2.add(new postInfo2(
                                        document.getData().get("str_email2").toString(),
                                        document.getData().get("str_comment2").toString(),
                                        document.getData().get("str_Nickname2").toString(),
                                        document.getData().get("str_time").toString(),
                                        document.getData().get("str_uid").toString()

                                        ));
                            }
                            RecyclerView recyclerView = Post_Visitor_Activity.this.findViewById(R.id.recycleView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Post_Visitor_Activity.this));
                            RecyclerView.Adapter mAdapter2 = new AdapterActivity2(Post_Visitor_Activity.this, postList2);
                            recyclerView.setAdapter(mAdapter2);
                        } else {
                            Log.d(TAG, "error", task.getException());
                        }
                    }
                });


        db.collection("Post").document(str_Id).collection("Participate").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            ArrayList<ParticipateUser> participateUser = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "==>" + document.getData());
                                participateUser.add(new ParticipateUser(
                                        document.getData().get("str_participate_Nickname").toString(),
                                        document.getData().get("str_participate_Major").toString(),
                                        document.getData().get("str_participate_StudentId").toString(),
                                        document.getData().get("str_participate_EmailId").toString(),
                                        document.getData().get("str_participate_Uid").toString()
                                ));
                            }
                        } else {
                            Log.d(TAG, "error", task.getException());
                        }
                    }
                });


        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_comment2 = edt_comment.getText().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("Account").document(user.getEmail().replace(".", ">"))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserAccount userAccount = task.getResult().toObject(UserAccount.class);
                            str_Nickname2 = userAccount.getNickName();
                            str_email2 = userAccount.getEmailId();
                            postInfo2 postInfo2 = new postInfo2(str_email2, str_comment2, str_Nickname2, formatedNow, str_uid);
                            update(postInfo2);
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Post_Visitor_Activity.this, "postActivity 오류", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Post_Visitor_Activity.this, "성공", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Post_Visitor_Activity.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Post_Visitor_Activity.this, EditPost_Activity.class);
                intent.putExtra("Title", str_Title.toString());
                intent.putExtra("Date", str_date.toString());
                intent.putExtra("Number", str_Number.toString());
                intent.putExtra("Post", str_post.toString());
                intent.putExtra("Time", str_time.toString());
                intent.putExtra("Id", str_Id.toString());
                intent.putExtra("Application", str_application.toString());
                startActivity(intent);
            }
        });

        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageRef.child(str_Title + "/" + str_application).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        new ImageDownload().execute(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        });

        btn_participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirebaseAuth.getCurrentUser().getEmail().equals(str_email) == false) {
                    count++;
                    tv_count.setText(count + "");
                    btn_participate.setEnabled(false);
                    btn_participate.setText("참여 완료");

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    db.collection("Account").document(user.getEmail().replace(".", ">"))
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                UserAccount userAccount = task.getResult().toObject(UserAccount.class);
                                str_participate_Nickname = userAccount.getNickName();
                                str_participate_Major = userAccount.getMajor();
                                str_participate_StudentId = userAccount.getStudentId();
                                str_participate_EmailId = userAccount.getEmailId();
                                str_participate_Uid = userAccount.getUid();
                                ParticipateUser participateUser = new ParticipateUser(str_participate_Nickname, str_participate_Major, str_participate_StudentId, str_participate_EmailId, str_participate_Uid);
                                update2(participateUser);
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Post_Visitor_Activity.this, "postActivity 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // 게시글 작성자가 참여가 확인하는 곳
                }

            }
        });

        if (mFirebaseAuth.getCurrentUser().getEmail().equals(str_email) == false) {
            System.out.println("이메일5" + str_email);
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
                        Toast.makeText(Post_Visitor_Activity.this, "성공", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void update2(ParticipateUser participateUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Post").document(str_Id).collection("Participate")
                .document(str_Id).set(participateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Post_Visitor_Activity.this, "참여신청됨", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Post_Visitor_Activity.this, "오류", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class ImageDownload extends AsyncTask<String, Void, Void> {

        private String fileName = str_application;

        private final String SAVE_FOLDER = "/save_folder";

        @Override
        protected Void doInBackground(String... params) {

            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;

            File dir = new File(savePath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileUrl = params[0];

            if (new File(savePath + "/" + fileName).exists() == false) {
            } else {
            }

            String localPath = savePath + "/" + fileName + ".jpg";

            try {
                URL imgUrl = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();
                File file = new File(localPath);
                FileOutputStream fos = new FileOutputStream(file);
                int read;
                for (; ; ) {
                    read = is.read(tmpByte);
                    if (read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, read);
                }
                is.close();
                fos.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String targetDir = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
            File file = new File(targetDir + "/" + fileName + ".jpg");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            Toast.makeText(Post_Visitor_Activity.this, "갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}


