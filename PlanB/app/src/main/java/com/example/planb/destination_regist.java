package com.example.planb;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planb.models.guide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class destination_regist extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database;
    private DatabaseReference mPostReference;
    private DatabaseReference myRef;
    private EditText area;
    private TextView sdate;
    private TextView edate;
    private EditText ts;
    private EditText price;
    private Button button;
    private int date;
    private String dateadd;
    private String uemail;
    private String uphone;
    private String uimage;
    private Date FirstDate;
    long calDateDays;
    long calDate;
    private FirebaseAuth firebaseAuth;
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener sDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatesLabel();
        }
    };

    private void updatesLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        sdate.setText(sdf.format(myCalendar.getTime()));
    }

    DatePickerDialog.OnDateSetListener eDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateeLabel();
        }
    };

    private void updateeLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        edate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.regist);

        area = findViewById(R.id.area);
        sdate = findViewById(R.id.sdate);
        edate = findViewById(R.id.edate);
        ts = findViewById(R.id.travel_story);
        price = findViewById(R.id.price);
        button = findViewById(R.id.rbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog d = new DatePickerDialog(destination_regist.this, sDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(new Date().getTime());
                d.show();
            }
        });
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog d = new DatePickerDialog(destination_regist.this, eDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(new Date().getTime());
                d.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calDateBetweense();
                if(area.getText().toString().length() != 0 && sdate.getText().toString().length() != 0 && edate.getText().toString().length() != 0 &&
                        ts.getText().toString().length() != 0 && price.getText().toString().length() != 0) {
                    if(calDateDays >= 0) {
                        for (int i = 0; i <= (int) calDateDays; i++) {
                            Date date1 = new Date(FirstDate.getTime() + (i * 24 * 60 * 60 * 1000));
                            SimpleDateFormat datef = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            dateadd = datef.format(date1);
                            postFirebaseDatabase(true);
                        }
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "종료일은 시작일보다 작을 수 없습니다.", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void postFirebaseDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference("Guide");
        uemail = user.getEmail();//유저 이메일 받는 변수
        Map<String, Object> postValues = null;
        if (add) {
            guide post = new guide(uemail, area.getText().toString(), dateadd, ts.getText().toString(), price.getText().toString());
            postValues = post.toMap();
        }
        mPostReference.push().updateChildren(postValues);
    }

    public void calDateBetweense() {
        try { // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            FirstDate = format.parse(sdate.getText().toString());
            Date SecondDate = format.parse(edate.getText().toString());

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            calDate = SecondDate.getTime() - FirstDate.getTime();
            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            calDateDays = calDate / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            // 예외 처리
        }
    }
}