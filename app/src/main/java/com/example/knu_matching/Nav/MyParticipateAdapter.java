package com.example.knu_matching.Nav;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.Post.PostAdapter;
import com.example.knu_matching.Post.Post_Owner_Acticity;
import com.example.knu_matching.Post.Visitor;
import com.example.knu_matching.R;
import com.example.knu_matching.WebView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MyParticipateAdapter extends RecyclerView.Adapter<MyParticipateAdapter.RecyclerViewHolders> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;
    private ArrayList<Post> mDataset;
    static final ArrayList arrNick = new ArrayList();

    int count = 0;

    public MyParticipateAdapter(Context context, ArrayList<Post> myData){
        this.mDataset = myData;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myparticipate, null);
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

                  //      System.out.println("닉네임"+document.getData().get("str_participate_Nickname"));
                        arrNick.add(document.getData().get("str_participate_Nickname"));
                        j = i + 1;
                        i++;
                    }
                    count = j;
                    holder.tv_title.setText(mDataset.get(position).getStr_Title());
                    holder.tv_date.setText(mDataset.get(position).getStr_time());
                    holder.tv_content.setText(mDataset.get(position).getStr_post());
                    holder.tv_participate.setText(Integer.toString(count));

              //      System.out.println("숫자"+count);
                    String[] arr = new String[count];
                    for(int q=0;q<count;q++){
                        arr[q] = arrNick.get(q).toString();
                 //       System.out.println("숫자값"+arr[q]);
                    }

                } else {
                    Log.d("Visitor", "Error getting documents: ", task.getException());
                }
            }
        });



        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Visitor.class);
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

//                v.getContext().startActivity(intent);
                ((Activity)context).startActivityForResult(intent, 0);
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
        public RecyclerViewHolders(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_participate = itemView.findViewById(R.id.tv_participate);
            cv = itemView.findViewById(R.id.cv);
        }
    }


}
