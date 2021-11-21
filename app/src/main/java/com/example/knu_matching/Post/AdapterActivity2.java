package com.example.knu_matching.Post;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.R;

import java.util.ArrayList;

public class AdapterActivity2 extends RecyclerView.Adapter<AdapterActivity2.GalleryViewHolder> {
    private ArrayList<postInfo2> Dataset;
    private Activity activity;

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView2;
        public GalleryViewHolder(CardView v){
            super(v);
            cardView2 = v;
        }
    }
    public AdapterActivity2 (Activity activity,ArrayList<postInfo2> dataset){
        this.Dataset = dataset;
        this.activity = activity;
    }

    public AdapterActivity2.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_post2,parent,false);
        final AdapterActivity2.GalleryViewHolder galleryViewHolder = new AdapterActivity2.GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return galleryViewHolder;
    }

    public void onBindViewHolder(@NonNull final AdapterActivity2.GalleryViewHolder holder, int position){

        CardView cardView = holder.cardView2;

        TextView textView = cardView.findViewById(R.id.NicknameView);
        textView.setText(Dataset.get(position).getStr_Nickname2());


        TextView textView1 = cardView.findViewById(R.id.commentView);
        textView1.setText(Dataset.get(position).getStr_comment2());

        TextView dateView = cardView.findViewById(R.id.dateView);
        dateView.setText((Dataset.get(position).getStr_time().substring(0,12)));
        System.out.println("test22" + dateView);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), postRegisterActivity.class);
            }
        });
    }


    @Override
    public int getItemCount(){
        return Dataset.size();
    }



}
