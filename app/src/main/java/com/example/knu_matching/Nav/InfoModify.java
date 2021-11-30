package com.example.knu_matching.Nav;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.MainActivity;
import com.example.knu_matching.membermanage.Student_Certificate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

// TODO: 2021-09-13 정보 불러오기까지 완료/ FireStore Cloud에서 업데이트 작업하기
public class InfoModify extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference dbRef = db.collection("Account").document((((MainActivity)MainActivity.context).strEmail.replace(".",">")));
    private String strEmail, strPassword, strNick, strMaojr, strStudentId, strPhoneNumber, strStudentName;
    private EditText edt_StudentName, edt_StudentID, edt_Major, edt_PhoneNumber, edt_nickname;
    private Button btn_update, btn_check_nick, btn_finish_modify;
    private TextView tv_nickstate;
    private boolean nickname_state;
    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_modify);
        init();
     //   System.out.println("디버깅");
     //   System.out.println("infomodify디버깅");
        nickname_state = true;

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoveToStudentCertificate();
            }
        });

        btn_check_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_NickName_Duplicate();
            }
        });

        btn_finish_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish_modify();
                Intent intent = new Intent();
                intent.putExtra("nickname",strNick);
                intent.putExtra("studentId",strStudentId);
                intent.putExtra("major",strMaojr);
                intent.putExtra("phoneNumber",strPhoneNumber);
                intent.putExtra("studentName",strStudentName);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public void init() {
        edt_StudentName = findViewById(R.id.edt_StudentName);
        edt_StudentID = findViewById(R.id.edt_StudentID);
        edt_Major = findViewById(R.id.edt_Major);
        edt_PhoneNumber = findViewById(R.id.edt_PhoneNumber);
        edt_nickname = findViewById(R.id.edt_nickname);
        tv_nickstate = findViewById(R.id.tv_nickstate);
        btn_update = findViewById(R.id.btn_update);
        btn_check_nick = findViewById(R.id.btn_check_nick);
        btn_finish_modify = findViewById(R.id.btn_finish_modify);
        edt_StudentName.setText(((MainActivity)MainActivity.context).strStudentName);
        edt_StudentID.setText(((MainActivity)MainActivity.context).strStudentId);
        edt_Major.setText(((MainActivity)MainActivity.context).strMaojr);
        edt_PhoneNumber.setText(((MainActivity)MainActivity.context).strPhoneNumber);
        edt_nickname.setText(((MainActivity)MainActivity.context).strNick);
    }

    private void MoveToStudentCertificate() {
        Intent intent = new Intent(InfoModify.this, Student_Certificate.class);
        startActivityResult.launch(intent);
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "InfoModify 돌아왔다. ");
                        strStudentName = result.getData().getStringExtra("StudentName");
                        strStudentId = result.getData().getStringExtra("StudentId");
                        strMaojr = result.getData().getStringExtra("Major");
                        strPhoneNumber = result.getData().getStringExtra("PhoneNumber");
                        edt_StudentName.setText(strStudentName);
                        edt_StudentID.setText(strStudentId);
                        edt_Major.setText(strMaojr);
                        edt_PhoneNumber.setText(strPhoneNumber);
                    }
                }
            });

    private void Check_NickName_Duplicate() {
        strNick = edt_nickname.getText().toString();
       // System.out.println("test2222   " + strNick + " " + strEmail + " " + strStudentId);

        db.collection("Account")
                .whereEqualTo("nickName", strNick)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                System.out.println("회원가입 디버깅"+QuerySnapshot document:task.getResult());
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserAccount userAccount = document.toObject(UserAccount.class);
                    //            Log.d("db디버깅", userAccount.getNickName());
                                if (strNick.equals(userAccount.getNickName())) {
                                    tv_nickstate.setText("중복된 닉네임 입니다.");
                                    nickname_state = false;
                                    return;
                                }
                            }
                            nickname_state = true;
                            tv_nickstate.setText("사용 가능한 닉네임 입니다.");
                        } else {

                        }
                    }
                });
    }

// FireStore Transaction
    private void finish_modify(){
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(dbRef,"studentName",edt_StudentName.getText().toString());
                transaction.update(dbRef,"studentId",edt_StudentID.getText().toString());
                transaction.update(dbRef,"major",edt_Major.getText().toString());
                transaction.update(dbRef,"phoneNumber",edt_PhoneNumber.getText().toString());
                transaction.update(dbRef,"nickName",edt_nickname.getText().toString());
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Transaction success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });

    }
}
