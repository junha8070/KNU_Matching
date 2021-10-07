package com.example.knu_matching.main;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.knu_matching.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FourthAdapter extends RecyclerView.Adapter<FourthAdapter.RecyclerViewHolders> {
    Context context;
    ArrayList<HashMap<String, String>> noticeList; //공지사항 정보 담겨있음

    String img_url;

    public FourthAdapter(Context context, ArrayList<HashMap<String, String>> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
        System.out.println("리스트"+noticeList);
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(v);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        HashMap<String, String> noticeItem = noticeList.get(position);
        holder.tv_title.setText(noticeItem.get("title")); //작성자
        holder.tv_place.setText(noticeItem.get("place")); //작성자
        holder.tv_region.setText(noticeItem.get("region")); //작성자
        holder.tv_startDate.setText(noticeItem.get("startDate")); //작성자
        holder.tv_endDate.setText(noticeItem.get("endDate")); //작성자
//        System.out.println("이미지 주소 추적"+noticeItem.get("imgurl"));
//        Picasso.get().load(noticeItem.get("url")).into(holder.iv_url);
//        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(noticeItem.get("url"));
//        jsoupAsyncTask.execute();
//        System.out.println("이미지 주소3"+img_url);
//        System.out.println("사진리스트"+arrayList);

    }

    @Override
    public int getItemCount() {
        return this.noticeList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class RecyclerViewHolders extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_place;
        TextView tv_region;
        TextView tv_startDate;
        TextView tv_endDate;
        CardView cv;
        ImageView iv_url;
        ImageButton btn_scrap, btn_recruit;

        public RecyclerViewHolders(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);
            tv_region = (TextView) itemView.findViewById(R.id.tv_region);
            tv_startDate = (TextView) itemView.findViewById(R.id.tv_startDate);
            tv_endDate = (TextView) itemView.findViewById(R.id.tv_endDate);
//            iv_url = (ImageView) v.findViewById(R.id.iv_url);
            cv = (CardView) itemView.findViewById(R.id.cv);
            btn_scrap = (ImageButton) itemView.findViewById(R.id.btn_scrap);
            btn_recruit = (ImageButton) itemView.findViewById(R.id.btn_recruit);

            btn_scrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_scrap.setImageResource(R.drawable.ic_after_scrap);
                    Toast.makeText(itemView.getContext(), "click position"+getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(itemView.getContext(), "click position"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


}