package com.example.planb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.planb.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.StringTokenizer;

public class UpdateUser extends AppCompatActivity {
    FirebaseUser CurrentUser;
    User user;

    EditText emailEditText;
    EditText phoneEditText;
    RadioButton mailRadio, femailRadio;
    EditText dobEditText;
    EditText introduceEditText;
    ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.update_user);

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        emailEditText = findViewById(R.id.emailUpdateUser);
        phoneEditText = findViewById(R.id.phoneUpdateUser);
        dobEditText = findViewById(R.id.dobUpdateUser);
        introduceEditText = findViewById(R.id.introductionUpdateUser);
        userImageView = findViewById(R.id.imageUpdateUser);
        mailRadio = findViewById(R.id.maleUpdateUser);
        femailRadio = findViewById(R.id.femaleUpdateUser);

        for (User i : MainActivity.users) {
            if (i.email.equals(CurrentUser.getEmail())) {
                user = i;
                break;
            }
        }

        emailEditText.setText(user.email);
        phoneEditText.setText(user.phone);
        dobEditText.setText(user.dob);
        introduceEditText.setText(user.introduce);
        if (user.gender.equals('M')) mailRadio.setChecked(true);
        else femailRadio.setChecked(true);

        String tmp = user.picture;
        StringTokenizer st = new StringTokenizer(tmp, "/");
        String filename="";
        while(st.hasMoreTokens()) {
            filename = st.nextToken();
        }
        StorageReference ref = FirebaseStorage.getInstance().getReference("images/"+filename);

        Glide.with(userImageView).load(ref).into(userImageView);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateUserButton:
            case R.id.passwordUpdateUser:
                startActivity(new Intent(this, UpdatePassword.class));
            case R.id.cancelUpdateButton:
                finish();
        }
    }
}
