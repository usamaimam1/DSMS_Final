package com.example.usama.dsms_fb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser =null;
    private FirebaseFirestore firebaseFireStore;
    private CollectionReference collectionReference;

    Button LogInButton, RegisterButton ;
    EditText Email, Password ;
    ProgressBar progressBar;

    public String EmailHolder,PasswordHolder,TempPassword,UserName;
    public Boolean EditTextEmptyHolder,databaseConnection;

    public static final String UserEmail = "";
    public static final String userName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogInButton = (Button)findViewById(R.id.buttonLogin);
        RegisterButton = (Button)findViewById(R.id.buttonRegister);

        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                firebaseUser = mAuth.getCurrentUser();
            }
        });

        firebaseFireStore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFireStore.collection("Signup_Credentials");

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempPassword = "Not Found";
                CheckEditTextStatus();
                LoginFunction();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    public void CheckEditTextStatus(){
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }
    public String LoginFunction(){
        String result="Done";
        databaseConnection=false;
        if(EditTextEmptyHolder) {
            collectionReference.whereEqualTo("CNIC",EmailHolder).whereEqualTo("Password",PasswordHolder).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    Boolean found = queryDocumentSnapshots.getDocuments().size() > 0;
                    if(found){
                        MainActivity.this.TempPassword = queryDocumentSnapshots.getDocuments().get(0).getString("Password");
                        MainActivity.this.UserName = queryDocumentSnapshots.getDocuments().get(0).getString("CNIC");
                        Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        intent.putExtra(userName,UserName );
                        intent.putExtra(UserEmail,EmailHolder);
                        startActivity(intent);
                    }else{
                        if(e!=null)
                            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            result = "Please Enter UserName or Password.";
        }
        return result;
    }
}
