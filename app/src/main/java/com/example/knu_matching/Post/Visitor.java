package com.example.knu_matching.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.MainActivity;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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
public class Visitor extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_comment, btn_participate;
    TextView tv_count, tv_total, tv_StartDate, tv_EndDate, tv_file, tv_content, tv_title;
    RecyclerView rv_comment;
    CommentAdapter commentAdapter = null;
    EditText edt_comment;
    String str_participate_Nickname, str_participate_Major, str_participate_StudentId, str_participate_EmailId;
    String str_title, str_count, str_total, str_StartDate, str_EndDate, str_filename, str_content, str_comment, str_email, str_Id, str_time, str_application;
    public String str_Current_Email;
    Intent intent;
    ArrayList<CommentItem> comment_list;
    Context context = this;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    int count = 0;

    LocalDateTime now = LocalDateTime.now();
    String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"));

    // Firebase
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseRef;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        //TODO: 첨부파일 작업하기

        init();     // 요소 초기화 작업
        // ArrayList 초기화
        comment_list = new ArrayList<>();

        // Adapter 초기화
        commentAdapter = new CommentAdapter(comment_list);
        rv_comment.setAdapter(commentAdapter);
        rv_comment.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        tv_title.setText(str_title);            // 제목
        tv_total.setText(str_total);            // 모집 인원
        tv_StartDate.setText(str_StartDate);    // 모집 시작기간
        tv_EndDate.setText(str_EndDate);        // 모집 끝나는기간
        tv_content.setText(str_content);        // 내용

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentItem commentItem = new CommentItem();
                commentItem.setStr_Email(auth.getCurrentUser().getEmail());
                commentItem.setStr_Date(formatedNow);
                commentItem.setStr_NickName(((MainActivity) MainActivity.context).strNick);
                commentItem.setStr_Content(edt_comment.getText().toString());
                db.collection("Post").document(str_Id).collection("Comment").add(commentItem)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(Visitor.this, "성공", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Visitor.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 댓글 DB 실시간으로 가져오기
        db.collection("Post").document(str_Id).collection("Comment").orderBy("str_time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.out.println("Error: Post_Owner_Activity - Comment 불러오기 오류");
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            switch (doc.getType()) {
                                case ADDED:
                                    System.out.println("오너 디버깅" + doc.getDocument().getString("str_email2"));
                                    addItem(doc.getDocument().getString("str_email2"), doc.getDocument().getString("str_Nickname2"), doc.getDocument().getString("str_comment2"), doc.getDocument().getString("str_time"));
                                    commentAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    // 수정되었을때 작업
                                    break;
                                case REMOVED:
                                    // 삭제되었을때 작업
                                    delItem(doc.getDocument().getString("str_email2"), doc.getDocument().getString("str_Nickname2"), doc.getDocument().getString("str_comment2"), doc.getDocument().getString("str_time"));
                                    commentAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                });

        tv_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageRef.child(str_title + "/" + str_application).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
            public void onClick(View view) {
                mFirebaseAuth = FirebaseAuth.getInstance();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Knu_Matching");
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
                                ParticipateUser participateUser = new ParticipateUser(str_participate_Nickname, str_participate_Major, str_participate_StudentId, str_participate_EmailId);
                                ParticipateUserSave(participateUser);
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Visitor.this, "postActivity 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // 게시글 작성자가 참여가 확인하는 곳
                }
            }
        });


//        db.collection("Post").document(str_Id).collection("Participate")
//                .whereEqualTo("str_participate_EmailId", auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                System.out.println("현재 이메일"+ auth.getCurrentUser().getEmail());
//                Toast.makeText(Visitor.this,"2222.",Toast.LENGTH_SHORT).show();
//                btn_participate.setEnabled(false);
//                btn_participate.setText("참여 완료");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Visitor.this,"오류가 발생하였습니다.",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // 댓글 추가
    private void addItem(String Email, String Nickname, String Content, String Date) {
        CommentItem item = new CommentItem();

        item.setStr_Email(Email);
        item.setStr_NickName(Nickname);
        item.setStr_Content(Content);
        item.setStr_Date(Date);

        comment_list.add(item);
    }

    // 댓글 삭제 눌렀을때 작동
    private void delItem(String Email, String Nickname, String Content, String Date) {
        CommentItem item = new CommentItem();

        item.setStr_Email(Email);
        item.setStr_NickName(Nickname);
        item.setStr_Content(Content);
        item.setStr_Date(Date);

        comment_list.remove(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        // 상단 툴바 셋팅
        toolbar = (Toolbar) findViewById(R.id.toolbar);             //툴바 설정
        setSupportActionBar(toolbar);                               //툴바 셋업
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //뒤로가기 자동 생성
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //툴바 기본 타이틀 제거

        // id 연결
        btn_comment = findViewById(R.id.btn_comment);
        btn_participate = findViewById(R.id.btn_participate);
        tv_title = findViewById(R.id.tv_title);
        tv_count = findViewById(R.id.tv_count);
        tv_total = findViewById(R.id.tv_total);
        tv_StartDate = findViewById(R.id.tv_StartDate);
        tv_EndDate = findViewById(R.id.tv_EndDate);
        tv_file = findViewById(R.id.tv_file);
        tv_content = findViewById(R.id.tv_content);
        rv_comment = findViewById(R.id.rv_comment);
        edt_comment = findViewById(R.id.edt_comment);

        // intent 값 받아오기
        intent = getIntent();
        str_Id = intent.getStringExtra("Id");
        str_title = intent.getStringExtra("Title");
        str_StartDate = intent.getStringExtra("Date");
        str_EndDate = intent.getStringExtra("EndDate");
        str_total = intent.getStringExtra("Number");
        str_content = intent.getStringExtra("Post");
        str_application = intent.getStringExtra("Application");
        str_time = intent.getStringExtra("Time");
        str_email = intent.getStringExtra("Email");

        // 접속 계정 정보
        str_Current_Email = auth.getCurrentUser().getEmail();
    }

    private void ParticipateUserSave(ParticipateUser participateUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Post").document(str_Id).collection("Participate")
                .document(auth.getCurrentUser().getEmail().replace(".",">")).set(participateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Visitor.this, "참여신청됨", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Visitor.this, "오류", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(Visitor.this, "갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}