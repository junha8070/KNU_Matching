package com.example.knu_matching.main.board;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.knu_matching.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment {
    ProgressDialog customProgressDialog;
    private Parcelable recyclerViewState;
    private String apiUrl = "https://adst.jobaba.net/jobabaApi/v1.do?authKey=32BMWQNO5SCJE5AY9J07P0FTBFTKJ98D&type=C";
    private static final String TAG_TITLE = "title";
    private static final String TAG_PLACE = "place";
    private static final String TAG_STARTDATE = "startDate";
    private static final String TAG_ENDDATE = "endDate";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_REGION = "region";
    private static final String TAG_URL = "url";
    private static final String TAG_IMGURL = "imgurl";
    private RecyclerView rv;
    int num = 1;
    private LinearLayoutManager mLinearLayoutManager;
    ArrayList<HashMap<String, String>> noticeList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> posts = new HashMap<String, String>();
    Activity activity;
    BoardAdapter adapter;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoardFragment.
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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
//로딩창 객체 생성
        customProgressDialog = new ProgressDialog(getContext());
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

//        noticeList = parsing.noticeList;

        mLinearLayoutManager = new GridLayoutManager(getActivity(), 1);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);

        BackThread thread = new BackThread();
        thread.setDaemon(true);
        thread.start();

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                System.out.println("마지막이다 이놈아아아아아아아아"+lastVisibleItemPosition+"|||"+itemTotalCount);
                if (lastVisibleItemPosition == itemTotalCount) {
                    customProgressDialog = new ProgressDialog(getContext());
                    //로딩창을 투명하게
                    customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    customProgressDialog.show();
                    BackThread thread = new BackThread();
                    thread.setDaemon(true);
                    thread.start();
                }
            }
        });

        return view;
    }

    class BackThread extends Thread{
        @Override
        public void run() {
            try {
                num = num+1;
                //url과 관련된 부분
                URL url = new URL(apiUrl + "&startPage=" + String.valueOf(num));
                InputStream is = url.openStream();

                //xmlParser 생성
                XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = xmlFactory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));
                int eventType = xpp.getEventType();
                String tag;

                //본격적으로 파싱
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
                handler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            recyclerViewState = rv.getLayoutManager().onSaveInstanceState();
            adapter = new BoardAdapter(getActivity(), noticeList);
            rv.setAdapter(adapter);
            rv.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            adapter.notifyDataSetChanged();
            customProgressDialog.cancel();
        }
    };
}