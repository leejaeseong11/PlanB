package com.example.planb;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class create_user extends AppCompatActivity {
    private Button editdatePicker;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    // 이메일, 비밀번호, 전화번호, 성별, 생년월일, 소개글
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextIntroduce;
    private ImageView editImage;

    private String email = "";
    private String password = "";
    private String phone = "";
    private Character gender = null;    // M, F
    private String dobString = "";            // YYYY-MM-DD
    private String introduce = "";
    private static int key;
    private Uri filePath;//uri로 이미지 받아오는 경로(암시적 intent)
    String urlString = ""; //이미지 파일 경로
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        // 데이터베이스 연결
        database = FirebaseDatabase.getInstance();

        editTextEmail = findViewById(R.id.emailCreateUser);
        editTextPassword = findViewById(R.id.passwordCreateUser);
        editTextPhone = findViewById(R.id.phoneCreateUser);
        editTextIntroduce = findViewById(R.id.introductionCreateUser);
        editdatePicker = findViewById(R.id.selectDobButton);
        editImage = findViewById(R.id.imageCreatUser);
    }

    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        phone = editTextPhone.getText().toString();
        introduce = editTextIntroduce.getText().toString();

        email = "test" + new Random().nextInt() + "@d.com";
        password = "password12!";
        phone = "01088888888";
        dobString = "19991228";
        gender = 'F';
        introduce = "안녕하세요~~ 미라지예요~~";
        boolean flag = uploadFile();
        if (isValidValues() && flag) {
            createUser(email, password, phone, dobString, introduce, gender, urlString);
        } else {
            Toast.makeText(create_user.this, "정보 입력이 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 회원가입 정보 유효성 검사
    private boolean isValidValues() {
        if (phone.isEmpty()) return false;
        else if (dobString.isEmpty()) return false;
        else if (introduce.isEmpty()) return false;
        else if (gender.toString().length() == 0) return false;
        else if (!isValidEmail()) return false;
        else if (!isValidPasswd()) return false;
        else return true;
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

    // 회원가입
    private void createUser(String email, String password, String phone, String dob, String introduce, Character gender, String picture) {
        myRef = database.getReference("User");
       // Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValue = null;

        User user = new User(email, phone, gender, dob, introduce, picture);
        userValue = user.toMap();
        myRef.push().updateChildren(userValue);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(create_user.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(create_user.this, "회원가입 실패..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void onGenderRadioClicked(View view) {
        switch(view.getId()) {
            case R.id.male:
                gender = 'M';
                break;
            case R.id.female:
                gender = 'F';
                break;
        }
    }

    public void onDatePickerClicked(View view){
        //Calendar를 이용하여 년, 월, 일, 시간, 분을 PICKER에 넣어준다.
        final Calendar cal = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(create_user.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int date) {
                String dateString = String.format("%d%02d%02d", year, month+1, date);
                TextView dob = (TextView)findViewById(R.id.dob);
                dob.setText(dateString);
                dobString = dateString;
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
        dialog.show();
    }

    public void onClickImage(View view){
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
                editImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private boolean uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

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
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
