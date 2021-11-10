package com.example.knu_matching.main;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.knu_matching.AdapterActivity;
import com.example.knu_matching.R;
import com.example.knu_matching.postActivity;
import com.example.knu_matching.postInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_Recent;
    private Button btn_next;
    private Button btn_back;
    private FirebaseUser user;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment4.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_post_fragment, container, false);
        Button btn_Recent = v.findViewById(R.id.btn_Recent);
        Button btn_next = v.findViewById(R.id.btn_next);
        Button btn_back = v.findViewById(R.id.btn_back);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Post")
                .orderBy("str_time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<postInfo> postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "==>" + document.getData());
                                postList.add(new postInfo(
                                        document.getData().get("str_Title").toString(),
                                        document.getData().get("str_date").toString(),
                                        document.getData().get("str_Number").toString(),
                                        document.getData().get("str_post").toString(),
                                        document.getData().get("str_time").toString(),
                                        document.getData().get("str_Nickname").toString(),
                                        document.getData().get("str_email").toString(),
                                        document.getId()
                                ));
                                System.out.println("이메일 " +document.getData().get("str_email").toString());
                            }
                            RecyclerView recyclerView = v.findViewById(R.id.recycleView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            RecyclerView.Adapter mAdapter = new AdapterActivity(getActivity(), postList);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "error", task.getException());
                        }
                    }
                });

        btn_Recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveSubActivity();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void myStartActivity(Class c){
        Intent intent =new Intent(getActivity(), c);
        startActivity(intent);
    }

    private void moveSubActivity() {
        Intent intent = new Intent(getContext(), postActivity.class);
        startActivityResult.launch(intent);
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "RegisterActivity로 돌아왔다. ");
                    }
                }
            });
}