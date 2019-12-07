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

import com.bumptech.glide.Glide;
import com.example.planb.models.Mixedguideframe;
import com.example.planb.models.User;
import com.example.planb.models.guide_delete_frame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class destination_delete extends AppCompatActivity {
    private RecyclerView recyclerView;
    private guide_delete_adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView date;
    TextView region;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_delete);
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

        recyclerView = (RecyclerView) findViewById(R.id.deleterecycle);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new guide_delete_adapter(getApplicationContext());
        recyclerView.setAdapter(mAdapter);


//        내가 등록한 여행지 알고리즘 생각해야할듯
        for(int i = 0; i < MainActivity.guides.size(); i++) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(MainActivity.guides.get(i).getEmail())){
                mAdapter.addItem(new guide_delete_frame(MainActivity.guides.get(i).getArea(), MainActivity.guides.get(i).getDate(),MainActivity.guides.get(i).getPrice(),MainActivity.guides.get(i).getDesc()));
            }
        }

            //어댑터에 연결
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new guide_delete_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(guide_delete_adapter.ViewHolder holder, View view, int position) {
                    guide_delete_frame item = mAdapter.getItem(position);
                    Toast.makeText(getApplicationContext(),position,Toast.LENGTH_SHORT).show();
                }
        });
    }
}
