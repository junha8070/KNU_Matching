package com.example.knu_matching;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.GalleryViewHolder> {
    private ArrayList<postInfo> mDataset;
    private Activity activity;


    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public GalleryViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public AdapterActivity (Activity activity,ArrayList<postInfo> myData){
        mDataset = myData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterActivity.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_post,parent,false);
        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder,int position){
        int temp = position;
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.textView);
        textView.setText(mDataset.get(position).getStr_Title());
        TextView dateView = cardView.findViewById(R.id.dateView);
        dateView.setText((mDataset.get(position).getStr_time().substring(0,12)));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), postRegisterActivity.class);
                intent.putExtra("Title", mDataset.get(temp).getStr_Title());
                intent.putExtra("Date", mDataset.get(temp).getStr_date());
                intent.putExtra("Number", mDataset.get(temp).getStr_Number());
                intent.putExtra("Post", mDataset.get(temp).getStr_post());
                intent.putExtra("Nickname", mDataset.get(temp).getStr_Nickname());
                intent.putExtra("Email", mDataset.get(temp).getStr_email());
                intent.putExtra("Time", mDataset.get(temp).getStr_time());
                intent.putExtra("Id", mDataset.get(temp).getStr_Id());
                intent.putExtra("Application", mDataset.get(temp).getStr_application());
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount(){
        return mDataset.size();
    }



}