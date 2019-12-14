package com.example.planb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb.models.User;
import com.example.planb.models.guide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<guide> guides = new ArrayList<>();
    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        FirebaseDatabase.getInstance().getReference().child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = new User();
                user.setPk(dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "date_of_birth" :
                            user.setDob(snapshot.getValue().toString());
                            break;
                        case "email" :
                            user.setEmail(snapshot.getValue().toString());
                            break;
                        case "gender" :
                            user.setGender(snapshot.getValue().toString().charAt(0));
                            break;
                        case "introduce" :
                            user.setIntroduce(snapshot.getValue().toString());
                            break;
                        case "phone" :
                            user.setPhone(snapshot.getValue().toString());
                            break;
                        case "picture" :
                            user.setPicture(snapshot.getValue().toString());
                            break;
                    }
                }
                users.add(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = new User();
                user.setPk(dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "date_of_birth" :
                            user.setDob(snapshot.getValue().toString());
                            break;
                        case "email" :
                            user.setEmail(snapshot.getValue().toString());
                            break;
                        case "gender" :
                            user.setGender(snapshot.getValue().toString().charAt(0));
                            break;
                        case "introduce" :
                            user.setIntroduce(snapshot.getValue().toString());
                            break;
                        case "phone" :
                            user.setPhone(snapshot.getValue().toString());
                            break;
                        case "picture" :
                            user.setPicture(snapshot.getValue().toString());
                            break;
                    }
                }

                for(User u : users){
                    if(u.email.equals(user.email)){
                        u.setPicture(user.picture);
                        u.setPhone(user.phone);
                        u.setIntroduce(user.introduce);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        FirebaseDatabase.getInstance().getReference().child("Guide").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                guide guide = new guide();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "area" :
                            guide.setArea(snapshot.getValue().toString());
                            break;
                        case "date" :
                            guide.setDate(snapshot.getValue().toString());
                            break;
                        case "desc" :
                            guide.setDesc(snapshot.getValue().toString());
                            break;
                        case "email" :
                            guide.setEmail(snapshot.getValue().toString());
                            break;
                        case "price" :
                            guide.setPrice(snapshot.getValue().toString());
                            break;
                    }
                }
                guides.add(guide);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onSigninButtonClicked(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if (isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        } else {
            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSignUpButtonClicked(View view) {
        Intent intent = new Intent(this, create_user.class);
        startActivity(intent);
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), destination_search.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}