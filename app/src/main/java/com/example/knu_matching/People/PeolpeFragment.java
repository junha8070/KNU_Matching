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
    private ArrayList<String> user_arrayList;
    private String uid, strNick, str_chatroom_name, strUid;
    private String chatRoomUid;
    boolean chatin, ischeckfriend, iswritename;
    private ArrayList<String> arr_participated_uid = new ArrayList<>();


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


        return view;

    }

    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<UserAccount> userModels;
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
            //내 UID
            FirebaseDatabase.getInstance().getReference().child("users")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userModels.clear();
                            arrayList.add(myUid);
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserAccount userAccount = snapshot.getValue(UserAccount.class);

                                System.out.println("test getuid " + userAccount.getUid());
                                System.out.println("test myUid " + myUid);
                                if (userAccount.uid.equals(myUid)) {
                                    arrNick.add(userAccount.getNickName());
                                    System.out.println("arrNick1 "+arrNick);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ((CustomViewHolder) holder).textView.setText(userModels.get(position).getNickName());

            if (userModels != null && userModels.size() > 0) {
                System.out.println("test 11111111111111 ");
                System.out.println("test arraylist " + arrayList);
                ((CustomViewHolder) holder).cbox_invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CustomViewHolder) holder).cbox_invite.isChecked()) {

                            strUid = userModels.get(position).getUid();
                            arrNick.add(userModels.get(position).getNickName());
                            arrayList.add(strUid);
                        } else {
                            strUid = userModels.get(position).getUid();
                            arrNick.remove(userModels.get(position).getNickName());
                            arrayList.remove(strUid);
                        }

                        if (arrayList.size() == 1) {
                            ischeckfriend = false;
                        } else {
                            ischeckfriend = true;
                        }

                        System.out.println("test8 arraylist " + arrayList);
                        System.out.println("test8 arrNick " + arrNick);
                        System.out.println("test8 arrayList.size() " + arrayList.size());
                        System.out.println("test8 ischeckfriend " + ischeckfriend);
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
                        System.out.println("test arraylist_btn_click " + arrayList);
                        System.out.println("test iswritename " + iswritename);
                        System.out.println("test ischeckfriend " + ischeckfriend);
                        System.out.println("test ChatRoomName " + str_chatroom_name);
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