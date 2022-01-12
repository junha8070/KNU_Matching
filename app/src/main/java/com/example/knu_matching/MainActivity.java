package com.example.knu_matching;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.Nav.Edit_profile_Activity;
import com.example.knu_matching.Nav.MyParticipate_Activity;
import com.example.knu_matching.Nav.MyPost_Activity;
import com.example.knu_matching.Nav.Scrap_Activity;
import com.example.knu_matching.Nav.SettingActivity;
import com.example.knu_matching.People.PeolpeFragment;
import com.example.knu_matching.Post.PostFragment;
import com.example.knu_matching.Recruitment.RecruitmentFragment;
import com.example.knu_matching.board.BoardFragment;
import com.example.knu_matching.chatting.ChatFragment;
import com.example.knu_matching.membermanage.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    //
    private long backpressedTime = 0;
    public Button btn_register, btn_logout;
    private final String TAG = this.getClass().getSimpleName();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user != null ? user.getUid() : null;
    public FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference dbRef = db.collection("Account").document(user.getEmail().replace(".", ">"));
    Context mContext;
    public String strEmail, strPassword, strNick, strMaojr, strStudentId, strPhoneNumber, strStudentName;
    int rate;

    // DrawerLayout
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iv_level;
    private TextView tv_name, tv_nickname, tv_major, tv_rate;

    private Intent intent;
    private ViewPager2 mViewPager;
    private MyViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    String code;
    private String[] titles = new String[]{"게시판", "친구", "채팅", "활동\n게시판"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        Intent fcm = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
//        startService(fcm);

        context = MainActivity.this;
//        getSupportActionBar().hide();


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
                    //   Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    //    Log.d(TAG, "Current data: " + snapshot.getData());
                    UserAccount userAccount = snapshot.toObject(UserAccount.class);
                    strStudentName = userAccount.getStudentName();
                    strEmail = userAccount.getEmailId();
                    strPassword = userAccount.getPassword();
                    strNick = userAccount.getNickName();
                    strMaojr = userAccount.getMajor();
                    strStudentId = userAccount.getStudentId();
                    strPhoneNumber = userAccount.getPhoneNumber();

                    // Toast.makeText(MainActivity.this, "메인2" + strNick, Toast.LENGTH_SHORT).show();
                } else {
                    // Log.d(TAG, "Current data: null");
                }
                //   Toast.makeText(MainActivity.this, "메인" + strNick, Toast.LENGTH_SHORT).show();


            }
        });

        //code = getIntent().getExtras().getString("code"); // 다른 Activity에서 값을 넘겨 받았을 때
        code = "";
        Log.e(TAG, code);

        Fragment frag_post = new PostFragment().newInstance(code, "");
        Fragment frag_people = new PeolpeFragment();
        Fragment frag_chat = new ChatFragment();
        Fragment frag_recruit = new RecruitmentFragment();
        Fragment frag_board = new BoardFragment().newInstance(code, "");
//        Fragment frag_mypage = new MypageFragment().newInstance(code, "");

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.rgb(0, 0, 0), Color.rgb(0, 113, 190));

        myPagerAdapter = new MyViewPagerAdapter(this);
        myPagerAdapter.addFrag(frag_post);
        myPagerAdapter.addFrag(frag_people);
        myPagerAdapter.addFrag(frag_chat);
//        myPagerAdapter.addFrag(frag_mypage);
        myPagerAdapter.addFrag(frag_board);
        mViewPager.setAdapter(myPagerAdapter);

        //displaying tabs
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> tab.setText(titles[position])).attach();

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        // 액션바 객체
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //뒤로가기버튼 이미지 적용
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigationView);
        View nav_header_view = navigationView.getHeaderView(0);
