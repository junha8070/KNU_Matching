package com.example.knu_matching.People;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.chatting.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeolpeFragment extends Fragment {
    private ArrayList<String> user_arrayList;
    private String uid, strNick, str_chatroom_name, strUid;
    private String chatRoomUid, find_value, strEmail;
    boolean chatin, ischeckfriend, iswritename, isexistfriend;
    private ArrayList<String> arr_participated_uid = new ArrayList<>();
    public Button btn_friend;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String checkMe_Email, checkMe_nickname;
    ImageButton btn_invite;
    EditText chatroom_name;

    public PeolpeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_peolpe_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID
        btn_invite = (ImageButton) view.findViewById(R.id.btn_invite);
        chatroom_name = (EditText) view.findViewById(R.id.chatroom_name);
        btn_friend = (Button) view.findViewById(R.id.btn_friend);

        return view;

    }

    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<UserAccount.friend> userModels;
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<String> arrNick = new ArrayList<String>();

        Context context;

        public PeopleFragmentRecyclerViewAdapter(ArrayList<String> arrayList, ArrayList<String> arrNick, Context context) {
            this.arrNick = arrNick;
            this.arrayList = arrayList;
            this.context = context;
        }

        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            arrayList.add(myUid);
            //내 UID
            FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("friend")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                Toast.makeText(getContext(), "아", Toast.LENGTH_SHORT).show();
                                System.out.println("test 아아아아아");
                                db.collection("Account").whereEqualTo("uid", myUid)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                        UserAccount userAccount = documentSnapshot.toObject(UserAccount.class);
                                                        UserAccount.friend friend = new UserAccount.friend();
                                                        friend.nickname = userAccount.getNickName();
                                                        friend.uid = userAccount.getUid();
                                                        friend.email = userAccount.getEmailId();
                                                        friend.stdId = userAccount.getStudentId();

                                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("friend").push().setValue(friend);
                                                        return;
                                                    }
                                                }
                                            }
                                        });

                            } else {
                                userModels.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    UserAccount userAccount = snapshot.getValue(UserAccount.class);
                                    System.out.println("test getuid " + userAccount.getUid());
                                    System.out.println("test myUid " + myUid);
                                    UserAccount.friend friend = snapshot.getValue(UserAccount.friend.class);
                                    System.out.println("test nickname" + friend.getNickname());
                                    arrNick.add(friend.getNickname());

                                    userModels.add(friend);
                                }
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            System.out.println("test arrNick " + arrNick);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            btn_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("학번입력");
                    alert.setMessage("학번을 입력해주세요");

                    final EditText input = new EditText(v.getContext());
                    alert.setView(input);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            find_value = input.getText().toString();
                            //find_value는 학번임

                            db.collection("Account")
                                    .whereEqualTo("studentId", find_value)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    UserAccount userAccount = document.toObject(UserAccount.class);
                                                    if (find_value.equals(userAccount.getStudentId())) {
                                                        Toast.makeText(getContext(), "존재함", Toast.LENGTH_SHORT).show();
                                                        //존재하는 학번임 -> 이미 친추 되어있는지 확인하기 위해 userAccount의 email값 넘겨서 확인
                                                        System.out.println("피플테테스스트트 userAccount.getEmail " + userAccount.getEmailId());
                                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("friend").orderByChild("email")
                                                                .equalTo(userAccount.getEmailId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                System.out.println("피플프래그먼트" + snapshot.getValue());
                                                                if (snapshot.getValue() != null) {
                                                                    System.out.println("피플프래그먼트 까꿍");
                                                                    //이미 친구임
                                                                } else {
                                                                    System.out.println("피플프래그먼트 까꿍2");
                                                                    System.out.println("testPle myUid " + uid);
                                                                    UserAccount.friend friend = new UserAccount.friend();
                                                                    friend.stdId = userAccount.getStudentId();
                                                                    friend.uid = userAccount.getUid();
                                                                    friend.email = userAccount.getEmailId();
                                                                    friend.nickname = userAccount.getNickName();

                                                                    //다이어로그 버튼 눌렀을 때 학번 DB에 존재한다면 친구 추가해야함
                                                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("friend").push().setValue(friend);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                System.out.println("피플프래그먼트 없데요");
                                                            }
                                                        });

                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alert.show();

                }
            });

            db.collection("Account").whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    UserAccount userAccount = documentSnapshot.toObject(UserAccount.class);
                                    checkMe_nickname = userAccount.getNickName();
                                    System.out.println("checkMe_nickname " + checkMe_nickname);
                                    return;
                                }
                            }
                        }
                    });
