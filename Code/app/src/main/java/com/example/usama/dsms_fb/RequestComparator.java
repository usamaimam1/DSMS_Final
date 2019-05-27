package com.example.usama.dsms_fb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RequestComparator extends AppCompatActivity {
    Intent intent;
    private ListView listview;
    private RequestComparatorDataAdapter requestComparatorDataAdapter;
    String NameHolder;
    ArrayList<WaterRequestData> requestComparatorDataAdapters;
    WaterRequestData originalRequest;
    String [] var;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_comparator);

        listview = (ListView) findViewById(R.id.RequestComparatorList);
        requestComparatorDataAdapters = new ArrayList<>();
        intent =getIntent();
        String request = intent.getStringExtra("Request");
        Toast.makeText(RequestComparator.this , request , Toast.LENGTH_LONG).show();

        Button submitOriginal =(Button) findViewById(R.id.submitOriginal);
        Button submitAlternate =(Button) findViewById(R.id.submitAlternate);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseUser = mAuth.getCurrentUser();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(Entries.WR_TableName);

        var = request.split("-");
        try{
            originalRequest = new WaterRequestData(var[0],var[1],var[2],var[3],var[4],var[5],var[6],var[7],var[8]);
            requestComparatorDataAdapters.add(originalRequest);
            generateAlternateRequest();
            setListView();
        }catch (Exception ex){
            Toast.makeText(RequestComparator.this , ex.toString() , Toast.LENGTH_LONG).show();
        }

        submitOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest(requestComparatorDataAdapters.get(0));
            }
        });

        submitAlternate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest(requestComparatorDataAdapters.get(1));
            }
        });
    }

    public void setListView(){
        //Collections.reverse(requestComparatorDataAdapters);
        requestComparatorDataAdapter = new RequestComparatorDataAdapter(this,requestComparatorDataAdapters);
        listview.setAdapter(requestComparatorDataAdapter);
    }
    public void generateAlternateRequest(){
        String tempStartTime = var[3];
        String tempEndTime = var[4];
        Integer start = Integer.parseInt(tempStartTime.split(":")[0]);
        Integer end = Integer.parseInt(tempEndTime.split(":")[0]);
        Integer newStart = start;
        Integer newEnd =end;
        ArrayList<WaterRequestData> alternateRequests = new ArrayList<>();
        while(start>0 && end <23){
            start++;
            end++;
            String nS = Integer.toString(start)+":"+tempStartTime.split(":")[1];
            String nE = Integer.toString(end)+":"+tempEndTime.split(":")[1];
            WaterRequestData aRequest = new WaterRequestData(var[0],var[1],var[2],nS,nE,"Alternate Request",var[6],var[7],var[8]);
            alternateRequests.add(aRequest);
            //requestComparatorDataAdapters.add(aRequest);
        }
        while(newStart>0 && newEnd <23){
            newStart--;
            newEnd--;
            String nS = Integer.toString(newStart)+":"+tempStartTime.split(":")[1];
            String nE = Integer.toString(newEnd)+":"+tempEndTime.split(":")[1];
            WaterRequestData aRequest = new WaterRequestData(var[0],var[1],var[2],nS,nE,"Alternate Request",var[6],var[7],var[8]);
            alternateRequests.add(aRequest);
            //requestComparatorDataAdapters.add(aRequest);
        }
        Collections.sort(alternateRequests, new Comparator<WaterRequestData>() {
            @Override
            public int compare(WaterRequestData c1, WaterRequestData c2) {
                return Double.compare(c1.getExpectedBill(), c2.getExpectedBill());
            }
        });
        //Collections.reverse(alternateRequests);
        requestComparatorDataAdapters.add(alternateRequests.get(0));
    }
    public void endActivity(){
        this.finish();
    }

    public void submitRequest(WaterRequestData waterRequestData){
        Map<String,String> request = new HashMap<>();
        request.put(Entries.WR_Table2,waterRequestData.getStartDate());
        request.put(Entries.WR_Table3,waterRequestData.getEndDate());
        request.put(Entries.WR_Table4,waterRequestData.getCropName());
        request.put(Entries.WR_Table5,waterRequestData.getStartTime());
        request.put(Entries.WR_Table6,waterRequestData.getEndTime());
        request.put(Entries.WR_Table7,waterRequestData.getCNIC());
        collectionReference.add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RequestComparator.this,"Request Added!",Toast.LENGTH_LONG).show();
                RequestComparator.this.endActivity();
            }
        });

    }
}
