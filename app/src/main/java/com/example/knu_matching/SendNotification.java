package com.example.knu_matching;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.textclassifier.TextLinks;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(String regToken, String msg, String title){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", msg);
                    dataJson.put("title", title);
                    json.put("notification", dataJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AAAAZNZI4io:APA91bFndhB2kDab40OTaCM3-d9jYBPQ0xt-lAFIpqwF8hDGXvz8p2k9mfXyQ7MZboFj7bDHA2FhVbMf7lRCEOCyStCYVfWZbtrqEBNo1SSWH47hCTJoW1EgDhFo1lRTwZH6mUBj7414")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    Log.d("error", e+"");
                }

                return  null;
            }
        }.execute();
    }
}