//
            System.out.println("testtest " + userModels.get(0).getNickname());  //0 번째는 무조건 자기 자신
//            if (!(String.valueOf(userModels.get(0).getNickname()).equals(checkMe_nickname))) {
//                ((CustomViewHolder) holder).cbox_invite.setVisibility(View.GONE);
//                // textView에 달려있는 모든 text가 다 바뀜
//                //처음엔 이거 나옴 근데 하나 더 친구를 추가하는 순간에 아래의 else로 감
//            } else {
//                if (String.valueOf(position).equals("0")) {
//
//                    ((CustomViewHolder) holder).textView.setText("친구와 대화를 시작해보세요!");
//                } else {
//                    ((CustomViewHolder) holder).textView.setText(userModels.get(position).getNickname());
//                }
//            }

            if (position == 0) {
                ((CustomViewHolder) holder).cbox_invite.setVisibility(View.GONE);
                ((CustomViewHolder) holder).textView.setText("친구와 대화를 시작해보세요!");

            } else {
                ((CustomViewHolder) holder).textView.setText(userModels.get(position).getNickname());
            }
            if (userModels != null && userModels.size() > 0) {
                //    System.out.println("test 11111111111111 ");
                //    System.out.println("test arraylist " + arrayList);
                ((CustomViewHolder) holder).cbox_invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CustomViewHolder) holder).cbox_invite.isChecked()) {

                            strUid = userModels.get(position).getUid();
                            arrNick.add(userModels.get(position).getNickname());
                            arrayList.add(strUid);
                        } else {
                            strUid = userModels.get(position).getUid();
                            arrNick.remove(userModels.get(position).getNickname());
                            arrayList.remove(strUid);
                        }

                        if (arrayList.size() == 1) {
                            ischeckfriend = false;
                        } else {
                            ischeckfriend = true;
                        }

                        //         System.out.println("test8 arraylist " + arrayList);
                        //         System.out.println("test8 arrNick " + arrNick);
                        //         System.out.println("test8 arrayList.size() " + arrayList.size());
                        //         System.out.println("test8 ischeckfriend " + ischeckfriend);
                    }
                });
            }

            chatroom_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    str_chatroom_name = chatroom_name.getText().toString();
                    if (str_chatroom_name.trim().equals("")) {
                        iswritename = false;
                    } else {
                        iswritename = true;
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            btn_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iswritename == true && ischeckfriend == true) {
                        //         System.out.println("test arraylist_btn_click " + arrayList);
                        //        System.out.println("test iswritename " + iswritename);
                        //        System.out.println("test ischeckfriend " + ischeckfriend);
                        //       System.out.println("test ChatRoomName " + str_chatroom_name);
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);
                        intent.putExtra("invited_List", arrayList);
                        intent.putExtra("arrNick", arrNick);
                        intent.putExtra("chatIn", true);
                        intent.putExtra("chatRoom_name", str_chatroom_name);
                        ActivityOptions activityOptions = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright, R.anim.toleft);
                            startActivity(intent, activityOptions.toBundle());
                        }
                    } else {
                        if (ischeckfriend == false) {
                            Toast.makeText(getContext(), "초대할 사람을 체크해주세요", Toast.LENGTH_SHORT).show();
                        }
                        if (iswritename == false) {
                            Toast.makeText(getContext(), "채팅방 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        private boolean isAlreadyFriend(String email) {

            //이미 친구인가?
            //
//            db.collection("Account").document(email.replace(".", ">")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    UserAccount userAccount = task.getResult().toObject(UserAccount.class);
//                    strEmail = userAccount.getEmailId();
//                }
//            });


//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    UserAccount.friend user = snapshot.getValue(UserAccount.friend.class);
//                    System.out.println("테테스스트트트 "+snapshot.child("email").getValue());
////                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
////                        System.out.println("테테스스트트트 "+dataSnapshot.getValue());
////                        dataSnapshot.getChildren().;
////                        System.out.println("테테스스트트트트트트 "+dataSnapshot.getValue());
////                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            return isexistfriend;
        }


        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public CheckBox cbox_invite;


            public CustomViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.frienditem_textview);
                cbox_invite = (CheckBox) view.findViewById(R.id.cbox_invite);

            }
        }
    }
}