//        tv_rate = nav_header_view.findViewById(R.id.tv_rate);
        tv_name = nav_header_view.findViewById(R.id.tv_name);
        iv_level = nav_header_view.findViewById(R.id.iv_level);
        tv_nickname = nav_header_view.findViewById(R.id.tv_nickname);
        tv_major = nav_header_view.findViewById(R.id.tv_major);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.nav_mypost:
                        intent = new Intent(MainActivity.context, MyPost_Activity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_participate:
                        intent = new Intent(MainActivity.context, MyParticipate_Activity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_scrap:
                        intent = new Intent(MainActivity.context, Scrap_Activity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_edit_profile:
                        intent = new Intent(MainActivity.context, Edit_profile_Activity.class);
                        intent.putExtra("nickname", strNick);
                        intent.putExtra("studentId", strStudentId);
                        intent.putExtra("major", strMaojr);
                        intent.putExtra("phoneNumber", strPhoneNumber);
                        intent.putExtra("studentName", strStudentName);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_setting:
                        intent = new Intent(MainActivity.context, SettingActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;
                }

                return false;
            }
        });
        db.collection("Report").whereEqualTo("str_comment_email", mFirebaseAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println("Error: Post_Owner_Activity - Comment 불러오기 오류");
                    return;
                }
                for (DocumentChange doc : value.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            System.out.println("신고 사이즈"+value.getDocuments().size());
                            // 추가되었을때 작업
                        case MODIFIED:
                            // 수정되었을때 작업
                        case REMOVED:
                            // 삭제되었을때 작업
                            switch (value.getDocuments().size()) {
                                case 0:
                                    iv_level.setImageResource(R.drawable.ic_level_6);
                                    break;
                                case 1:
                                    iv_level.setImageResource(R.drawable.ic_level_5);
                                    break;
                                case 2:
                                    iv_level.setImageResource(R.drawable.ic_level_4);
                                    break;
                                case 3:
                                    iv_level.setImageResource(R.drawable.ic_level_3);
                                    break;
                                case 4:
                                    iv_level.setImageResource(R.drawable.ic_level_2);
                                    break;
                                case 5:
                                    iv_level.setImageResource(R.drawable.ic_level_1);
                                    break;
                                case 6:
                                default:
                                    iv_level.setImageResource(R.drawable.ic_level_0);
                                    break;
                            }
                            break;
                    }
                }
            }
        });
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                System.out.println("신고 사이즈"+task.getResult().size());
//                switch (task.getResult().size()){
//                    case 0:
//                        iv_level.setImageResource(R.drawable.ic_level_6);
//                        break;
//                    case 1:
//                        iv_level.setImageResource(R.drawable.ic_level_5);
//                        break;
//                    case 2:
//                        iv_level.setImageResource(R.drawable.ic_level_4);
//                        break;
//                    case 3:
//                        iv_level.setImageResource(R.drawable.ic_level_3);
//                        break;
//                    case 4:
//                        iv_level.setImageResource(R.drawable.ic_level_2);
//                        break;
//                    case 5:
//                        iv_level.setImageResource(R.drawable.ic_level_1);
//                        break;
//                    case 6:
//                    default:
//                        iv_level.setImageResource(R.drawable.ic_level_0);
//                        break;
//                }
//            }
//        });
        dbRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //     Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    //      Log.d(TAG, "Current data: " + snapshot.getData());
                    UserAccount userAccount = snapshot.toObject(UserAccount.class);
                    strStudentName = userAccount.getStudentName();
                    strEmail = userAccount.getEmailId();
                    strPassword = userAccount.getPassword();
                    strNick = userAccount.getNickName();
                    strMaojr = userAccount.getMajor();
                    strStudentId = userAccount.getStudentId();
                    strPhoneNumber = userAccount.getPhoneNumber();
                    rate = userAccount.getRate();

                    tv_name.setText(strStudentName);
                    tv_nickname.setText(strNick);
                    tv_major.setText(strMaojr);
//                    tv_rate.setText(String.valueOf(rate));

                } else {
                    //     Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void displayMessage(String message) {
        //   Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //메뉴 선택시 네비게이션 호출
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //뒤로가기 두번해야 꺼지게 하는 코드
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }
}