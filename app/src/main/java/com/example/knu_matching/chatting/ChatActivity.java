package com.example.knu_matching.chatting;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.SystemParameterRule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class ChatActivity extends AppCompatActivity {
    private ArrayList<String> user_arrayList;
    private Button button, chat_in;
    private EditText editText;
    private ArrayList<String> arrayList = new ArrayList<>();
    private String uid, chatRoomUid, chatRoomUidd, chatRoomName;
    private Integer chatIn;
    private Boolean boo;
    private Boolean chat_list;
    private RecyclerView recyclerView;
    private Boolean first_chat;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    // 입장 버튼 누르면 계속 새로운 채팅방 만들어짐
    // 기존의 대화내용 다 사라져버리고
    // 그냥 새로 만들어짐
    // Model이 아마 계속 만들어지는거 같은데
    // 이걸 확인해줄 방법이 필요해
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID
        arrayList = getIntent().getStringArrayListExtra("invited_List");    //나 제외
        chatIn = getIntent().getExtras().getInt("chatIn");
        chat_list = getIntent().getExtras().getBoolean("chat_list");
        chatRoomName = getIntent().getExtras().getString("chatRoom_name");
        button = (Button) findViewById(R.id.messageActivity_button);
        editText = (EditText) findViewById(R.id.messageActivity_editText);
        recyclerView = (RecyclerView)findViewById(R.id.messageActivity_reclclerview);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid)
                .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    int i = 0;
                    //chatModel의 users랑 arrayList의 값들이 모두 같으면
                    //이미 존재하는 채팅방이라고봐야
                    user_arrayList = new ArrayList<String>();
                    for(String key : chatModel.users.keySet()){
                        user_arrayList.add(key);
                    }
                    user_arrayList.containsAll(arrayList);
                    boo = user_arrayList.size() == arrayList.size();
                    System.out.println("test boolean "+ boo);
                    System.out.println("test0 " + chatModel.users + " arr " + arrayList + " 비교 " + user_arrayList.containsAll(arrayList));
                    if ((user_arrayList.containsAll(arrayList) == true) && (user_arrayList.size() == arrayList.size())){
                        first_chat = false;
                        chatRoomUid = item.getKey();
                        System.out.println("test4 chatRoomUid "+chatRoomUid);
                        button.setEnabled(true);
                        break;
                    }
                    else{
                        continue;
                    }
                }

                ChatModel chatModel = new ChatModel();
                for(String element: arrayList){
                    //user uid
                    System.out.println("test2 element " + element);
                    chatModel.users.put(element, true);
                }
                chatModel.setChatRoomUid(chatRoomUid);
                chatModel.setRoomName(chatRoomName);
                System.out.println("test3 chatModel users "+chatModel.users);
                System.out.println("test3 arrayList "+arrayList);
                System.out.println("test3 chatRoomUid "+chatRoomUid);

                if(chatRoomUid == null){

                    button.setEnabled(false);
                    first_chat =true;

                    FirebaseDatabase.getInstance().getReference().child("chatrooms")
                            .push().setValue(chatModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    System.out.println("test3 chatRoomUid "+chatModel.getChatRoomUid());
                                    checkChatRoom();
                                }
                            });
                }
                checkChatRoom();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).push().setValue(chatRoomName).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                System.out.println("test roomnameset success");
//            }
//        });

        //채팅방 내에서 메세지 전송 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatRoomUid == null) {
                    checkChatRoom();
                }
                else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.msg = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;

                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText("");
                        }
                    });

                }
            }
        });
        checkChatRoom();
    }

    public void  checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    chatRoomUidd = item.getKey();
                    System.out.println("test chatRoomUid 44  "+chatRoomUidd);
                    System.out.println("test chatRoomUid 5  "+chatRoomUid);
                    System.out.println("test first_chat "+ first_chat);
                    if(first_chat==true){
                        chatRoomUid = chatRoomUidd;
                        System.out.println("test chatRoomUid 6  "+chatRoomUid);
                    }
                    button.setEnabled(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    recyclerView.setAdapter(new RecyclerViewAdapter());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;
        UserAccount userModel;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference()
                    .child("users").child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userModel = dataSnapshot.getValue(UserAccount.class);
                            getMessageList();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms")
                    .child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
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
//                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                //상대방이 보낸 메세지
            }else {
                messageViewHolder.textview_name.setText(userModel.getNickName());
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
//                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).msg);
                messageViewHolder.textView_message.setTextSize(25);
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
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textView_message);
                textview_name = (TextView)view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView)view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_main);
                textView_timestamp = (TextView)view.findViewById(R.id.messageItem_textview_timestamp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fromleft,R.anim.toright);
    }
}