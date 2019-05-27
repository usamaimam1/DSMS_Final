package com.example.usama.dsms_fb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RegisterActivity extends AppCompatActivity {

    public static final String UserEmail = "";
    public static final String userName="";
    EditText Email, Password, Name , PostalAddress, ConsumerID, ReferenceNumber, AgriculturalArea , TubewellHorsepower, BoringInfeet, WaterPipeDiameter, SmartMeterAvailability, PaymentMethod ;
    Button Register;
    String NameHolder, EmailHolder, PasswordHolder , PostalAddressHolder, ConsumerIDHolder, ReferenceNumberHolder, AgricultureAreaHolder, TubewellHorsepowerHolder, BoringinfeetHolder, WaterPipeDiameterHolder, SmartMeterAvailabilityHolder, PaymentMethodHolder;
    Boolean EditTextEmptyHolder, RegistrationCredentialsVerified;
    final Map<String,String> signupDoc = new HashMap<>();

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser=null;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private Entries entry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        entry = new Entries();

        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        Name = (EditText)findViewById(R.id.editName);
        PostalAddress = (EditText)findViewById(R.id.editPostalAddress);
        ConsumerID = (EditText)findViewById(R.id.editConsumerID);
        ReferenceNumber = (EditText)findViewById(R.id.editReferenceNumber);
        AgriculturalArea = (EditText)findViewById(R.id.editAgricultualArea);
        TubewellHorsepower =(EditText)findViewById(R.id.editTubewellHorsePower);
        BoringInfeet = (EditText)findViewById(R.id.editBoringinfeet);
        WaterPipeDiameter = (EditText)findViewById(R.id.editwaterPipeDiameter);
        SmartMeterAvailability = (EditText)findViewById(R.id.editSmartMeterAvailability);
        PaymentMethod = (EditText)findViewById(R.id.editPaymentMethod);

        Register = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseUser = mAuth.getCurrentUser();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Signup_Credentials");

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextStatus();
                Toast.makeText(RegisterActivity.this,verifyCredentials(),Toast.LENGTH_LONG).show();
                InsertDocument();
            }
        });

    }

    public void CheckEditTextStatus(){
        NameHolder = Name.getText().toString() ;
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        PostalAddressHolder = PostalAddress.getText().toString();
        ConsumerIDHolder = ConsumerID.getText().toString();
        ReferenceNumberHolder = ReferenceNumber.getText().toString();
        AgricultureAreaHolder = AgriculturalArea.getText().toString();
        TubewellHorsepowerHolder = TubewellHorsepower.getText().toString();
        BoringinfeetHolder = BoringInfeet.getText().toString();
        WaterPipeDiameterHolder = WaterPipeDiameter.getText().toString();
        SmartMeterAvailabilityHolder = SmartMeterAvailability.getText().toString();
        PaymentMethodHolder = SmartMeterAvailability.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(PostalAddressHolder) || TextUtils.isEmpty(ConsumerIDHolder) || TextUtils.isEmpty(ReferenceNumberHolder) || TextUtils.isEmpty(AgricultureAreaHolder) || TextUtils.isEmpty(TubewellHorsepowerHolder) || TextUtils.isEmpty(BoringinfeetHolder) || TextUtils.isEmpty(WaterPipeDiameterHolder) || TextUtils.isEmpty(SmartMeterAvailabilityHolder) || TextUtils.isEmpty(PaymentMethodHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }
    public String verifyCredentials(){
        String result="";
        RegistrationCredentialsVerified =false;
        Boolean cnic_verify =  EmailHolder.matches("[0-9]+") && EmailHolder.length() == 13 ;
        Boolean pass_length =  PasswordHolder.length() > 4;
        Boolean sm_available = SmartMeterAvailabilityHolder.indexOf('Y') == -1 || SmartMeterAvailabilityHolder.indexOf('y')== -1 || SmartMeterAvailabilityHolder.indexOf('N')==-1 || SmartMeterAvailabilityHolder.indexOf('n')==-1;
        Boolean hp_Convertible = checkparseability(TubewellHorsepowerHolder);
        Boolean bif_Convertible = checkparseability(BoringinfeetHolder);
        if(!cnic_verify){
            result+=" \n Invalid CNIC \n";
        }else if(!pass_length){
            result+="\n Password Length should be greater than 4 \n";
        }else if(!sm_available){
            result+=" \n Smart meter Availability field should be yes or no \n";
        }else if(! hp_Convertible){
            result+=" \n Horsepower Should be a decimal value \n";
        }else if(! bif_Convertible){
            result+=" \n Boring in feet should be a decimal value \n";
        }else{
            RegistrationCredentialsVerified = true;
            result+="\n Credentials Verified \n";
        }
        return result;
    }
    public Boolean checkparseability(String toCheck){
        Boolean res = false;
        try{
            Double.parseDouble(toCheck);
            res= true;
        }catch (Exception ex){
            return res;
        }
        return res;
    }
    public void InsertDocument(){
        if(EditTextEmptyHolder && RegistrationCredentialsVerified){
            signupDoc.put(entry.Table_Column_1_Name,NameHolder);
            signupDoc.put(entry.Table_Column_2_Email,EmailHolder);
            signupDoc.put(entry.Table_Column_3_Password,PasswordHolder);
            signupDoc.put(entry.Table_Column_4_PostalAddres,PostalAddressHolder);
            signupDoc.put(entry.Table_Column_5_ConsumerId,ConsumerIDHolder);
            signupDoc.put(entry.Table_Column_6_ReferenceNumber,ReferenceNumberHolder);
            signupDoc.put(entry.Table_Column_7_AgriculturalArea,AgricultureAreaHolder);
            signupDoc.put(entry.Table_Column_8_TubewellHorsePower,TubewellHorsepowerHolder);
            signupDoc.put(entry.Table_Column_9_Boringinfeet,BoringinfeetHolder);
            signupDoc.put(entry.Table_Column_10_WaterpipeDiameter,WaterPipeDiameterHolder);
            signupDoc.put(entry.Table_Column_11_SmartMeterAvailability,SmartMeterAvailabilityHolder);
            signupDoc.put(entry.Table_Column_12_PaymentMethod,PaymentMethodHolder);

            DocumentReference doc = collectionReference.document(EmailHolder);
            doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()){
                        Toast.makeText(RegisterActivity.this, "\n Entry with these credentials already exists \n",Toast.LENGTH_LONG).show();
                    }else{
                        RegisterActivity.this.collectionReference.document(RegisterActivity.this.EmailHolder).set(RegisterActivity.this.signupDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                intent.putExtra(userName,NameHolder );
                                intent.putExtra(UserEmail,EmailHolder);
                                startActivity(intent);
                                RegisterActivity.this.finish();
                                RegisterActivity.this.RegistrationCredentialsVerified =false;
                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this,"Doc Insertion Failed",Toast.LENGTH_LONG).show();
        }
    }
}
