package com.example.usama.dsms_fb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;

public class showRequests extends AppCompatActivity {
    private ListView listview;
    private WaterRequestDataAdapter waterRequestDataAdapter;
    String NameHolder;
    ArrayList<WaterRequestData> waterRequestDataArrayList;

    private String hp = "";
    private String bif ="";
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private  FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);

        listview = (ListView) findViewById(R.id.waterRequestList);
        WaterRequestDataAdapter waterRequestDataAdapter;
        waterRequestDataArrayList = new ArrayList<>();
        Intent intent = getIntent();
        NameHolder = intent.getStringExtra(DashboardActivity.email);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseUser = mAuth.getCurrentUser();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Signup_Credentials");

        collectionReference.whereEqualTo("CNIC",NameHolder).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Toast.makeText(showRequests.this,e.toString(),Toast.LENGTH_LONG).show();
                }else{
                    if(queryDocumentSnapshots.getDocuments().size()>0){
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        showRequests.this.hp = doc.getString("TubewellHorsepower");
                        showRequests.this.bif = doc.getString("Boringinfeet");
                        firebaseFirestore.collection(Entries.WR_TableName).whereEqualTo(Entries.WR_Table7,showRequests.this.NameHolder).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if(queryDocumentSnapshots.getDocuments().size()>0){
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                        String sD = document.getString("_startDate");
                                        String eD = document.getString("_endDate");
                                        String cN = document.getString("_cropName");
                                        String sT = document.getString("_startTime");
                                        String eT = document.getString("_endTime");
                                        String rID = document.getString("_RequestID");
                                        WaterRequestData waterRequestData = new WaterRequestData(sD,eD,cN,sT,eT,rID,showRequests.this.hp,showRequests.this.bif,showRequests.this.NameHolder);
                                        Log.d("Water Request Data : ",waterRequestData.printWaterRequestData());
                                        showRequests.this.waterRequestDataArrayList.add(waterRequestData);
                                    }
                                    Collections.reverse(waterRequestDataArrayList);
                                    showRequests.this.waterRequestDataAdapter = new WaterRequestDataAdapter(showRequests.this,waterRequestDataArrayList);
                                    showRequests.this.listview.setAdapter(showRequests.this.waterRequestDataAdapter);
                                }else{

                                }
                            }
                        });
                    }else{
                        Toast.makeText(showRequests.this,"No data against this id",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
