package com.example.knu_matching.Nav;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.GetSet.Participate;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.RecyclerViewHolders> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;
    private ArrayList<Post> mDataset;
    private String[] listArr;
    int count = 0;
    List<String> mSelectedItems;
    ArrayList<String> arr_participated_uid = new ArrayList<String>();
    int q =0;


    public MyPostAdapter(Context context, ArrayList<Post> myData){
        this.mDataset = myData;
        this.context = context;
       // System.out.println("리스트" + mDataset.get(0).getStr_uid());
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
                 //       Log.d("Visitor", document.getId() + " => " + document.getData());
                        j = i + 1;
                        i++;
                    }
                    count = j;
                    holder.tv_title.setText(mDataset.get(position).getStr_Title());
                    String timeFormmat = mDataset.get(position).getStr_time();
                    holder.tv_date.setText(timeFormmat.substring(0,4)+"년 "+timeFormmat.substring(5,7)+"월 "+timeFormmat.substring(8,10)+"일");
                    holder.tv_content.setText(mDataset.get(position).getStr_post());
                    holder.tv_participate.setText(Integer.toString(count));
                 //   System.out.println("숫자"+count);
                    if(mDataset.get(position).getStr_Number().equals(0)){
                        holder.btn_chat.setEnabled(false);
                    }
                } else {
               //     Log.d("Visitor", "Error getting documents: ", task.getException());
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
                System.out.println("템프 게시판 아이디"+mDataset.get(position).getStr_Id());
                db.collection("Participate_list").whereEqualTo("str_post_uid",mDataset.get(position).getStr_Id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listArr = new String[task.getResult().size()];
                        for(int i=0;i<task.getResult().size();i++){
                            String tempId = task.getResult().getDocuments().toString();
                            System.out.println("템프"+tempId);
                            String target ="DocumentSnapshot{key=Participate_list/";
                            int target_num = tempId.indexOf(target);
                            String result = tempId.substring(target_num+38,(tempId.substring(target_num).indexOf(", metadata=")+target_num));
                            System.out.println("템프결과"+result);
                            db.collection("Participate_list").document(result).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Participate participate = task.getResult().toObject(Participate.class);
                                    System.out.println("포스트 아답터 디버깅1"+participate.getStr_user_email());
                                    System.out.println("포스트 아답터 디버깅2"+participate.getStr_post_uid());
                                    System.out.println("포스트 아답터 디버깅3"+participate.getStr_user_uid());
                                    listArr[q] = participate.getStr_user_email();
                                    q++;

//                                    db.collection("Report").whereEqualTo("str_comment_email",participate.getStr_user_email()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            int level = task.getResult().size();
//                                            listArr[q] = String.valueOf(level);
//                                            System.out.println("레벨"+level);
//                                            q++;
//                                        }
//                                    });
                                }
                            });
                        }
                        q=0;
//                        System.out.println("리스트 사이트"+listArr.length);
////                        System.out.println("배열 사이즈"+listArr.length);
//                        boolean[] bol = new boolean[listArr.length];
//                        for(int i=0;i<listArr.length;i++) {
//                            bol[i] = false;
//                        }
//                        if(listArr.length>0){
//                            final ArrayList<String> mSelectedItems = new ArrayList<String>();
//                            System.out.println("리스트 사이트2"+listArr.length);
//                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                            System.out.println("리스트 사이트3"+listArr.length);
//                            dialog.setTitle("팀원을 선택해주세요")
//                                    .setMultiChoiceItems(
//                                            listArr,
//                                            bol,
//                                            new DialogInterface.OnMultiChoiceClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                                                    System.out.println("리스트 사이트4"+listArr.length);
//                                                    if(isChecked == true) // Checked 상태일 때 추가
//                                                    {
//                                                        mSelectedItems.add(listArr[which]);
//                                                    }
//                                                    else				  // Check 해제 되었을 때 제거
//                                                    {
//                                                        mSelectedItems.remove(which);
//                                                    }
//                                                }
//                                            }
//
//                                    );
//                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
//                                @Override
//                                public void onClick(DialogInterface dialog, int pos)
//                                {
//                                    String SeletedItemsString = "";
//
//                                    for(int i =0; i<mSelectedItems.size();i++)
//                                    {
//                                        SeletedItemsString =  SeletedItemsString + "," + mSelectedItems.get(i);
//                                    }
//
//                                    Toast toast = Toast.makeText(context, "선택 된 항목은 :" + SeletedItemsString,Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                }
//                            });
//
//                            AlertDialog alertDialog = dialog.create();
//                            alertDialog.show();
//                        }else{
//
//                        }

                    }
                });
//                System.out.println("scrap strid "+mDataset.get(position).getStr_Id());



            }


        });


    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        db.collection("Post").document(mDataset.get(position).getStr_Id()).collection("Participate")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                    System.out.println("mypost adapter "+documentSnapshot.getId());
//                    //참여자 이메일 쫙 나올거임
//
//                    db.collection("Post").document(mDataset.get(position).getStr_Id())
//                            .collection("Participate").document(documentSnapshot.getId())
//                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                                    ParticipateUser participateUser = task.getResult().toObject(ParticipateUser.class);
////                                    System.out.println("mypost adapter uid "+participateUser.getStr_participate_Uid());
////                                    System.out.println("mypost adapter task "+task.getResult().getData());
////                                    System.out.println("mypost adapter task "+task.getResult().toString());
//                            //      System.out.println("mypost adapter uid " + task.getResult().get("str_participate_Uid").toString());
//                            arr_participated_uid.add(task.getResult().get("str_participate_Uid").toString());
//                            arr_participated_uid.add(auth.getUid());
//                            //   System.out.println("mypost adapter participated_uid " + arr_participated_uid);
//                            Intent intent = new Intent(v.getContext(), ChatActivity.class);
//                            //   System.out.println("myPostAdapter to chatactivity " + arr_participated_uid);
//                            intent.putExtra("participated_uid", arr_participated_uid);
//                            intent.putExtra("isMyPost", true);
//                            intent.putExtra("roomName", mDataset.get(position).getStr_Title());
//                            v.getContext().startActivity(intent);
//
//                        }
//                    });
//
//                }
//            }
//        });
//    }

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
