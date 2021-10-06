package com.example.knu_matching.main;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class FourthAdapter extends RecyclerView.Adapter<FourthAdapter.ViewHolder> {
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
    public FourthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FourthAdapter.ViewHolder holder, int position) {
        HashMap<String, String> noticeItem = noticeList.get(position);
        holder.tv_title.setText(noticeItem.get("title")); //작성자
        holder.tv_place.setText(noticeItem.get("place")); //작성자
        holder.tv_region.setText(noticeItem.get("region")); //작성자
        holder.tv_startDate.setText(noticeItem.get("startDate")); //작성자
        holder.tv_endDate.setText(noticeItem.get("endDate")); //작성자
        System.out.println("이미지 주소 추적"+noticeItem.get("imgurl"));
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_place;
        TextView tv_region;
        TextView tv_startDate;
        TextView tv_endDate;
        CardView cv;
        ImageView iv_url;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_place = (TextView) v.findViewById(R.id.tv_place);
            tv_region = (TextView) v.findViewById(R.id.tv_region);
            tv_startDate = (TextView) v.findViewById(R.id.tv_startDate);
            tv_endDate = (TextView) v.findViewById(R.id.tv_endDate);
            iv_url = (ImageView) v.findViewById(R.id.iv_url);
            cv = (CardView) v.findViewById(R.id.cv);
        }
    }


}