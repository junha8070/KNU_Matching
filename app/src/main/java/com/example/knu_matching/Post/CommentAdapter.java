package com.example.knu_matching.Post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.GetSet.Post;
import com.example.knu_matching.GetSet.Report;
import com.example.knu_matching.MainActivity;
import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.membermanage.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    String TAG = "CommentAdapter";
    private ArrayList<CommentItem> mData = null;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    String str_Email;
    String str_Uid;
    String str_post_uid;
    String str_Id;
    static String str_Comment_uid;
    String str_Date;
    int str_count;
    LinearLayout layout_comment;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rv;

    public CommentAdapter(ArrayList<CommentItem> data) {
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
        System.out.println("comment item position" + position);
        System.out.println("comment item " + item.getStr_Comment_uid());

        item.setStr_count(position);

        str_Email = item.getStr_Email();
        str_Uid = item.getStr_Uid();
        str_post_uid = item.getStr_Post_uid();
        str_Comment_uid = item.getStr_Comment_uid();
        str_Date = item.getStr_Date();
        str_count = item.getStr_count();

        if(position==0){
            holder.tv_content.setVisibility(View.GONE);
            holder.tv_date.setVisibility(View.GONE);
            holder.btn_del_comment.setVisibility(View.GONE);
            holder.tv_nickname.setText(item.getStr_NickName()+"님의 글 입니다.");
        }
        else{

            holder.tv_nickname.setText(item.getStr_NickName());
            holder.tv_content.setText(item.getStr_Content());
            holder.tv_date.setText(item.getStr_Date());
        }
        if ((str_Email.equals(auth.getCurrentUser().getEmail())) == false) {
            holder.btn_del_comment.setVisibility(View.GONE);
        }
        if ((str_Email.equals(auth.getCurrentUser().getEmail())) == true) {
            holder.btn_report.setVisibility(View.GONE);
        }
        System.out.println("comment item str_Uid " + str_Uid);
        System.out.println("comment item str_Email " + str_Email);
        System.out.println("comment item str_Comment_uid " + str_Comment_uid);
        System.out.println("comment item str_post_uid " + str_post_uid);
        System.out.println("comment item str_count " + str_count);
        System.out.println("comment item str_Date " + str_Date);
        System.out.println("\n ");

        holder.btn_del_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Post").document(str_post_uid).collection("Comment").orderBy("str_Date", Query.Direction.ASCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Integer cnt = 0;
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    System.out.println("same comment getId " + documentSnapshot.getId());
                                    System.out.println("same comment cnt " + cnt);
                                    System.out.println("same comment uid " + str_Comment_uid);
                                    System.out.println("same comment position " + position);
                                    System.out.println("same comment str_count " + str_count);

                                    if (cnt.equals(position)) {
                                        System.out.println("same comment uid db " + documentSnapshot.getId());
                                        str_Comment_uid = documentSnapshot.getId();
                                        System.out.println("same comment uid " + str_Comment_uid);

                                        db.collection("Post").document(str_post_uid)
                                                .collection("Comment").document(str_Comment_uid)
                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    }
                                    cnt++;
                                }
                                cnt = 0;
                            }
                        });
                delItem(position);
            }
        });
        holder.btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Post").document(str_post_uid).collection("Comment").orderBy("str_Date", Query.Direction.ASCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Integer cnt = 0;
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                    System.out.println("btn_report comment getId " + documentSnapshot.getId());
//                                    System.out.println("btn_report comment cnt " + cnt);
//                                    System.out.println("btn_report comment uid " + str_Comment_uid);
//                                    System.out.println("btn_report comment position " + position);
//                                    System.out.println("btn_report comment str_count " + str_count);
                                    //System.out.println("documentSnapshot test" + documentSnapshot.getString(str_Comment_uid));

                                    if (cnt.equals(position)) {
                                        System.out.println("btn_report comment uid db " + documentSnapshot.getId());
                                        str_Comment_uid = documentSnapshot.getId();
                                        System.out.println("btn_report comment uid " + str_count);
                                        System.out.println("btn_report comment uid " + str_Comment_uid);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(layout_comment.getContext());
                                        builder.setTitle("댓글 신고");
                                        builder.setMessage("신고하시겠습니까?");
                                        builder.setNegativeButton("예",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //예 눌렀을때의 이벤트 처리
                                                        Report report = new Report();
                                                        report.setStr_Reporter_uid(auth.getCurrentUser().getUid());
                                                        report.setStr_comment_uid(str_Comment_uid);
                                                        report.setStr_post_uid(str_post_uid);
                                                        report.setStr_comment_email(str_Email);
                                                        CollectionReference checkReport = db.collection("Report");
                                                        checkReport.whereEqualTo("str_comment_uid",str_Comment_uid).whereEqualTo("str_Reporter_uid", auth.getCurrentUser().getUid())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                Report reporter = document.toObject(Report.class);
                                                                                if (auth.getCurrentUser().getUid().equals(reporter.getStr_Reporter_uid())) {
                                                                                    AlertDialog.Builder dlg = new AlertDialog.Builder(layout_comment.getContext());
                                                                                    dlg.setTitle("신고 접수");
                                                                                    dlg.setMessage("이미 접수된 신고입니다.");
                                                                                    dlg.setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            dialog.cancel();
                                                                                        }
                                                                                    }); // 버튼생성 ,default로 close
                                                                                    dlg.setCancelable(false);//뒤로가기버튼 비활성화
                                                                                    dlg.show();
                                                                                    return;
                                                                                }
                                                                            }
                                                                            checkReport.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        checkReport.add(report).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                                                AlertDialog.Builder dlg = new AlertDialog.Builder(layout_comment.getContext());
                                                                                                dlg.setTitle("신고 접수");
                                                                                                dlg.setMessage("신고 접수가 완료되었습니다.");
                                                                                                dlg.setNeutralButton("닫기", new DialogInterface.OnClickListener() {

                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                        dialog.cancel();
                                                                                                    }
                                                                                                }); // 버튼생성 ,default로 close
                                                                                                dlg.setCancelable(false);//뒤로가기버튼 비활성화
                                                                                                dlg.show();
                                                                                            }
                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                dialog.cancel();
                                                                                                Toast.makeText(layout_comment.getContext(), "신고 접수 실패", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(layout_comment.getContext(), "신고 접수에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });

                                        builder.setPositiveButton("아니오",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //아니오 눌렀을때의 이벤트 처리
                                                        dialog.cancel();
                                                    }
                                                });

                                        builder.show();
//                                        db.collection("Post").document(str_post_uid)
//                                                .collection("Comment").document(str_Comment_uid)
//                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    notifyDataSetChanged();
//                                                }
//                                            }
//                                        });
                                    }
                                    cnt++;
                                }
                                cnt = 0;
                            }
                        });
//                System.out.println("uid값" + mData.get(position).getStr_Comment_uid());
//                deleteDialog(str_Comment_uid);

            }
        });
    }

    private int position;

    // 댓글 삭제 눌렀을때 작동
    private void delItem(int position) {
        System.out.println("position " + position);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nickname, tv_content, tv_date;
        ImageButton btn_report, btn_del_comment;


        ViewHolder(View itemView) {
            super(itemView);

            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
//          btn_comment = itemView.findViewById(R.id.btn_comment);
            btn_report = itemView.findViewById(R.id.btn_report);
            btn_del_comment = itemView.findViewById(R.id.btn_del_comment);
            layout_comment = itemView.findViewById(R.id.layout_comment);

            System.out.println("현재 이메일 Adapter" + auth.getCurrentUser().getEmail());
            System.out.println("댓글 이메일 Adapter" + str_Email);
        }

    }

}
