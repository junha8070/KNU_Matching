package com.example.knu_matching.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
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
import com.example.knu_matching.SendNotification;
import com.example.knu_matching.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Post_Owner_Acticity extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_down, btn_comment, btn_participate;
    TextView tv_count, tv_total, tv_StartDate, tv_EndDate, tv_file, tv_content, tv_title, tv_link;
    static final Map<String, String> comment_notice= new HashMap<String,String>();
    RecyclerView rv_comment;
    CommentAdapter commentAdapter = null;
    EditText edt_comment;
    String str_title, str_count, str_total, str_StartDate, str_EndDate, str_filename, str_content, str_comment, str_email, str_Id, str_time, str_uri, str_link, str_uid;
    public String str_Current_Email;
    Intent intent;
    ArrayList<CommentItem> comment_list;
    Context context = this;
    int count = 0;
    LocalDateTime now = LocalDateTime.now();
    String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"));
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String File_Name = "확장자를 포함한 파일명";
    String File_extend = "확장자명";
    String Save_Path;
    String Save_folder = "/Download";
    DownloadThread dThread;

    // Firebase
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_owner_acticity);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        init();     // 요소 초기화 작업
        // ArrayList 초기화
        comment_list = new ArrayList<>();

        // Adapter 초기화
        commentAdapter = new CommentAdapter(comment_list);
        rv_comment.setAdapter(commentAdapter);
        rv_comment.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            Save_Path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + Save_folder;
        }

        tv_title.setText(str_title);            // 제목
        tv_total.setText(str_total);            // 모집 인원
        tv_StartDate.setText(str_StartDate);    // 모집 시작기간
        tv_EndDate.setText(str_EndDate);        // 모집 끝나는기간
        tv_content.setText(str_content);        // 내용
        tv_file.setText(str_filename);          // 첨부파일 이름
        tv_link.setText(str_link);              // 링크
        tv_file.setPaintFlags(tv_file.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        System.out.println("array값" + str_filename);
        System.out.println("array값" + str_uri);
        String temp = str_filename;

        if (str_filename != null) {
            String[] str_split = str_filename.split("\\.");
            File_Name = str_filename;
            File_extend = str_split[1];

            tv_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File dir = new File(Save_Path);
                    // 폴더가 존재하지 않을 경우 폴더를 만듦
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    // 다운로드 폴더에 동일한 파일명이 존재하는지 확인해서
                    // 없으면 다운받고 있으면 해당 파일 실행시킴.
                    if (new File(Save_Path + "/" + File_Name).exists() == false) {
                        dThread = new DownloadThread(str_uri + "/" + File_Name,
                                Save_Path + "/" + File_Name);
                        dThread.start();
                    } else {
                        showDownloadFile();
                    }
                }
            });
        }

        System.out.println("참여자 데이터" + str_uid);

        db.collection("Post").document(str_Id).collection("Participate")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int i = 0;
                int j = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Visitor", document.getId() + " => " + document.getData());
                        j = i + 1;
                        i++;
                    }
                    count = j;
                    tv_count.setText(count + "");
                } else {
                    Log.d("Visitor", "Error getting documents: ", task.getException());
                }
            }

        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentItem commentItem = new CommentItem();
                commentItem.setStr_Email(auth.getCurrentUser().getEmail());
                commentItem.setStr_Date(formatedNow);
                commentItem.setStr_NickName(((MainActivity) MainActivity.context).strNick);
                commentItem.setStr_Content(edt_comment.getText().toString());
                commentItem.setStr_Uid(auth.getCurrentUser().getUid());

                db.collection("Post").document(str_Id).collection("Comment").add(commentItem)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(Post_Owner_Acticity.this, "성공", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Post_Owner_Acticity.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 댓글 DB 실시간으로 가져오기
        db.collection("Post").document(str_Id).collection("Comment").orderBy("str_Date", Query.Direction.ASCENDING)
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
                                    System.out.println("오너 디버깅" + doc.getDocument().getString("str_Email"));
                                    addItem(doc.getDocument().getString("str_Email"), doc.getDocument().getString("str_NickName"), doc.getDocument().getString("str_Content"), doc.getDocument().getString("str_Date"), doc.getDocument().getString("str_Uid"));
                                    commentAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    // 수정되었을때 작업
                                    break;
                                case REMOVED:
                                    // 삭제되었을때 작업
                                    delItem(doc.getDocument().getString("str_Email"), doc.getDocument().getString("str_NickName"), doc.getDocument().getString("str_Content"), doc.getDocument().getString("str_Date"), doc.getDocument().getString("str_Uid"));
                                    commentAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                });
    }

    // 댓글 추가
    private void addItem(String Email, String Nickname, String Content, String Date, String Uid) {
        CommentItem item = new CommentItem();

        item.setStr_Email(Email);
        item.setStr_NickName(Nickname);
        item.setStr_Content(Content);
        item.setStr_Date(Date);
        item.setStr_Uid(Uid);

        comment_list.add(item);
    }

    // 댓글 삭제 눌렀을때 작동
    private void delItem(String Email, String Nickname, String Content, String Date, String Uid) {
        CommentItem item = new CommentItem();

        item.setStr_Email(Email);
        item.setStr_NickName(Nickname);
        item.setStr_Content(Content);
        item.setStr_Date(Date);
        item.setStr_Uid(Uid);

        comment_list.remove(item);
    }

    // 상단 툴바에 ... 메뉴 띄우기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mln = getMenuInflater();
        mln.inflate(R.menu.toolbar_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_edit:
                Toast.makeText(getApplicationContext(), "수정하기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Post_Owner_Acticity.this, EditPost_Activity.class);
                intent.putExtra("Title", str_title);
                intent.putExtra("StartDate", str_StartDate);
                intent.putExtra("EndDate", str_EndDate);
                intent.putExtra("Number", str_total);
                intent.putExtra("Post", str_content);
                intent.putExtra("Filename", str_filename);
                intent.putExtra("Str_Id", str_Id);
                intent.putExtra("Link", str_link);
                intent.putExtra("Uid", str_uid);
                startActivity(intent);
                break;
            case R.id.btn_del:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder.setTitle("게시글을 삭제하실건가요?");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage(str_title + " 게시물을 삭제할까요?")
                        .setCancelable(false)
                        .setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 프로그램을 종료한다
                                        db.collection("Post").document(str_Id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "삭제가 완료되었습니다:)", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.\n관리자한테 문의주시길 바랍니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("아니요",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();
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
        btn_down = findViewById(R.id.btn_down);
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
        tv_link = findViewById(R.id.tv_link);

        // intent 값 받아오기
        intent = getIntent();
        str_Id = intent.getStringExtra("Str_Id");
        str_title = intent.getStringExtra("Title");
        str_StartDate = intent.getStringExtra("StartDate");
        str_EndDate = intent.getStringExtra("EndDate");
        str_total = intent.getStringExtra("Number");
        str_content = intent.getStringExtra("Post");
        str_filename = intent.getStringExtra("Filename");
        str_time = intent.getStringExtra("Time");
        str_email = intent.getStringExtra("Email");
        str_uri = intent.getStringExtra("Uri");
        str_link = intent.getStringExtra("Link");
        str_uid = intent.getStringExtra("Uid");

        // 접속 계정 정보
        str_Current_Email = auth.getCurrentUser().getEmail();
    }

    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;
            try {
                imgurl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) imgurl
                        .openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);
                for (; ; ) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }
                is.close();
                fos.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
//            loadingBar.setVisibility(View.GONE);
            // 파일 다운로드 종료 후 다운받은 파일을 실행시킨다.
            showDownloadFile();
        }

    };

    private void showDownloadFile() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(Save_Path + "/" + File_Name);

        // 파일 확장자 별로 mime type 지정해 준다.
        if (File_extend.equals("mp3")) {
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
        } else if (File_extend.equals("mp4")) {
            intent.setDataAndType(Uri.fromFile(file), "vidio/*");
        } else if (File_extend.equals("jpg") || File_extend.equals("jpeg")
                || File_extend.equals("JPG") || File_extend.equals("gif")
                || File_extend.equals("png") || File_extend.equals("bmp")) {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        } else if (File_extend.equals("txt")) {
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else if (File_extend.equals("doc") || File_extend.equals("docx")) {
            intent.setDataAndType(Uri.fromFile(file), "application/msword");
        } else if (File_extend.equals("xls") || File_extend.equals("xlsx")) {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.ms-excel");
        } else if (File_extend.equals("ppt") || File_extend.equals("pptx")) {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.ms-powerpoint");
        } else if (File_extend.equals("pdf")) {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        }
        startActivity(intent);
    }
}