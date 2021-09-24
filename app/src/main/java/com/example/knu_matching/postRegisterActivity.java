package com.example.knu_matching;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class postRegisterActivity extends RecyclerView.Adapter<postRegisterActivity.GalleryViewHolder> {
    private ArrayList<postInfo> mDataset;
    private Activity activity;

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public GalleryViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public postRegisterActivity (Activity activity,ArrayList<postInfo> myData){
        mDataset = myData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_post_register,parent,false);
        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.textView);
        textView.setText(mDataset.get(position).getStr_Title());
        TextView dateView = cardView.findViewById(R.id.dateView);
        dateView.setText((mDataset.get(position).getStr_time().substring(0,12)));
    }

//    @Override
//    public void onBindViewHolder(@NonNull final GalleryViewHolder holder,int position){
//        CardView cardView = holder.cardView;
//        TextView textView = cardView.findViewById(R.id.textView);
//        textView.setText(mDataset.get(position).getStr_Title());
//        TextView dateView = cardView.findViewById(R.id.dateView);
//        dateView.setText((mDataset.get(position).getStr_time().substring(0,12)));
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }



}
