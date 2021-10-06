package com.example.knu_matching.main;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsingActivity {

    private String ParsingUrl = "https://adst.jobaba.net/jobabaApi/v1.do?authKey=32BMWQNO5SCJE5AY9J07P0FTBFTKJ98D&type=C";
    HashMap<String,String> posts = new HashMap<String,String>();
    ArrayList<HashMap<String,String>> noticeList;
    private static final String TAG_TITLE = "title";
    private static final String TAG_PLACE = "place";
    private static final String TAG_STARTDATE = "startDate";
    private static final String TAG_ENDDATE = "endDate";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_REGION = "region";
    private static final String TAG_URL = "url";

    void getXmlData(){

        try{
            URL url= new URL(ParsingUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("data")) ;// 첫번째 검색결과
                        else if(tag.equals("title")){
                            xpp.next();
                            posts.put(TAG_TITLE,xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        }
                        else if(tag.equals("place")){
                            xpp.next();
                            posts.put(TAG_PLACE,xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        }
                        else if(tag.equals("startDate")){
                            xpp.next();
                            posts.put(TAG_STARTDATE,xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        }
                        else if(tag.equals("endDate")){
                            xpp.next();
                            posts.put(TAG_ENDDATE,xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        }
                        else if(tag.equals("clCd")){
                            xpp.next();
                            posts.put(TAG_CATEGORY,xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        }
                        else if(tag.equals("legalCd")){
                            xpp.next();
                            posts.put(TAG_REGION,xpp.getText());
                            System.out.println("파싱:" + xpp.getText());
                        }
                        else if(tag.equals("jobabaUrl")){
                            xpp.next();
                            posts.put(TAG_URL,xpp.getText());
                            noticeList.add(posts);
                            System.out.println("파싱:" + xpp.getText());
                            System.out.println("해쉬"+posts+"\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType= xpp.next();

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }//getXmlData method....
}
