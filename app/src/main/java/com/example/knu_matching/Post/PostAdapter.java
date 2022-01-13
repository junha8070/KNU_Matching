package com.example.knu_matching.Post;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.MainActivity;
import com.example.knu_matching.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.GalleryViewHolder> {
    private ArrayList<Post> mDataset;
    private Activity activity;

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView tv_field;

        public GalleryViewHolder(CardView v){
            super(v);
            cardView = v;
            this.tv_field=v.findViewById(R.id.tv_field);

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

        TextView tv_field = cardView.findViewById(R.id.tv_field);
        tv_field.setText(mDataset.get(position).getStr_field());
        
        TextView dateView = cardView.findViewById(R.id.dateView);
        String timeFormmat = mDataset.get(position).getStr_time();
        dateView.setText(timeFormmat.substring(0,4)+"년 "+timeFormmat.substring(5,7)+"월 "+timeFormmat.substring(8,10)+"일");
        String startday=timeFormmat.substring(0,4)+"/"+timeFormmat.substring(5,7)+"/"+timeFormmat.substring(8,10);

        String str = mDataset.get(position).getStr_EndDate();
        String[] result= new String[3];
        result = str.split(". ");
        String result0=result[0];
        String result1=result[1];
        String result2=result[2];

        String endday=result0+"/"+result1+"/"+result2;

        Date format1 = null;
        try {
            format1 = new SimpleDateFormat("yyyy/MM/dd").parse(endday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date format2 = null;
        try {
            format2 = new SimpleDateFormat("yyyy/MM/dd").parse(startday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
        long diffMin = (format1.getTime() - format2.getTime()) / 60000; //분 차이
        long diffHor = (format1.getTime() - format2.getTime()) / 3600000; //시 차이
        if(diffSec<0){
            long diffDays = diffSec / (24*60*60); //일자수 차이
            diffDays=-diffDays;
            String final_day="D+"+String.valueOf(diffDays);
            TextView tv_Dday=cardView.findViewById(R.id.tv_Dday);
            tv_Dday.setText(final_day);
        }
        else{
            long diffDays = diffSec / (24*60*60); //일자수 차이
            String final_day="D-"+String.valueOf(diffDays);
            TextView tv_Dday=cardView.findViewById(R.id.tv_Dday);
            tv_Dday.setText(final_day);
        }

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
                    intent.putExtra("Link", mDataset.get(temp).getStr_link());
                    intent.putExtra("Uid", mDataset.get(temp).getStr_uid());
                    intent.putExtra("field", mDataset.get(temp).getStr_field());

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
                    intent.putExtra("Link", mDataset.get(temp).getStr_link());
                    intent.putExtra("Uid", mDataset.get(temp).getStr_uid());
                    intent.putExtra("field", mDataset.get(temp).getStr_field());

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