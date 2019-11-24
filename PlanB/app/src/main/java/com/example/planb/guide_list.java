package com.example.planb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planb.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class guide_list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private guide_adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView date;
    private TextView region;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guidelistview);
        date = findViewById(R.id.getdate);
        region = findViewById(R.id.getregion);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.v("text", snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Intent intent = getIntent();

        if(intent!=null){
            date.setText(intent.getStringExtra("REGION"));
            region.setText(intent.getStringExtra("SET_DATE"));
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new guide_adapter(getApplicationContext());
        recyclerView.setAdapter(mAdapter);


        //디비에서 정보 받아오면 addItem 사용해서 어댑터에 가이드 추가
//        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","난 너무 예뻐요","190000"));
//        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","난 너무 매력있어","190000"));
//        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","난 너무 멋져","190000"));
//        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","암 소 소 소","190000"));
//        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","핫핫","190000"));

        //어댑터에 연결
        recyclerView.setAdapter(mAdapter);

        //어댑터클래스에 직접 이벤트처리관련 코드를 작성해줘야함 (리스트뷰처럼 구현되어있지않음 직접 정의해놔야한다.)
        //setOnItemClickListener라는 이름으로 이벤트 메소드 직접 정의한거임
        mAdapter.setOnItemClickListener(new guide_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(guide_adapter.ViewHolder holder, View view, int position) {
                guide item = mAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), "해당 가이드 지역이 선택됨==> " + item.getArea(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void createUser(String email, String password, String phone, String dob, String introduce, Character gender) {
//        myRef = database.getReference("User");//"User"에서 시작하겠다는 뜻.
//
//        // Map<String, Object> childUpdates = new HashMap<>();
//        Map<String, Object> userValue = null;
//
//        User user = new User(email, phone, gender, dob, introduce);
//        userValue = user.toMap();
//
//        //childUpdates.put("", userValue);
//        //myRef.child("guide").addChildEventListener-->그 데이터 베이스에서 작용
//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Log.v("testx2", snapshot.getKey());
////                    Log.v("testx3", snapshot.getValue().toString());
////                    Log.v("testx4", snapshot.getChildrenCount()+"");
////                }--> 모델안에있는것이 차례대로 돌음. getKEy검사해서 키가 지역, 날짜인거 비교해서 가져요기
//                //가져올떄 getvalue tostring 해서 가져와도 되고, 아니면은
//                Log.v("testx2", dataSnapshot.getKey());
//                Log.v("testx3", dataSnapshot.getValue().toString());
//                Log.v("testx4", dataSnapshot.getChildrenCount()+"");
//                if (dataSnapshot.exists());
////                Object user = dataSnapshot.getValue(Object.class);
////                Log.v("testx2", user.toString());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//
//        myRef.push().updateChildren(userValue);
//
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // 회원가입 성공
//                            //Toast.makeText(create_user.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            // 회원가입 실패
//                            //Toast.makeText(create_user.this, "회원가입 실패..", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
