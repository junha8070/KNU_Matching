package com.example.knu_matching.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.main.board.BoardFragment;
import com.example.knu_matching.main.recruit.RecruitmentFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    public static Context context;

    public Button btn_register, btn_logout;
    private final String TAG = this.getClass().getSimpleName();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user != null ? user.getUid() : null;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference dbRef = db.collection("Account").document(user.getEmail().replace(".", ">"));
    Context mContext;
    public String strEmail, strPassword, strNick, strMaojr, strStudentId, strPhoneNumber, strStudentName;

    private ViewPager2 mViewPager;
    private MyViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    String code;
    private String[] titles = new String[]{"최신글", "친구", "모집\n상황", "활동\n게시판", "My\nPage"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        getSupportActionBar().hide();


//        dbRef.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot document = task.getResult();
//                            UserAccount userAccount = document.toObject(UserAccount.class);
//                            strStudentName = userAccount.getStudentName();
//                            strEmail = userAccount.getEmailId();
//                            strPassword = userAccount.getPassword();
//                            strNick = userAccount.getNickName();
//                            strMaojr = userAccount.getMajor();
//                            strStudentId = userAccount.getStudentId();
//                            strPhoneNumber = userAccount.getPhoneNumber();
//                            Toast.makeText(MainActivity.this, strMaojr,Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        dbRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    UserAccount userAccount = snapshot.toObject(UserAccount.class);
                    strStudentName = userAccount.getStudentName();
                    strEmail = userAccount.getEmailId();
                    strPassword = userAccount.getPassword();
                    strNick = userAccount.getNickName();
                    strMaojr = userAccount.getMajor();
                    strStudentId = userAccount.getStudentId();
                    strPhoneNumber = userAccount.getPhoneNumber();

                    Toast.makeText(MainActivity.this, "메인2"+strNick, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Current data: null");
                }Toast.makeText(MainActivity.this, "메인"+strNick, Toast.LENGTH_SHORT).show();
            }
        });

        //code = getIntent().getExtras().getString("code"); // 다른 Activity에서 값을 넘겨 받았을 때
        code = "";
        Log.e(TAG, code);

        Fragment frag_post = new PostFragment().newInstance(code, "");
        Fragment frag_people = new PeolpeFragment();
        Fragment frag_recruit = new RecruitmentFragment();
        Fragment frag_board = new BoardFragment().newInstance(code, "");
        Fragment frag_mypage = new MypageFragment().newInstance(code, "");

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);

        myPagerAdapter = new MyViewPagerAdapter(this);
        myPagerAdapter.addFrag(frag_post);
        myPagerAdapter.addFrag(frag_people);
        myPagerAdapter.addFrag(frag_recruit);
        myPagerAdapter.addFrag(frag_board);
        myPagerAdapter.addFrag(frag_mypage);
        mViewPager.setAdapter(myPagerAdapter);

        //displaying tabs
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> tab.setText(titles[position])).attach();
    }

    public void refresh(){
        Intent intent = getIntent();
        finish(); //현재 액티비티 종료 실시
        overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
        startActivity(intent); //현재 액티비티 재실행 실시
        overridePendingTransition(0, 0);

    }
}