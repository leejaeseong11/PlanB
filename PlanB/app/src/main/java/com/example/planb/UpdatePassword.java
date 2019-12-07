package com.example.planb;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.planb.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class UpdatePassword extends AppCompatActivity {
    FirebaseUser currentUser;

    EditText editNew1, editNew2;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.update_password);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        editNew1 = findViewById(R.id.newPassword1);
        editNew2 = findViewById(R.id.newPassword2);
    }

    public void onClick(View view) {
        String password1 = editNew1.getText().toString();
        String password2 = editNew2.getText().toString();
        switch (view.getId()) {
            case R.id.updateUserButton:
                if (!PASSWORD_PATTERN.matcher(password1).matches()) {
                    Toast.makeText(this, "4자 이상 16자 이하 숫자, 영어, 특수문자로 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!password1.equals(password2)) {
                    Toast.makeText(this, "비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    break;
                } else currentUser.updatePassword(password1);
            case R.id.cancelUpdateButton:
                finish();
        }
    }
}
