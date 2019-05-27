package com.example.usama.dsms_fb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import javax.annotation.Nullable;

public class DashboardActivity extends AppCompatActivity {
    String NameHolder,EmailHolder;
    TextView Name;
    Button LogOUT, waterRequest, viewRequests, viewInfo, viewCustomerInfo;
    public static final String email="";

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Name = (TextView)findViewById(R.id.textView1);

        LogOUT = (Button)findViewById(R.id.button1);
        waterRequest = (Button) findViewById(R.id.button2);
        viewRequests = (Button) findViewById(R.id.button3);
        viewInfo = (Button) findViewById(R.id.button4);
        viewCustomerInfo =(Button)findViewById(R.id.button5);

        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(MainActivity.UserEmail);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseUser = mAuth.getCurrentUser();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Signup_Credentials");

        collectionReference.whereEqualTo("CNIC",EmailHolder).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    DashboardActivity.this.NameHolder =  doc.getString(Entries.Table_Column_1_Name);
                    DashboardActivity.this.Name.setText(DashboardActivity.this.Name.getText()+ DashboardActivity.this.NameHolder);
                }else{
                    if(e!=null)
                        Toast.makeText(DashboardActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(DashboardActivity.this,"No Result for This id",Toast.LENGTH_LONG).show();
                }
            }
        });

        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finishing current DashBoard activity on button click.
                finish();
                Toast.makeText(DashboardActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();

            }
        });
        waterRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(DashboardActivity.this,WaterRequest.class);
                intent.putExtra(email,EmailHolder);
                startActivity(intent);
            }
        });
        viewRequests.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(DashboardActivity.this,showRequests.class);
                intent.putExtra(email,EmailHolder);
                startActivity(intent);
            }
        });
        viewInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                Intent intent = new Intent(DashboardActivity.this,InfoActivity.class);
//                intent.putExtra(email,EmailHolder);
//                startActivity(intent);
            }
        });
        viewCustomerInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(DashboardActivity.this,CustomerInfoActivity.class);
                intent.putExtra(email,EmailHolder);
                startActivity(intent);
            }
        });
    }
}
