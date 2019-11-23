package com.example.planb;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
<<<<<<< HEAD
=======
import android.widget.RadioButton;
import android.widget.TextView;
>>>>>>> 4c62cad276a3cfcc4a1ea8265d1dfa0d164d9fc1
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
import java.util.Calendar;
import java.util.Date;
>>>>>>> 4c62cad276a3cfcc4a1ea8265d1dfa0d164d9fc1
import java.util.regex.Pattern;

public class create_user extends AppCompatActivity {

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
    private EditText editTextDob;   // YYYY-MM-DD
    private EditText editTextIntroduce;
    private Button editdatePicker;

    private String email = "";
    private String password = "";
    private String phone = "";
    private Character gender = null;    // M, F
    private String dob = "";            // YYYY-MM-DD
    private String introduce = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        // 데이터베이스 연결
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

        editTextEmail = findViewById(R.id.emailCreateUser);
        editTextPassword = findViewById(R.id.passwordCreateUser);
        editTextPhone = findViewById(R.id.phoneCreateUser);
        editTextDob = findViewById(R.id.passwordCreateUser);
        editTextIntroduce = findViewById(R.id.passwordCreateUser);
        editdatePicker = findViewById(R.id.selectDobButton);
    }

    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        phone = editTextPhone.getText().toString();
        dob = editTextDob.getText().toString();
        introduce = editTextIntroduce.getText().toString();
        
        if (isValidValues()) {
            createUser(email, password, phone, dob, introduce, gender);
        } else {
            Toast.makeText(create_user.this, "정보 입력이 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 회원가입 정보 유효성 검사
    private boolean isValidValues() {
        if (phone.isEmpty()) return false;
        else if (dob.isEmpty()) return false;
        else if (introduce.isEmpty()) return false;
        else if (gender.equals(null)) return false;
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
    private void createUser(String email, String password, String phone, String dob, String introduce, Character gender) {
        Map<String, Object> users = new HashMap<>();
        users.put("phone", phone);
        users.put("gender", gender);
        users.put("date_of_birth", dob);
        users.put("introduce", introduce);
        myRef.child(email).push().updateChildren(users);

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
                String dateString = String.format("%d%d%d", year, month+1, date);
                TextView dob = (TextView)findViewById(R.id.dob);
                dob.setText(dateString);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
        dialog.show();
    }
}
