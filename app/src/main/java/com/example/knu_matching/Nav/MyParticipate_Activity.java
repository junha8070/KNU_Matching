package com.example.knu_matching.Nav;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyParticipate_Activity extends AppCompatActivity {

    //GetterSetter
    Post post;

    // Firebase
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Layout
    private Toolbar toolbar;
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    private MyParticipateAdapter adapter;

    // value
    String str_title, str_count, str_total, str_StartDate, str_EndDate, str_filename, str_content, str_comment, str_email, str_Id, str_time, str_uri, str_link, str_uid;
    ArrayList<Post> postList;
//    static final ArrayList<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_participate);

        init();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager = new GridLayoutManager(MyParticipate_Activity.this, 1);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection("Participate").document(auth.getCurrentUser().getEmail().replace(".", ">")).collection("participate").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    System.out.println("뭐업어지니" + value.getDocumentChanges());
                                    postList = new ArrayList<>();
                                    db.collection("Post").document(dc.getDocument().getData().get("str_Id").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if (task.isSuccessful()) {
                                                Post post = task.getResult().toObject(Post.class);
                                                post.getStr_Title();
                                                post.getStr_Title();
                                                post.getStr_Number();
                                                post.getUri();
                                                post.getStr_post();
                                                post.getStr_email();
                                                post.getStr_filename();
                                                post.getStr_EndDate();
                                                post.getStr_StartDate();
                                                post.getStr_Id();
                                                post.getStr_Nickname();
                                                post.getStr_time();
                                                post.getStr_link();
                                                post.getStr_uid();
                                                postList.add(post);

                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.out.println("아답터 들어가기");
                                                    System.out.println("아답터 들어가기" + postList);
                                                    adapter = new MyParticipateAdapter(MyParticipate_Activity.this, postList);
                                                    rv.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    });

                                    break;
                                case MODIFIED:
                                    break;
                                case REMOVED:
                                    System.out.println("뭐업어지니2" + value.getDocumentChanges());


                                    break;
                            }
                        }

                    }
                });
//                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot dc : task.getResult()) {
//                            ListUp(dc.getData().get("str_Id").toString());
//
//
//
//                        }
//
//                    }
//                });
            }
        }).start();

    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);             //툴바 설정
        setSupportActionBar(toolbar);                               //툴바 셋업
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //뒤로가기 자동 생성
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //툴바 기본 타이틀 제거

        // layout 요소 초기화
        rv = findViewById(R.id.rv);

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

    public void ListUp(String str_Id) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("들어왔나용?");
        System.out.println("들어왔나용?2");
        finish();//인텐트 종료
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent); //액티비티 열기
        overridePendingTransition(0, 0);
    }
}