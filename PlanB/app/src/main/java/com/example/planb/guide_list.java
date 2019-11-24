package com.example.planb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class guide_list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private guide_adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView date;
    private TextView region;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guidelistview);
        date = findViewById(R.id.getdate);
        region = findViewById(R.id.getregion);
        database = FirebaseDatabase.getInstance();
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
        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","난 너무 예뻐요","190000"));
        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","난 너무 매력있어","190000"));
        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","난 너무 멋져","190000"));
        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","암 소 소 소","190000"));
        mAdapter.addItem(new guide("n@gmail.com","대전","2019/11/23","핫핫","190000"));

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
//    public void getFirebaseDatabase() {
//        myRef = database.getReference();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                arrayData.clear();
//                arrayIndex.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String key = snapshot.getKey();
//
//                    guide get = dataSnapshot.getValue(guide.class);
//                    String[] info = {get.area, get.email, get.price, get.desc};
//                    String Result = setTextLength(info[0], 10) + setTextLength(info[1], 10) + setTextLength(info[2], 10) + setTextLength(info[3], 10);
//
//                    if (key == "20191123") {
//                        arrayData.add(Result);
//                        arrayIndex.add(key);
//                    }
//                }
//                arrayAdapter.clear();
//                arrayAdapter.addAll(arrayData);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//    public String setTextLength(String text, int length) {
//         if (text.length() < length) {
//            int gap = length - text.length();
//            for (int i = 0; i < gap; i++) {
//                text = text + " ";
//            }
//         }
//    return text;
//    }
}
