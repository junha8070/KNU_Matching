package com.example.knu_matching.Recruitment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.Post.CommentAdapter;
import com.example.knu_matching.Post.ParticipateUser;
import com.example.knu_matching.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RecruitmentAdapter extends RecyclerView.Adapter<RecruitmentAdapter.ViewHolder> {
    private ArrayList<ParticipateUser> mData = null;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public RecruitmentAdapter(ArrayList<ParticipateUser> data){
        mData = data;
    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public RecruitmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recruitment_item, parent, false);
        RecruitmentAdapter.ViewHolder vh = new RecruitmentAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull RecruitmentAdapter.ViewHolder holder, int position) {
        ParticipateUser item = mData.get(position);

        //str_Email = item.getStr_Email();
        holder.tv_nickname.setText(item.getStr_participate_Nickname());
        holder.tv_major.setText(item.getStr_participate_Major());
        holder.tv_studentID.setText(item.getStr_participate_StudentId());
    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nickname, tv_major, tv_studentID;

        ViewHolder(View itemView){
            super(itemView);

            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_major = itemView.findViewById(R.id.tv_major);
            tv_studentID = itemView.findViewById(R.id.tv_studentID);

            System.out.println("현재 이메일 Adapter"+auth.getCurrentUser().getEmail());
        }
    }

}
