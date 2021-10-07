package com.example.knu_matching.main.board;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetThumb {
    String thumb_url;
    @SuppressLint("StaticFieldLeak")
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
                        thumb_url = tag.attr("content");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
