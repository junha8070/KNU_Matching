package com.example.knu_matching;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.textView);
        textView.setText(mDataset.get(position).getStr_Title());
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }



}
