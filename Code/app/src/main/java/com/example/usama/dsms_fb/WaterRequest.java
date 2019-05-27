package com.example.usama.dsms_fb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nullable;

public class WaterRequest extends AppCompatActivity {
    Intent intent;

    private EditText startDate;
    private EditText endDate;
    private EditText startTime;
    private EditText endTime;
    private EditText cropName;
    private Button submitButton;

    private String EmailHolder;
    private String startDateHolder;
    private String endDateHolder;
    private String cropNameHolder;
    private String startTimeHolder;
    private String endTimeHolder;
    private String horsepower;
    private String boringInFeet;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    WaterRequestData waterRequestData;
    Calendar startCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startdateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, monthOfYear);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStartLabel();
        }

    };
    Calendar endCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener enddateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, monthOfYear);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndLabel();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_request);

        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(DashboardActivity.email);

        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        cropName = (EditText) findViewById(R.id.cropName);
        startTime = (EditText) findViewById(R.id.startTime);
        endTime = (EditText) findViewById(R.id.endTime);
        submitButton =(Button) findViewById(R.id.buttonRegisterWater);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseUser = mAuth.getCurrentUser();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Signup_Credentials");

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(WaterRequest.this, startdateSet, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(WaterRequest.this, enddateSet, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener(){
            int currentHour;
            int currentMinute;
            TimePickerDialog timePickerDialog;
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(WaterRequest.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        startTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener(){
            int currentHour;
            int currentMinute;
            TimePickerDialog timePickerDialog;
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(WaterRequest.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        endTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addRequest();
            }
        });
    }

    public void endActivity(){
        this.finish();
    }
    public Boolean dateVerify(String start,String end){
        Boolean res = false;
        String message = "";
        String [] startDate = start.split("/");
        Boolean lengthVerify = startDate.length ==3;
        Boolean numVerify =false;
        if(lengthVerify){
            numVerify = isNum(startDate[0]) && isNum(startDate[1]) &&  isNum(startDate[2]);
        }
        Boolean startVerify = numVerify && lengthVerify;
        if(!startVerify){
            message +="1- Start Date format is not Valid \n";
        }

        String [] endDate = end.split("/");
        lengthVerify = endDate.length ==3;
        numVerify =false;
        if(lengthVerify){
            numVerify = isNum(endDate[0]) && isNum(endDate[1]) &&  isNum(endDate[2]);
        }
        Boolean endVerify = numVerify && lengthVerify;
        if(!endVerify){
            message +="2- End Date format is not Valid \n";
        }

        Boolean diff_Verify = false;
        if(startVerify && endVerify){
            Double f = Double.parseDouble(startDate[0]);
            Double s = Double.parseDouble(endDate[0]);
            Double fm = Double.parseDouble(startDate[1]);
            Double sm = Double.parseDouble(endDate[1]);
            Double fy = Double.parseDouble(startDate[2]);
            Double sy = Double.parseDouble(endDate[2]);
            Double d_diff = s-f;
            Double m_diff = sm-fm;
            Double y_diff = sy - fy;
            diff_Verify = (d_diff >= 0.0 &&  m_diff >=0.0) || (d_diff < 0.0 && m_diff > 0.0) && (y_diff >=0.0);
        }
        if(!diff_Verify){
            message+="3 - End Date shouldn't be less than the Start Date \n";
        }
        if(message!=""){
            Toast.makeText(WaterRequest.this, message, Toast.LENGTH_LONG).show();
        }
        return startVerify && endVerify && diff_Verify;
    }

    public Boolean timeVerify(String s, String e){
        Boolean res =false;
        Boolean lengthVerify = s.split(":").length ==2 && e.split(":").length ==2;
        if(lengthVerify){
            Boolean numVerify = isNum(s.split(":")[0]) && isNum(s.split(":")[1]) && isNum(e.split(":")[0]) && isNum(e.split(":")[1]);
            if(numVerify){
                Double sh = Double.parseDouble(s.split(":")[0]);
                Double sm = Double.parseDouble(s.split(":")[1]);
                Double eh = Double.parseDouble(e.split(":")[0]);
                Double em = Double.parseDouble(e.split(":")[1]);
                if(eh-sh == 0.0 && em-sm > 0.0){
                    res=true;
                }else if(eh-sh > 0.0){
                    res = true;
                }else{
                    res = false;
                    Toast.makeText(WaterRequest.this, "End time should not be less than start time", Toast.LENGTH_LONG).show();
                }
            }
        }
        return res;
    }

    public Boolean isNum(String n){
        try{
            Double.parseDouble(n);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public void addRequest(){
        startDateHolder = startDate.getText().toString();
        endDateHolder = endDate.getText().toString();
        cropNameHolder = cropName.getText().toString();
        startTimeHolder = startTime.getText().toString();
        endTimeHolder = endTime.getText().toString();
        try {
            if (TextUtils.isEmpty(startDateHolder) || TextUtils.isEmpty(endDateHolder) || TextUtils.isEmpty(cropNameHolder) || TextUtils.isEmpty(startTimeHolder) || TextUtils.isEmpty(endTimeHolder)) {
                Toast.makeText(WaterRequest.this, "Incomplete Form", Toast.LENGTH_LONG).show();
                return;
            } else {
                if(dateVerify(startDateHolder,endDateHolder) && timeVerify(startTimeHolder,endTimeHolder)){
                    Toast.makeText(WaterRequest.this, "Form Validated", Toast.LENGTH_LONG).show();
                    collectionReference.whereEqualTo("CNIC",EmailHolder).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if(queryDocumentSnapshots.getDocuments().size()>0){
                                DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                                WaterRequest.this.horsepower = doc.getString("TubewellHorsepower");
                                WaterRequest.this.boringInFeet = doc.getString("Boringinfeet");
                                Intent intent = new Intent(getApplicationContext(),RequestComparator.class);
                                String t = startDateHolder+"-"+endDateHolder+"-"+cropNameHolder+"-"+startTimeHolder+"-"+endTimeHolder+"-Original Request-"+horsepower+"-"+boringInFeet+"-"+EmailHolder;
                                intent.putExtra("Request",t);
                                WaterRequest.this.startActivity(intent);
                                endActivity();
                            }else{
                                if(e!=null)
                                    Toast.makeText(WaterRequest.this,e.toString(),Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(WaterRequest.this,"No Result for This id",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }catch (Exception ex){
            Toast.makeText(WaterRequest.this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private void updateStartLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate.setText(sdf.format(startCalendar.getTime()));
    }
    private void updateEndLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        endDate.setText(sdf.format(endCalendar.getTime()));
    }
}
