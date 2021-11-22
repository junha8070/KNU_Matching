package com.example.knu_matching.chatting;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private String uid;
    private TextView chatroom_name;
    private String chatRoomUid;
    boolean chat_list= false;
    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new ChatFragmentRecyclerViewAdapter());
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID

        return view;
    }

    class ChatFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<ChatModel> chatModels;

        public ChatFragmentRecyclerViewAdapter() {
            chatModels = new ArrayList<>();
//
//            FirebaseDatabase.getInstance().getReference().child("chatrooms")
//                    .addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    chatModels.clear();
//                    showChatModels.clear();
//                    //snapshot = 전체 chatrooms
//                    //chatroom의 users 봤을 때 현재 로그인된 내 uid있을 경우에만 chatModel에 추가하기
//                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
//                        ChatModel chatModel = snapshot.getValue(ChatModel.class);
//                        chatModels.add(chatModel);
//                        System.out.println("test chatModel 22 " + chatModel);
//
//                        //chatModels는 ArrayList
//                        //chatModel는 chatModel형snapshot? ChatModel.class
//                    }
//                    notifyDataSetChanged();
//                    System.out.println("test chatModel 1 " + chatModels);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatModels.clear();
                    for(DataSnapshot item: snapshot.getChildren()){
                        ChatModel chatModel = item.getValue(ChatModel.class);

                        System.out.println("test chat item " +chatModel.users.keySet());
                        if(chatModel.users.keySet().contains(uid)){
                            System.out.println("testtest if equals 안");
                            chatModels.add(chatModel);
                        }
                        System.out.println("test chatModel 22 " + chatModels);

                        //chatModels는 ArrayList
                        //chatModel는 chatModel형snapshot? ChatModel.class
                    }
                    System.out.println("test chatModel 1 " + chatModels);
                    notifyDataSetChanged();
                    System.out.println("test chatModel 1 " + chatModels);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatlist,parent,false);
            return new ChatFragment.ChatFragmentRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ((CustomViewHolder)holder).chatroom_name.setText(chatModels.get(position).getRoomName());
            System.out.println("test chatModel 2 " + chatModels.get(position).getRoomName());
            ((CustomViewHolder)holder).itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView chatroom_name;

            public CustomViewHolder(View itemView) {
                super(itemView);
                chatroom_name = (TextView) itemView.findViewById(R.id.chatroom_tv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int listTagNum = (int) v.getTag();
                        chat_list = true;
                        System.out.println("test position tag "+listTagNum);

                        //db의 순서대로 0, 1, 2 ...
                        //true면 리스트에 보인다는 뜻
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);
                        intent.putExtra("chat_list", chat_list);
                        intent.putExtra("listTagNum", listTagNum);
                        ActivityOptions activityOptions = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright,R.anim.toleft);
                            startActivity(intent,activityOptions.toBundle());
                        }
                    }
                });
            }

        }
    }
}