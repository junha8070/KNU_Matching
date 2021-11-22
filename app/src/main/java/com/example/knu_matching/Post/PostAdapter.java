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

import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.MainActivity;
import com.example.knu_matching.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.GalleryViewHolder> {
    private ArrayList<Post> mDataset;
    private Activity activity;


    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public GalleryViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public PostAdapter(Activity activity, ArrayList<Post> myData){
        mDataset = myData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
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

                if(mDataset.get(temp).getStr_email().equals(((MainActivity)MainActivity.context).mFirebaseAuth.getCurrentUser().getEmail())){
                    Intent intent = new Intent(view.getContext(), Post_Owner_Acticity.class);
                    intent.putExtra("Title", mDataset.get(temp).getStr_Title());
                    intent.putExtra("StartDate", mDataset.get(temp).getStr_StartDate());
                    intent.putExtra("EndDate", mDataset.get(temp).getStr_EndDate());
                    intent.putExtra("Number", mDataset.get(temp).getStr_Number());
                    intent.putExtra("Post", mDataset.get(temp).getStr_post());
                    intent.putExtra("Nickname", mDataset.get(temp).getStr_Nickname());
                    intent.putExtra("Email", mDataset.get(temp).getStr_email());
                    intent.putExtra("Time", mDataset.get(temp).getStr_time());
                    intent.putExtra("Str_Id", mDataset.get(temp).getStr_Id());
                    intent.putExtra("Uri",mDataset.get(temp).getUri());
                    intent.putExtra("Filename", mDataset.get(temp).getStr_filename());
                    view.getContext().startActivity(intent);
                }
                else{
                    Intent intent = new Intent(view.getContext(), Visitor.class);
                    intent.putExtra("Title", mDataset.get(temp).getStr_Title());
                    intent.putExtra("StartDate", mDataset.get(temp).getStr_StartDate());
                    intent.putExtra("EndDate", mDataset.get(temp).getStr_EndDate());
                    intent.putExtra("Number", mDataset.get(temp).getStr_Number());
                    intent.putExtra("Post", mDataset.get(temp).getStr_post());
                    intent.putExtra("Nickname", mDataset.get(temp).getStr_Nickname());
                    intent.putExtra("Email", mDataset.get(temp).getStr_email());
                    intent.putExtra("Time", mDataset.get(temp).getStr_time());
                    intent.putExtra("Str_Id", mDataset.get(temp).getStr_Id());
                    intent.putExtra("Uri",mDataset.get(temp).getUri());
                    intent.putExtra("Filename", mDataset.get(temp).getStr_filename());
                    view.getContext().startActivity(intent);
                }

            }
        });
    }


    @Override
    public int getItemCount(){
        return mDataset.size();
    }



}