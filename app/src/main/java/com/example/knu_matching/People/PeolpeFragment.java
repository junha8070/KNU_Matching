package com.example.knu_matching.People;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.chatting.ChatActivity;
import com.example.knu_matching.main.PostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeolpeFragment extends Fragment {
    private String uid, strNick, str_chatroom_name, strUid;
    private String chatRoomUid;
    boolean chatin;
    Button btn_invite;
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
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID
        btn_invite = (Button) view.findViewById(R.id.btn_invite);
        chatroom_name = (EditText) view.findViewById(R.id.chatroom_name);
        return view;

    }

    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<UserAccount> userModels;
        ArrayList<String> arrayList = new ArrayList<String>();
        Context context;
        public PeopleFragmentRecyclerViewAdapter(ArrayList<String> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //내 UID
            FirebaseDatabase.getInstance().getReference().child("users")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();
                    arrayList.add(myUid);
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);

                        System.out.println("test getuid "+ userAccount.getUid());

                        System.out.println("test myUid "+ myUid);

                        if(userAccount.uid.equals(myUid)){
                            continue;
                        }
                        userModels.add(userAccount);
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ((CustomViewHolder)holder).textView.setText(userModels.get(position).getNickName());
            if(userModels != null && userModels.size() > 0){
                System.out.println("test 11111111111111 ");
                System.out.println("test arraylist "+ arrayList);
                ((CustomViewHolder) holder).cbox_invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CustomViewHolder) holder).cbox_invite.isChecked()) {
                            strUid = userModels.get(position).getUid();
                            arrayList.add(strUid);
                        } else {
                            strUid = userModels.get(position).getUid();
                            arrayList.remove(strUid);
                        }

                        System.out.println("test arraylist " + arrayList);
                    }
                });
            }

            btn_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("test arraylist_btn_click " + arrayList);
                    chatin = true;
                    str_chatroom_name = chatroom_name.getText().toString();
                    System.out.println("test ChatRoomName " + str_chatroom_name);
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    intent.putExtra("invited_List", arrayList);
                    intent.putExtra("chatIn", true);
                    intent.putExtra("chatRoom_name", str_chatroom_name);
                    ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright,R.anim.toleft);
                        startActivity(intent,activityOptions.toBundle());
                    }
                }
            });
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
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                textView = (TextView) view.findViewById(R.id.frienditem_textview);
                cbox_invite = (CheckBox) view.findViewById(R.id.cbox_invite);
            }
        }
    }

}