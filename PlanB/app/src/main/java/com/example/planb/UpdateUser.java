package com.example.planb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.planb.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private Uri filePath;
    String filename="";
    String urlString="";

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

        while(st.hasMoreTokens()) {
            filename = st.nextToken();
        }
        StorageReference ref = FirebaseStorage.getInstance().getReference("images/"+filename);
        

        Glide.with(userImageView).load(ref).into(userImageView);
        urlString = "gs://planb-32e2f.appspot.com/images/" + filename;
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateUserButton:
                uploadFile();
                Map<String, Object> changeData = null;

                User newUser = new User(user.email, phoneEditText.getText().toString(), user.gender, user.dob, introduceEditText.getText().toString(), urlString);

                changeData = newUser.toMap();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");

                reference.child(user.getPk()).updateChildren(changeData);

                Toast.makeText(getApplicationContext(), "정보 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                finish();
                //수정 버튼 구현하기
                break;
            case R.id.cancelUpdateButton:
                finish();
        }
    }


    public void onClickImageChanged(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storage.getReference().child("images/" + filename).delete();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now) + ".png";
            urlString = "gs://planb-32e2f.appspot.com/images/" + filename;

            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://planb-32e2f.appspot.com").child("images/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });

        }
    }

}
