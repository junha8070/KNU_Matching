package com.example.knu_matching.main.board;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.R;
import com.example.knu_matching.WebView;
import com.example.knu_matching.main.GetSet.Board;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.RecyclerViewHolders> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    Context context;
    ArrayList<HashMap<String, String>> noticeList; //공지사항 정보 담겨있음
    HashMap<String, String> noticeItem;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public BoardAdapter(Context context, ArrayList<HashMap<String, String>> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
        System.out.println("리스트" + noticeList);
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(v);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        String region = "";
        noticeItem = noticeList.get(position);
        holder.tv_title.setText(noticeItem.get("title")); //작성자
        holder.tv_place.setText(noticeItem.get("place")); //작성자
        RegionCode regionCode = new RegionCode();
        if (noticeItem.get("region") == null) {
            region = "사이트 조회 요망";
        } else {
            region = regionCode.getRegion(noticeItem.get("region"));
        }
        holder.tv_region.setText(region); //작성자
        holder.tv_startDate.setText(noticeItem.get("startDate")); //작성자
        holder.tv_endDate.setText(noticeItem.get("endDate")); //작성자
        holder.jobabaUrl = noticeItem.get("url");
    }

    @Override
    public int getItemCount() {
        return this.noticeList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_place;
        TextView tv_region;
        TextView tv_startDate;
        TextView tv_endDate;
        String jobabaUrl;
        CardView cv;
        Button btn_scrap, btn_recruit;
        private long btnPressTime = 0;

        public RecyclerViewHolders(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);
            tv_region = (TextView) itemView.findViewById(R.id.tv_region);
            tv_startDate = (TextView) itemView.findViewById(R.id.tv_startDate);
            tv_endDate = (TextView) itemView.findViewById(R.id.tv_endDate);
            btn_scrap = itemView.findViewById(R.id.btn_scrap);
            btn_recruit = itemView.findViewById(R.id.btn_post);
            cv = (CardView) itemView.findViewById(R.id.cv);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cv.getContext(), WebView.class);
                    intent.putExtra("url", jobabaUrl);
                    cv.getContext().startActivity(intent);
                }
            });

            btn_scrap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    db.collection("Scrap")
                            .document(mFirebaseAuth.getCurrentUser().getEmail().replace(".", ">"))
                            .collection("activity")
                            .whereEqualTo("str_title", noticeList.get(getLayoutPosition()).get("title")).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Board findboard = document.toObject(Board.class);
                                            Log.d("db디버깅", findboard.getStr_title());
                                            Toast.makeText(cv.getContext(), "이미 스크랩 하셨습니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    Board board = new Board();
                                    board.setStr_title(noticeList.get(getLayoutPosition()).get("title"));
                                    board.setStr_place(noticeList.get(getLayoutPosition()).get("place"));
                                    board.setStr_region(noticeList.get(getLayoutPosition()).get("region"));
                                    board.setStr_startDate(noticeList.get(getLayoutPosition()).get("startDate"));
                                    board.setStr_endDate(noticeList.get(getLayoutPosition()).get("endDate"));
                                    board.setStr_url(noticeList.get(getLayoutPosition()).get("url"));
                                    db.collection("Scrap")
                                            .document(mFirebaseAuth.getCurrentUser().getEmail().replace(".", ">"))
                                            .collection("activity")
                                            .add(board);
                                    Toast.makeText(cv.getContext(), "스크랩 되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            btn_recruit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(cv.getContext(), "게시판으로 연동", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}