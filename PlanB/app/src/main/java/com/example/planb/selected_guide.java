package com.example.planb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.StringTokenizer;

public class selected_guide extends AppCompatActivity {
    TextView area;
    TextView date;
    TextView price;
    TextView email;
    TextView phone;
    TextView desc;
    Button sendemail;
    Button callguide;
    ImageView imageview;

    String gemail;
    String gdate;
    String gregion;
    String gprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_select);

        area = findViewById(R.id.region);
        date = findViewById(R.id.date);
        price = findViewById(R.id.price);
        email = findViewById(R.id.guide_email);
        phone = findViewById(R.id.phone);
        imageview = findViewById(R.id.imageView);
        desc = findViewById(R.id.desc);
        sendemail = findViewById(R.id.select);
        callguide = findViewById(R.id.select2);

        Intent intent = getIntent();
        gemail = intent.getStringExtra("SEND_EMAIL");
        gdate = intent.getStringExtra("SEND_DATE");
        gregion = intent.getStringExtra("SEND_REGION");
        gprice = intent.getStringExtra("SEND_PRICE");

        for (int j = 0; j < MainActivity.users.size(); j++) {
            if (MainActivity.users.get(j).email.equals(gemail)) {
                area.setText(gregion);
                date.setText(gdate);
                price.setText(gprice);
                email.setText(gemail);
                phone.setText(MainActivity.users.get(j).phone);
                desc.setText(MainActivity.users.get(j).introduce);

                String tmp = MainActivity.users.get(j).picture;
                StringTokenizer st = new StringTokenizer(tmp, "/");
                String filename="";
                while(st.hasMoreTokens()) {
                    filename = st.nextToken();
                }
                StorageReference ref = FirebaseStorage.getInstance().getReference("images/"+filename);
                Glide.with(imageview).load(ref).into(imageview);
            }
        }
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+gemail));
                startActivity(intent);
            }
        });

        callguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone.getText().toString()));
                startActivity(intent);
            }
        });
    }
}
