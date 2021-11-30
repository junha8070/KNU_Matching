package com.example.knu_matching.Nav;

import static android.content.ContentValues.TAG;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.R;
import com.example.knu_matching.board.ProgressDialog;
import com.example.knu_matching.GetSet.Board;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Scrap_Activity extends AppCompatActivity {
    private static final String TAG_TITLE = "title";
    private static final String TAG_PLACE = "place";
    private static final String TAG_STARTDATE = "startDate";
    private static final String TAG_ENDDATE = "endDate";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_REGION = "region";
    private static final String TAG_URL = "url";
    private static final String TAG_IMGURL = "imgurl";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Parcelable recyclerViewState;
    HashMap<String, String> posts = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> noticeList = new ArrayList<HashMap<String, String>>();
    Board board;
    Scrap_Adapter adapter;
    ProgressDialog customProgressDialog;
    RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

        rv = findViewById(R.id.rv);
        mLinearLayoutManager = new GridLayoutManager(Scrap_Activity.this, 1);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(Scrap_Activity.this);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection("Scrap").document(auth.getCurrentUser().getEmail().replace(".", ">")).collection("activity")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                //    Log.w(TAG, "listen:error", e);
                                    return;
                                }

                                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        //    Log.d(TAG, "Add city: " + dc.getDocument().getData());
                                            board = dc.getDocument().toObject(Board.class);
                                            posts.put("UID", dc.getDocument().getId());
                                            posts.put(TAG_TITLE, board.getStr_title());
                                            posts.put(TAG_STARTDATE, board.getStr_startDate());
                                            posts.put(TAG_ENDDATE, board.getStr_endDate());
                                            posts.put(TAG_PLACE, board.getStr_place());
                                            posts.put(TAG_REGION, board.getStr_region());
                                            posts.put(TAG_URL, board.getStr_url());
                                            noticeList.add(posts);
                                            posts = new HashMap<>();
                                            break;
                                        case MODIFIED:
                                      //      Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                            break;
                                        case REMOVED:
                                       //     Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                            board = dc.getDocument().toObject(Board.class);
                                            posts.put("UID", dc.getDocument().getId());
                                            posts.put(TAG_TITLE, board.getStr_title());
                                            posts.put(TAG_STARTDATE, board.getStr_startDate());
                                            posts.put(TAG_ENDDATE, board.getStr_endDate());
                                            posts.put(TAG_PLACE, board.getStr_place());
                                            posts.put(TAG_REGION, board.getStr_region());
                                            posts.put(TAG_URL, board.getStr_url());
                                            noticeList.remove(posts);
                                            posts.put("UID", dc.getDocument().getId());
                                            posts.remove(board.getStr_title());
                                            posts.remove(board.getStr_startDate());
                                            posts.remove(board.getStr_endDate());
                                            posts.remove(board.getStr_place());
                                            posts.remove(board.getStr_region());
                                            posts.remove(board.getStr_url());
                                            break;
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                recyclerViewState = rv.getLayoutManager().onSaveInstanceState();
                                        for (int i = 0; i < noticeList.size(); i++) {
                                    //        System.out.println("액티비티" + noticeList.get(i));
                                        }
                                        adapter = new Scrap_Adapter(Scrap_Activity.this, noticeList);
                                        rv.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        customProgressDialog.cancel();
                                    }
                                });

                            }
                        });

            }
        }).start();

    }
}