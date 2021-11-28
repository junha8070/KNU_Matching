package com.example.knu_matching.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<CommentItem> mData = null;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    String str_Email, str_Uid;

    public CommentAdapter(ArrayList<CommentItem> data){
        mData = data;
    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.post_owner_comment_item, parent, false);
        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        CommentItem item = mData.get(position);

        str_Email = item.getStr_Email();
        str_Uid = item.getStr_Uid();
        holder.tv_nickname.setText(item.getStr_NickName());
        holder.tv_content.setText(item.getStr_Content());
        holder.tv_date.setText(item.getStr_Date());
        if((str_Email.equals(auth.getCurrentUser().getEmail()))==false){
            holder.btn_del.setVisibility(View.GONE);
        }
    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nickname, tv_content, tv_date;
        ImageButton btn_comment,btn_report, btn_del;


        ViewHolder(View itemView){
            super(itemView);

            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
//            btn_comment = itemView.findViewById(R.id.btn_comment);
            btn_report = itemView.findViewById(R.id.btn_report);
            btn_del = itemView.findViewById(R.id.btn_del);

            System.out.println("현재 이메일 Adapter"+auth.getCurrentUser().getEmail());
            System.out.println("댓글 이메일 Adapter"+str_Email);
        }
    }

}
