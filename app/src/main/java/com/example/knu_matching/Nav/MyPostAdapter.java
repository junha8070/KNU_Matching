package com.example.knu_matching.Nav;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.People.PeolpeFragment;
import com.example.knu_matching.Post.ParticipateUser;
import com.example.knu_matching.Post.PostAdapter;
import com.example.knu_matching.Post.Post_Owner_Acticity;
import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.WebView;
import com.example.knu_matching.chatting.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.RecyclerViewHolders> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;
    private ArrayList<Post> mDataset;
    int count = 0;
    ArrayList<String> arr_participated_uid = new ArrayList<String>();


    public MyPostAdapter(Context context, ArrayList<Post> myData){
        this.mDataset = myData;
        this.context = context;
        System.out.println("리스트" + mDataset.get(0).getStr_uid());
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypost, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(v);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolders holder, int position) {
        db.collection("Post").document(mDataset.get(position).getStr_Id()).collection("Participate")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int i = 0;
                int j = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Visitor", document.getId() + " => " + document.getData());
                        j = i + 1;
                        i++;
                    }
                    count = j;
                    holder.tv_title.setText(mDataset.get(position).getStr_Title());
                    holder.tv_date.setText(mDataset.get(position).getStr_time());
                    holder.tv_content.setText(mDataset.get(position).getStr_post());
                    holder.tv_participate.setText(Integer.toString(count));
                    System.out.println("숫자"+count);
                    if(mDataset.get(position).getStr_Number().equals(0)){
                        holder.btn_chat.setEnabled(false);
                    }
                } else {
                    Log.d("Visitor", "Error getting documents: ", task.getException());
                }
            }


        });

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Post_Owner_Acticity.class);
                intent.putExtra("Title", mDataset.get(position).getStr_Title());
                intent.putExtra("StartDate", mDataset.get(position).getStr_StartDate());
                intent.putExtra("EndDate", mDataset.get(position).getStr_EndDate());
                intent.putExtra("Number", mDataset.get(position).getStr_Number());
                intent.putExtra("Post", mDataset.get(position).getStr_post());
                intent.putExtra("Nickname", mDataset.get(position).getStr_Nickname());
                intent.putExtra("Email", mDataset.get(position).getStr_email());
                intent.putExtra("Time", mDataset.get(position).getStr_time());
                intent.putExtra("Str_Id", mDataset.get(position).getStr_Id());
                intent.putExtra("Uri",mDataset.get(position).getUri());
                intent.putExtra("Filename", mDataset.get(position).getStr_filename());
                intent.putExtra("Link", mDataset.get(position).getStr_link());
                intent.putExtra("Uid", mDataset.get(position).getStr_uid());

                v.getContext().startActivity(intent);
            }
        });

        holder.btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("scrap strid "+mDataset.get(position).getStr_Id());

                db.collection("Post").document(mDataset.get(position).getStr_Id()).collection("Participate")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            System.out.println("mypost adapter "+documentSnapshot.getId());
                            //참여자 이메일 쫙 나올거임

                            db.collection("Post").document(mDataset.get(position).getStr_Id())
                                    .collection("Participate").document(documentSnapshot.getId())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    ParticipateUser participateUser = task.getResult().toObject(ParticipateUser.class);
//                                    System.out.println("mypost adapter uid "+participateUser.getStr_participate_Uid());
//                                    System.out.println("mypost adapter task "+task.getResult().getData());
//                                    System.out.println("mypost adapter task "+task.getResult().toString());
                                    System.out.println("mypost adapter uid " + task.getResult().get("str_participate_Uid").toString());
                                    arr_participated_uid.add(task.getResult().get("str_participate_Uid").toString());
                                    arr_participated_uid.add(auth.getUid());
                                    System.out.println("mypost adapter participated_uid " + arr_participated_uid);
                                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                                    System.out.println("myPostAdapter to chatactivity " + arr_participated_uid);
                                    intent.putExtra("participated_uid", arr_participated_uid);
                                    intent.putExtra("isMyPost", true);
                                    intent.putExtra("Number", mDataset.get(position).getStr_Number());
                                    intent.putExtra("roomName", mDataset.get(position).getStr_Title());
                                    v.getContext().startActivity(intent);

                                }
                            });

                        }
                    }
                });

            }


        });


    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView tv_title, tv_date, tv_content, tv_participate;
        CardView cv;
        Button btn_chat;
        public RecyclerViewHolders(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_participate = itemView.findViewById(R.id.tv_participate);
            btn_chat = itemView.findViewById(R.id.btn_Chat);
            cv = itemView.findViewById(R.id.cv);


        }
    }
}
