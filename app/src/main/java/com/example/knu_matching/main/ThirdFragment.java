package com.example.knu_matching.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.membermanage.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    Button btn_modify;
    TextView tv_name, tv_rate, tv_nickname, tv_email, tv_major, tv_studentId, tv_number, tv_leave, tv_signout;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    UserAccount account = new UserAccount();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public ThirdFragment() {
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
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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
        View v = inflater.inflate(R.layout.activity_third_fragment, container, false);
        btn_modify = (Button) v.findViewById(R.id.btn_modify);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_rate = (TextView) v.findViewById(R.id.tv_rate);
        tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        tv_major = (TextView) v.findViewById(R.id.tv_major);
        tv_studentId = (TextView) v.findViewById(R.id.tv_studentId);
        tv_number = (TextView) v.findViewById(R.id.tv_number);
        tv_leave = (TextView) v.findViewById(R.id.tv_leave);
        tv_signout = (TextView) v.findViewById(R.id.tv_signout);

//        tv_name.setText(account.getEmailId(FirebaseUser));

        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(v.getContext(),"테스트",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}