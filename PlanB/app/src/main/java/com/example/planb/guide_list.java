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

import com.example.planb.models.Mixedguideframe;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class guide_list extends AppCompatActivity {
    private RecyclerView recyclerView;
    private guide_adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView date;
    TextView region;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    public static String sendate;
    public static String sendregion;

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
        sendate = intent.getStringExtra("SET_DATE");
        sendregion = intent.getStringExtra("REGION");
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


        for(int i = 0; i < MainActivity.guides.size(); i++) {
            if(guide_list.sendate.equals(MainActivity.guides.get(i).getDate()) && guide_list.sendregion.equals(MainActivity.guides.get(i).getArea())) {
                for(int j = 0; j < MainActivity.users.size(); j++) {
                    if(MainActivity.users.get(j).email.equals(MainActivity.guides.get(i).getEmail())) {
                        mAdapter.addItem(new Mixedguideframe(MainActivity.guides.get(i).getEmail(), MainActivity.users.get(j).picture , MainActivity.users.get(j).phone, MainActivity.guides.get(i).getDesc(), MainActivity.guides.get(i).getPrice()));
                        break;
                    }
                }
            }
        }
        //어댑터에 연결
        recyclerView.setAdapter(mAdapter);

        //어댑터클래스에 직접 이벤트처리관련 코드를 작성해줘야함 (리스트뷰처럼 구현되어있지않음 직접 정의해놔야한다.)
        //setOnItemClickListener라는 이름으로 이벤트 메소드 직접 정의한거임
        mAdapter.setOnItemClickListener(new guide_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(guide_adapter.ViewHolder holder, View view, int position) {
                Mixedguideframe item = mAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), selected_guide.class);
                intent.putExtra("SEND_EMAIL", holder.guidemail.getText());
                intent.putExtra("SEND_DATE", guide_list.sendate);
                intent.putExtra("SEND_REGION", guide_list.sendregion);
                intent.putExtra("SEND_PRICE", holder.guidecost.getText());
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "해당 가이드 지역이 선택됨==> " + item.getArea(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
