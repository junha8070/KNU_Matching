package com.example.knu_matching.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.knu_matching.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

public class BoardFragment extends Fragment {
    private String ParsingUrl = "https://adst.jobaba.net/jobabaApi/v1.do?authKey=32BMWQNO5SCJE5AY9J07P0FTBFTKJ98D&type=C";
    HashMap<String, String> posts = new HashMap<String, String>();
    HashMap<String, String> array = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> noticeList = new ArrayList<HashMap<String, String>>();
    FourthAdapter adapter = new FourthAdapter(getActivity(), noticeList);
    private static final String TAG_TITLE = "title";
    private static final String TAG_PLACE = "place";
    private static final String TAG_STARTDATE = "startDate";
    private static final String TAG_ENDDATE = "endDate";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_REGION = "region";
    private static final String TAG_URL = "url";
    private static final String TAG_IMGURL = "imgurl";
    XmlPullParser xpp;
    private Context mContext;
    Activity activity;
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    ParsingActivity parsingActivity = new ParsingActivity();
    String data, title, place, region, startDate, endDate, site, img_url;

    private Button btn_test;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment4.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fourth_fragment, container, false);
        mLinearLayoutManager = new GridLayoutManager(getActivity(),2);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) view.findViewById(R.id.recycleView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);
//        btn_test = (Button) view.findViewById(R.id.btn_test);
//        btn_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getXmlData();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        for(int i=0;i<noticeList.size();i++){
                            System.out.println("뭘까"+noticeList.get(i).get(TAG_URL));
                            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(noticeList.get(i).get(TAG_URL));
                            jsoupAsyncTask.execute();
                        }
                    }
                });
            }
        }).start();



        // Inflate the layout for this fragment
        return view;

    }

    void getXmlData() {
        try {
            URL url = new URL(ParsingUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("data")) ;// 첫번째 검색결과
                        else if (tag.equals("title")) {
                            xpp.next();
                            posts.put(TAG_TITLE, xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        } else if (tag.equals("place")) {
                            xpp.next();
                            posts.put(TAG_PLACE, xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        } else if (tag.equals("startDate")) {
                            xpp.next();
                            posts.put(TAG_STARTDATE, xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        } else if (tag.equals("endDate")) {
                            xpp.next();
                            posts.put(TAG_ENDDATE, xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        } else if (tag.equals("clCd")) {
                            xpp.next();
                            posts.put(TAG_CATEGORY, xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        } else if (tag.equals("legalCd")) {
                            xpp.next();
                            posts.put(TAG_REGION, xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        } else if (tag.equals("jobabaUrl")) {
                            xpp.next();
                            posts.put(TAG_URL, xpp.getText());

                            System.out.println("파싱:" + xpp.getText());
                            System.out.println("hash 출력" + posts);
                            System.out.println("post출력" + posts);
                            noticeList.add(posts);
                            posts = new HashMap<>();//메모리 주소를 다시 생성하고 다시 생성한것에 값을 넣어줘야 된다.
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;

                }
                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("리스트 출력23" + noticeList.toString());
        System.out.println("디버깅3" + posts.get(TAG_TITLE));
    }//getXmlData method....

    private class JsoupAsyncTask extends AsyncTask {
        String this_url;
        JsoupAsyncTask(String get_url) {
            this_url = get_url;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Connection con = Jsoup.connect(this_url);
                Document doc = con.get();
                Elements ogTags = doc.select("meta[property^=og:]");
                if (ogTags.size() <= 0) {
                    return null;
                }
                // 필요한 OGTag를 추려낸다
                for (int i = 0; i < ogTags.size(); i++) {
                    Element tag = ogTags.get(i);

                    String text = tag.attr("property");
                    if ("og:image".equals(text)) {
                        img_url = tag.attr("content");
                        System.out.println("이미지 주소2"+img_url);
                        array.put(TAG_IMGURL,img_url);
                        arrayList.add(array);
                        array = new HashMap<>();
                        System.out.println("사진리스트2"+arrayList);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(Object o) {
            System.out.println("사진리스트3"+arrayList);
            FourthAdapter adapter = new FourthAdapter(getActivity(), arrayList);
            rv.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
        }
    }
}