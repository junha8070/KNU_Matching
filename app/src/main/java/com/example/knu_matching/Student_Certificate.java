package com.example.knu_matching;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*
실제 존재하는 학번인지 확인하는 코드
*/
public class Student_Certificate extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    public String StudentId=null, Major=null, Email=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_certificate);

        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Uri uri = Uri.parse(view.getUrl()); //url을 uri로 변경
                System.out.println("테스트 디버깅:" + uri);
                if (uri.toString().contains("web.kangnam.ac.kr/sso/index.jsp")) {
                    System.out.println("디버깅2");
                    //로딩창 띄워버리기
                }

                view.loadUrl(url);
                return true;

            }

        });
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mWebView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        mWebView.loadUrl("https://knusso.kangnam.ac.kr/login.jsp"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

    }

    public class MyJavascriptInterface {

        @JavascriptInterface
        public void getHtml(String html) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            System.out.println(html);
            String StartIDTarget = "inputUid\":\"";
            String EndIDTarget = "\",\"inputDm";
            String StartMajorTarget = "userDeptNm\":\"";
            String EndMajorTarget = "\",\"userDeptCd";
            String StartEmailTarget = "\"userMail\":\"";
            String EndEmailTarget = "\",\"userPosi\"";

            int start = html.indexOf(StartIDTarget);
            int End = html.indexOf(EndIDTarget);
            StudentId = html.substring(start + 11, End);
            System.out.println("디버깅4");
            System.out.println("학번:" + StudentId);

            start = html.indexOf(StartMajorTarget);
            End = html.indexOf(EndMajorTarget);
            Major = html.substring(start + 13, End);
            System.out.println("디버깅5");
            System.out.println("학번:" + Major);

            start = html.indexOf(StartEmailTarget);
            End = html.indexOf(EndEmailTarget);
            Email = html.substring(start + 12, End);
            System.out.println("디버깅5");
            System.out.println("학번:" + Email);
            backMainActivity(StudentId,Major,Email);
        }
    }

    private void backMainActivity(String StudentId, String Major, String Email) {
        System.out.println("디버깅3");
        Intent intent = new Intent();
        intent.putExtra("StudentId",StudentId);
        intent.putExtra("Major",Major);
        intent.putExtra("Email",Email);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}