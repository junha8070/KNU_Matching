package com.example.knu_matching.chatting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.MainActivity;
import com.example.knu_matching.R;
//import com.example.knu_matching.SendNotification;
import com.example.knu_matching.SendNotification;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.membermanage.FindIDActivity;
import com.example.knu_matching.membermanage.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.SystemParameterRule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class ChatActivity extends AppCompatActivity {
    private ArrayList<String> user_arrayList;
    private Map<String, String> arr_Nick;
    private Map<String, String> token_List;
    private ImageButton button, chat_in;
    private EditText editText;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrNick = new ArrayList<>();
    ArrayList<String> arr_participated_uid = new ArrayList<>();
    private String uid, chatRoomUid, chatRoomUidd, chatRoomName, nickname;
    private Integer listTagNum, roomNum;
    private RecyclerView recyclerView;
    private Boolean first_chat, isMyPost, chat_list;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String mToken;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    String str_Num, str_roomName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID
        arrayList = getIntent().getStringArrayListExtra("invited_List");
        chat_list = getIntent().getExtras().getBoolean("chat_list");
        chatRoomName = getIntent().getExtras().getString("chatRoom_name");
        arrNick = getIntent().getStringArrayListExtra("arrNick");
        str_Num = getIntent().getStringExtra("Number");
        arr_participated_uid = getIntent().getStringArrayListExtra("participated_uid");
        str_roomName =getIntent().getExtras().getString("roomName");
        isMyPost = getIntent().getExtras().getBoolean("isMyPost");
        listTagNum = getIntent().getExtras().getInt("listTagNum");
        button = (ImageButton) findViewById(R.id.messageActivity_button);
        editText = (EditText) findViewById(R.id.messageActivity_editText);
        recyclerView = (RecyclerView)findViewById(R.id.messageActivity_reclclerview);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        System.out.println("test oncreate arrNick1 "+ arrayList);
        user_arrayList = new ArrayList<String>();

        roomNum=0;
//        System.out.println("test oncreate arrNick2 "+arrayList);
        Map<String, Object> map = new HashMap<>();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            System.out.println("Fetching FCM registration token failed" + task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

//                        System.out.println("fcm map 1" + token);
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("token").setValue(token);

                        map.put("fcmToken", token);
   //                     System.out.println("fcm map " + map.keySet());
                        String msg = getString(R.string.msg_token_fmt, token);
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                mToken = snapshot.getValue().toString();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });
     //   System.out.println("myPostAdapter to chatactivity1 "+ arr_participated_uid);

        //myPostAdapter에서 클릭해서 채팅방 생성
        if(isMyPost.equals(true)){

          //  System.out.println("myPostAdapter to chatactivity2 "+ arr_participated_uid);

          //  System.out.println("myPostAdapter to chatactivity3 "+ arrayList);
            chat_list = false;
            arrayList = new ArrayList<>();
            for(String element : arr_participated_uid){
                arrayList.add(element);
            }
          //  System.out.println("myPostAdapter to chatactivity4 "+ arrayList);
            chatRoomName = str_roomName;
        }

        //peopleFragment->chatActivity
        if(chat_list == false){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid)
                    .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot item : dataSnapshot.getChildren()) {
                        user_arrayList.clear();
                        ChatModel chatModel = item.getValue(ChatModel.class);
                        roomNum++;
                       // System.out.println("arrayList real item"+ arrayList);
                        for(String key : chatModel.users.keySet()){
                            user_arrayList.add(key);
                        }
                        //System.out.println("arrayList real user_arrayList "+ user_arrayList);
                       // System.out.println("arrayList real "+ arrayList);

                    }
                    ChatModel chatModel = new ChatModel();
                    for(String element: arrayList){
                        //user uid
                    //    System.out.println("test arralist element " + element);
                        chatModel.users.put(element, true);
                    }
                    chatModel.setChatRoomUid(chatRoomUid);
                    chatModel.setRoomName(chatRoomName);
                    chatModel.setRoomNum(roomNum);
                 //   System.out.println("test chatModel chatModel users "+chatModel.users);
                 //   System.out.println("test chatModel chatRoomUid "+chatModel.getChatRoomUid());

                    if(chatRoomUid == null){

                        button.setEnabled(false);
                        first_chat =true;

                        FirebaseDatabase.getInstance().getReference().child("chatrooms")
                                .push().setValue(chatModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

//                                        checkChatRoom();
                                    }
                                });
                    }
                    checkChatRoom();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        //채팅 탭 ->chatActivity
        else{
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("roomNum").equalTo(listTagNum)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot item : dataSnapshot.getChildren()) {
                                ChatModel chatModel = item.getValue(ChatModel.class);
                                chatRoomUid = item.getKey();
                              //  System.out.println("test dataSnapshot 666  " + dataSnapshot);
                              //  System.out.println("test item 666  " + item);
                              //  System.out.println("test listTagNum 666  " + listTagNum);
                              //  System.out.println("test chatRoomUid 666  " + chatRoomUid);
                                first_chat = false;
                                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                                recyclerView.setAdapter(new RecyclerViewAdapter());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid)
                    .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChatModel chatModel = item.getValue(ChatModel.class);
                        roomNum++;

                        //chatModel의 users랑 arrayList의 값들이 모두 같으면
                        //이미 존재하는 채팅방이라고봐야
                        for (String key : chatModel.users.keySet()) {
                            user_arrayList.add(key);
                        }//이건 chatroom 안에 같이 있는 users arrayList


                       // System.out.println("test userarrayList "+ user_arrayList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        //채팅방 내에서 메세지 전송 버튼
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               // System.out.println("Send msg token "+arrayList);
               // System.out.println("Send msg token1 "+mToken);
               // System.out.println("Send msg token2 "+map.entrySet());

                if(chatRoomUid == null) {
                    checkChatRoom();
                }
                else {
                    if(editText.getText().toString().equals("")){
                    }
                    else{
                        arr_Nick = new HashMap<>();
                        token_List = new HashMap<>();
                        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    UserAccount userAccount = item.getValue(UserAccount.class);
                                    //System.out.println("userAccount " +userAccount.getNickName());
                                   // System.out.println("token token " +userAccount.getToken());
                                    arr_Nick.put(userAccount.getNickName(), userAccount.getUid());
                                    token_List.put(userAccount.getToken(), userAccount.getUid());

                                }
                                ChatModel.Comment comment = new ChatModel.Comment();
                                comment.uid = uid;
                                comment.msg = editText.getText().toString();
                                comment.timestamp = ServerValue.TIMESTAMP;
                                for (String key : arr_Nick.keySet()) {
                                    String value = arr_Nick.get(key);
                                    if (value.equals(uid)) {
                                        comment.nickname = key;
                                    } else {
                                  //      System.out.println("wrong");
                                    }

                                }

                                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot item : snapshot.getChildren()){
                                         //   System.out.println("valuevaluevalue "+ item.getKey());

                                            for(String key : token_List.keySet()) {
                                                String value = token_List.get(key);

                                           //     System.out.println("arrayList element "+item.getKey());
                                           //     System.out.println("arrayList key "+key);
                                           //     System.out.println("arrayList value "+value);
                                                if(value.equals(item.getKey())){
                                            //        System.out.println("arrayList equals "+item.getKey());
                                                    SendNotification.sendNotification(key, "메세지가 도착했습니다!", comment.nickname);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        editText.setText("");
                                    }
                                });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
        checkChatRoom();
    }

    public void  checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            chatRoomUidd = item.getKey();
                            //System.out.println("test checkChatRoom chatRoomUidd "+chatRoomUidd);
                            //System.out.println("test checkChatRoom chatRoomUid2 "+chatRoomUid);
                            //System.out.println("test checkChatRoom first_chat "+ first_chat);
                            if(first_chat){
                                chatRoomUid = chatRoomUidd;
                            }
                            button.setEnabled(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                            recyclerView.setAdapter(new RecyclerViewAdapter());

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;
        UserAccount userModel;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userModel = dataSnapshot.getValue(UserAccount.class);
                            //System.out.println("test please userModel "+ userModel.uid);

                            getMessageList();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }

        void getMessageList(){
            System.out.println("getMessageList chatRoomUid " + chatRoomUid);

            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            comments.clear();
                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                comments.add(item.getValue(ChatModel.Comment.class));
                             //   System.out.println("please comment item " + item);
                            }
                            //메세지가 갱신
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(comments.size() - 1);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);
            //내가보낸 메세지
            if(comments.get(position).uid.equals(uid)){
                messageViewHolder.textView_message.setText(comments.get(position).msg);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.sender);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);

                //상대방이 보낸 메세지
            }else {
                messageViewHolder.textview_name.setText(comments.get(position).nickname);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.receiver);
                messageViewHolder.textView_message.setText(comments.get(position).msg);
                messageViewHolder.textView_message.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_timestamp.setText(time);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textview_name;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textView_message);
                textview_name = (TextView)view.findViewById(R.id.messageItem_textview_name);
                linearLayout_destination = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_main);
                textView_timestamp = (TextView)view.findViewById(R.id.messageItem_textview_timestamp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(ChatActivity.this, MainActivity.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
        startActivity(intent);  //인텐트 이동
        overridePendingTransition(R.anim.fromleft,R.anim.toright);
    }
}