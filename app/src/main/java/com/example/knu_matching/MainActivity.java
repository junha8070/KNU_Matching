package com.example.knu_matching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.knu_matching.main.FirstFragment;
import com.example.knu_matching.main.MyViewPagerAdapter;
import com.example.knu_matching.main.SecondFragment;
import com.example.knu_matching.main.ThirdFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public Button btn_register, btn_logout;
    private FirebaseAuth mFirebaseAuth;
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ViewPager2 mViewPager;
    private MyViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    String code;
    private String[] titles = new String[]{"리스트", "웹뷰", "연락처"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mContext = MainActivity.this;
        getSupportActionBar().hide();

        //code = getIntent().getExtras().getString("code"); // 다른 Activity에서 값을 넘겨 받았을 때
        code = "";
        Log.e(TAG, code);

        Fragment frag1 = new FirstFragment().newInstance(code,"");
        Fragment frag2 = new SecondFragment().newInstance(code,"");
        Fragment frag3 = new ThirdFragment().newInstance(code,"");

        mViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);

        myPagerAdapter = new MyViewPagerAdapter(this);
        myPagerAdapter.addFrag(frag1);
        myPagerAdapter.addFrag(frag2);
        myPagerAdapter.addFrag(frag3);

        mViewPager.setAdapter(myPagerAdapter);

        //displaying tabs
        new TabLayoutMediator(tabLayout, mViewPager, (tab, position) -> tab.setText(titles[position])).attach();
    }
}