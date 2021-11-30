package com.example.knu_matching;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.knu_matching.chatting.ChatActivity;

public class WebView extends AppCompatActivity {

    private String Url, toolbar_title;
    Toolbar toolbar;
    TextView tv_toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        tv_toolBar_title = findViewById(R.id.tv_toolBar_title);

        toolbar = (Toolbar) findViewById(R.id.toolbar);             //툴바 설정
        setSupportActionBar(toolbar);                               //툴바 셋업
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //뒤로가기 자동 생성
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //툴바 기본 타이틀 제거

        Intent intent = getIntent();
        Url = intent.getStringExtra("url");
        toolbar_title = intent.getStringExtra("title");

        tv_toolBar_title.setText(toolbar_title);

        com.example.knu_matching.WebViewCustom view = findViewById(R.id.view);
        view.loadUrl(Url);
        view.setWebViewClient(new WebViewClient());
        view.setWebChromeClient(new WebChromeClient());
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