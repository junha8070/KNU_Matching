package com.example.knu_matching.Post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.MainActivity;
import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<CommentItem> mData = null;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    String str_Email;
    String str_Uid;
    String str_post_uid;
    String str_Id;
    String str_Comment_uid;
    String str_Date;
    int str_count;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rv;
    public CommentAdapter(ArrayList<CommentItem> data){
        mData = data;
    }
    Context context;


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
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommentItem item = mData.get(position);
        System.out.println("comment item position"+position);

        item.setStr_count(position);

        str_Email = item.getStr_Email();
        str_Uid = item.getStr_Uid();
        str_post_uid = item.getStr_Post_uid();
        str_Comment_uid = item.getStr_Comment_uid();
        str_Date = item.getStr_Date();
        str_count =item.getStr_count();

        holder.tv_nickname.setText(item.getStr_NickName());
        holder.tv_content.setText(item.getStr_Content());
        holder.tv_date.setText(item.getStr_Date());
        if((str_Email.equals(auth.getCurrentUser().getEmail()))==false){
            holder.btn_del_comment.setVisibility(View.GONE);
        }
        System.out.println("comment item str_Uid "+str_Uid);
        System.out.println("comment item str_Email "+str_Email);
        System.out.println("comment item str_Comment_uid "+str_Comment_uid);
        System.out.println("comment item str_post_uid "+str_post_uid);
        System.out.println("comment item str_count "+str_count);
        System.out.println("comment item str_Date "+str_Date);
        System.out.println("\n ");

        holder.btn_del_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Post").document(str_post_uid).collection("Comment").orderBy("str_Date", Query.Direction.ASCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Integer cnt=0;
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    System.out.println("same comment getId "+ documentSnapshot.getId());
                                    System.out.println("same comment cnt "+ cnt);
                                    System.out.println("same comment uid "+ str_Comment_uid);
                                    System.out.println("same comment position "+ position);
                                    System.out.println("same comment str_count "+ str_count);

                                    if(cnt.equals(position)){
                                        System.out.println("same comment uid db " + documentSnapshot.getId());
                                        str_Comment_uid = documentSnapshot.getId();
                                        System.out.println("same comment uid "+ str_Comment_uid);
                                        db.collection("Post").document(str_post_uid)
                                                .collection("Comment").document(str_Comment_uid)
                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    }
                                    cnt++;
                                }
                                cnt=0;
                            }
                        });
                delItem(position);
            }
        });

    }

    private int position;

    // 댓글 삭제 눌렀을때 작동
    private void delItem(int position) {
        System.out.println("position "+position);
        mData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nickname, tv_content, tv_date;
        ImageButton btn_report, btn_del_comment;


        ViewHolder(View itemView){
            super(itemView);

            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
//            btn_comment = itemView.findViewById(R.id.btn_comment);
            btn_report = itemView.findViewById(R.id.btn_report);
            btn_del_comment = itemView.findViewById(R.id.btn_del_comment);

            System.out.println("현재 이메일 Adapter"+auth.getCurrentUser().getEmail());
            System.out.println("댓글 이메일 Adapter"+str_Email);
        }

    }

}
