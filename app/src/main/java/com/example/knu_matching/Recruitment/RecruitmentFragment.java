package com.example.knu_matching.Recruitment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.knu_matching.GetSet.CommentItem;
import com.example.knu_matching.Post.CommentAdapter;
import com.example.knu_matching.Post.ParticipateUser;
import com.example.knu_matching.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecruitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecruitmentFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecruitmentAdapter RecruitmentAdapter = null;
    private ArrayList<ParticipateUser> ParticipateUser_list;

    public RecruitmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecruitmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecruitmentFragment newInstance(String param1, String param2) {
        RecruitmentFragment fragment = new RecruitmentFragment();
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
        View v = inflater.inflate(R.layout.fragment_recruitment, container, false);
        RecyclerView rv_recruitment = v.findViewById(R.id.rv_recruitment);

        db.collection("Post").whereEqualTo("str_email",auth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    System.out.println("현재 이용자 게시물"+task.getResult().getDocuments());
                }
            }
        });


        ParticipateUser_list = new ArrayList<>();

        // Adapter 초기화
        RecruitmentAdapter = new RecruitmentAdapter(ParticipateUser_list);
        rv_recruitment.setAdapter(RecruitmentAdapter);
        rv_recruitment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        //return inflater.inflate(R.layout.fragment_recruitment, container, false);

        return v;

    }
}