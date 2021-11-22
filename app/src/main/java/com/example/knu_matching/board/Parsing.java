package com.example.knu_matching.board;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Parsing{
    private String apiUrl = "https://adst.jobaba.net/jobabaApi/v1.do?authKey=32BMWQNO5SCJE5AY9J07P0FTBFTKJ98D&type=C";
    private static final String TAG_TITLE = "title";
    private static final String TAG_PLACE = "place";
    private static final String TAG_STARTDATE = "startDate";
    private static final String TAG_ENDDATE = "endDate";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_REGION = "region";
    private static final String TAG_URL = "url";
    private static final String TAG_IMGURL = "imgurl";
    public HashMap<String, String> posts = new HashMap<String, String>();
    public ArrayList<HashMap<String, String>> noticeList = new ArrayList<HashMap<String, String>>();
    public void getData(int num){
                try {

                    //url과 관련된 부분
                    URL url = new URL(apiUrl+"&startPage="+String.valueOf(num));
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

    }
}
