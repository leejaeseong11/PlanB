package com.example.planb;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.planb.models.User;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import static android.os.Build.ID;

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
                new DatePickerDialog(destination_regist.this, sDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(destination_regist.this, eDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calDateBetweense();
                for (int i = 0; i <= (int) calDateDays; i++) {
                    Date date1 = new Date(FirstDate.getTime() + (i * 24 * 60 * 60 * 1000));
                    SimpleDateFormat datef = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    dateadd = datef.format(date1);
                    postFirebaseDatabase(true);
                }
                Toast.makeText(getApplicationContext(), uemail + uphone + uimage, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void postFirebaseDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference();
        uemail = user.getEmail();//유저 이메일 받는 변수
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        String keyvalue = "20191230";
        createUser();
        if (add) {
            guide post = new guide(uemail, uimage, uphone, area.getText().toString(), dateadd, ts.getText().toString(), price.getText().toString());
            postValues = post.toMap();
        }

        String datekey = "kk";
        StringTokenizer st = new StringTokenizer(dateadd, "/");
        while (st.hasMoreTokens()) {
            datekey += st.nextToken();
        }
        childUpdates.put("/key_list/" + datekey, postValues);
        mPostReference.updateChildren(childUpdates);
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
            calDateDays = Math.abs(calDateDays);

        } catch (ParseException e) {
            // 예외 처리
        }
    }

    private void createUser() {
        FirebaseDatabase.getInstance().getReference().child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.v("testx456", dataSnapshot.getValue().toString());
                for (DataSnapshot p : dataSnapshot.getChildren()) {
                    Log.v("testx3", dataSnapshot.getValue().toString());
                    String key = p.getKey();
                    User get = p.getValue(User.class);
                    Log.v("testx4", dataSnapshot.getValue().toString());
                    String[] info = {get.email, get.phone, get.picture};
                   // String Result = setTextLength(info[0], 10) + setTextLength(info[1], 10) + setTextLength(info[2], 10) + setTextLength(info[3], 10);
                    //Log.d("getFirebaseDatabase", "key: " + key);
                    Log.v("testx5", dataSnapshot.getValue().toString());
                    if (uemail.equals(info[0])) {
                        uphone = info[1];
                        uimage = info[2];
                    }
                    Log.v("testx6", info[0] + ", " + info[1] + ", " + info[2]);
                }
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
//        myRef = database.getReference();
//        Query myTopPostsQuery = myRef.child("User").orderByChild("email");
//        myTopPostsQuery.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                            User get = postSnapshot.getValue(User.class);
//                            String[] info = {get.email, get.phone, get.picture};
//                            String Result = setTextLength(info[0], 10) + setTextLength(info[1], 10) + setTextLength(info[2], 10) + setTextLength(info[3], 10);
//                            //Log.d("getFirebaseDatabase", "key: " + key);
//                            if (uemail.equals(info[0])) {
//                                uphone = info[1];
//                                uimage = info[2];
//                            }
//                            Log.v("testx", info[0] + ", " + info[1] + ", " + info[2]);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w("testx", "getUser:onCancelled", databaseError.toException());
//                    }
//                });
//        Log.v("testx", myTopPostsQuery.toString());

//        myRef = database.getReference("User");//"User"에서 시작하겠다는 뜻.
//
//        //childUpdates.put("", userValue);Query@2fb7130
//        //myRef.child("guide").addChildEventListener-->그 데이터 베이스에서 작용
//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    String key = postSnapshot.getKey();
//                    User get = postSnapshot.getValue(User.class);
//                    String[] info = {get.email, get.phone, get.picture};
//                    String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10);
//                    Log.d("getFirebaseDatabase", "key: " + key);
//                    if(uemail == info[0]){
//                        uphone = info[1];
//                        uimage = info[2];
//                    }
//                }//--> 모델안에있는것이 차례대로 돌음. getKEy검사해서 키가 지역, 날짜인거 비교해서 가져요기
//                //가져올떄 getvalue tostring 해서 가져와도 되고, 아니면은
//                Log.v("testx2", dataSnapshot.getKey());
//                Log.v("testx3", dataSnapshot.getValue().toString());
//                Log.v("testx4", dataSnapshot.getChildrenCount()+"");
//                if (dataSnapshot.exists());
////                Object user = dataSnapshot.getValue(Object.class);
////                Log.v("testx2", user.toString());
//            }
//            public String setTextLength(String text, int length){
//                if(text.length()<length){
//                    int gap = length - text.length();
//                    for (int i=0; i<gap; i++){
//                        text = text + " ";
//                    }
//                }
//                return text;
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });

    }

    public String setTextLength(String text, int length) {
        if (text.length() < length) {
            int gap = length - text.length();
            for (int i = 0; i < gap; i++) {
                text = text + " ";
            }
        }
        return text;
    }

}
