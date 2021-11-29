package com.example.knu_matching.Nav;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.knu_matching.GetSet.Board;
import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.Post.PostAdapter;
import com.example.knu_matching.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPost_Activity extends AppCompatActivity {

    //GetterSetter
    Post post;

    // Firebase
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Layout
    private Toolbar toolbar;
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    private MyPostAdapter adapter;

    // value
    String str_title, str_count, str_total, str_StartDate, str_EndDate, str_filename, str_content, str_comment, str_email, str_Id, str_time, str_uri, str_link, str_uid;
    ArrayList<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        init();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager = new GridLayoutManager(MyPost_Activity.this, 1);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);



        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection("Post").whereEqualTo("str_email", auth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Post> postList = new ArrayList<>();
                        for (DocumentChange data : value.getDocumentChanges()) {
                            switch (data.getType()) {
                                case ADDED:
                                    Post post = data.getDocument().toObject(Post.class);
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
                                    System.out.println("결과값 출력2 " + postList.get(0).getStr_uid());
                                    System.out.println("결과값 출력 " + post.getStr_Title());
                                    System.out.println("결과값 출력 " + post.getStr_uid());
                                    break;
                                case MODIFIED:
                                    break;
                                case REMOVED:
                                    break;
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("아답터 들어가기");
                                adapter = new MyPostAdapter(MyPost_Activity.this, postList);
                                System.out.println("결과값 출력23 " + postList.get(0).getStr_uid());
                                rv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
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
}