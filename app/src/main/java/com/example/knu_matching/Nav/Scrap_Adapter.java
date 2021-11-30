package com.example.knu_matching.Nav;


import android.content.Context;
import android.content.Intent;
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
import com.example.knu_matching.board.RegionCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Scrap_Adapter extends RecyclerView.Adapter<Scrap_Adapter.RecyclerViewHolders>{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;
    ArrayList<HashMap<String, String>> noticeList; //공지사항 정보 담겨있음
    HashMap<String, String> noticeItem;

    public Scrap_Adapter(Context context, ArrayList<HashMap<String, String>> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    //    System.out.println("리스트" + noticeList);
    }
    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scrap, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(v);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
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
        holder.UID = noticeItem.get("UID");
    }

    @Override
    public int getItemCount() {
        return this.noticeList.size();
    }
    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_place;
        TextView tv_region;
        TextView tv_startDate;
        TextView tv_endDate;
        String jobabaUrl, UID;
        CardView cv;
        Button btn_del_scrap, btn_recruit;
        private long btnPressTime = 0;

        public RecyclerViewHolders(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);
            tv_region = (TextView) itemView.findViewById(R.id.tv_region);
            tv_startDate = (TextView) itemView.findViewById(R.id.tv_startDate);
            tv_endDate = (TextView) itemView.findViewById(R.id.tv_endDate);
            btn_del_scrap = itemView.findViewById(R.id.btn_del_scrap);
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

            btn_del_scrap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
               //     Toast.makeText(cv.getContext(), "삭제버튼", Toast.LENGTH_SHORT).show();
                    db.collection("Scrap").document(auth.getCurrentUser().getEmail().replace(".", ">")).collection("activity")
                            .document(UID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(cv.getContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(cv.getContext(), "삭제하는 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
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
