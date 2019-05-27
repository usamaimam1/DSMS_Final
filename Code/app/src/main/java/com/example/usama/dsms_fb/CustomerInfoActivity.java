package com.example.usama.dsms_fb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class CustomerInfoActivity extends AppCompatActivity {
    String  EmailHolder, PostalAddressHolder,ConsumerIDHolder, ReferenceNumberHolder, AgricullturalAreaHolder, TubwellHorsepowerHolder, BoringinfeetHolder,WaterpipediameterHolder,SmartmeterAvailablityHolder,PaymentMethodHolder;
    TextView Email, PostalAddress, ConsumerID, ReferenceNumber, AgriculturalArea, TubewellHorsepower, Boringinfeet, WaterPipeDiameter, SmartMeterAvailablity, PaymentMethod;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(DashboardActivity.email);

        Email = (TextView)findViewById(R.id.cnic);
        PostalAddress = (TextView)findViewById(R.id.textPostal);
        ConsumerID = (TextView)findViewById(R.id.textConsumerID);
        ReferenceNumber = (TextView)findViewById(R.id.textReferenceNumber);
        AgriculturalArea = (TextView)findViewById(R.id.textAgriculturalArea);
        TubewellHorsepower = (TextView)findViewById(R.id.textTubewellHorsepower);
        Boringinfeet = (TextView)findViewById(R.id.textBoringinfeet);
        WaterPipeDiameter = (TextView)findViewById(R.id.textWaterpipediameter);
        SmartMeterAvailablity = (TextView)findViewById(R.id.textSmartmeterAvailability);
        PaymentMethod = (TextView)findViewById(R.id.textPaymentMethod);

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
                if(e==null && queryDocumentSnapshots.getDocuments().size()>0){
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    PostalAddressHolder = doc.getString(Entries.Table_Column_4_PostalAddres);
                    ConsumerIDHolder = doc.getString(Entries.Table_Column_5_ConsumerId);
                    ReferenceNumberHolder = doc.getString(Entries.Table_Column_6_ReferenceNumber);
                    AgricullturalAreaHolder =doc.getString(Entries.Table_Column_7_AgriculturalArea);
                    TubwellHorsepowerHolder = doc.getString(Entries.Table_Column_8_TubewellHorsePower);
                    BoringinfeetHolder = doc.getString(Entries.Table_Column_9_Boringinfeet);
                    WaterpipediameterHolder = doc.getString(Entries.Table_Column_10_WaterpipeDiameter);
                    SmartmeterAvailablityHolder = doc.getString(Entries.Table_Column_11_SmartMeterAvailability);
                    PaymentMethodHolder = doc.getString(Entries.Table_Column_12_PaymentMethod);

                    Email.setText(Email.getText().toString() + EmailHolder);
                    PostalAddress.setText(PostalAddress.getText()+PostalAddressHolder);
                    ConsumerID.setText(ConsumerID.getText()+ConsumerIDHolder);
                    ReferenceNumber.setText(ReferenceNumber.getText()+ReferenceNumberHolder);
                    AgriculturalArea.setText(AgriculturalArea.getText()+AgricullturalAreaHolder);
                    TubewellHorsepower.setText(TubewellHorsepower.getText()+TubwellHorsepowerHolder);
                    Boringinfeet.setText(Boringinfeet.getText()+BoringinfeetHolder);
                    WaterPipeDiameter.setText(WaterPipeDiameter.getText()+WaterpipediameterHolder);
                    SmartMeterAvailablity.setText(SmartMeterAvailablity.getText()+SmartmeterAvailablityHolder);
                    PaymentMethod.setText(PaymentMethod.getText()+PaymentMethodHolder);
                }
            }
        });
    }
}
