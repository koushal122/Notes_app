package com.example.thoughtswriter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.JournalAPI;

public class CreateAccountActitvity extends AppCompatActivity {

    private Button createaccount;
    EditText username;
    EditText password;
    EditText email;
    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;


    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().setElevation(0);
        createaccount=findViewById(R.id.CreateAccountbuttoncreateacc);
        username=findViewById(R.id.usernameedittextcreateacc);
        password=findViewById(R.id.passwordedittextcreateacc);
        email=findViewById(R.id.Emailedittextcrateacc);
        progressBar=findViewById(R.id.progressBarcrateacc);

        firebaseAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentuser=firebaseAuth.getCurrentUser();
                if(currentuser!=null)
                {
                    //user is logged in
                }
                else
                {

                }
                

            }
        };

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(email.getText().toString()) &&
                        !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(username.getText().toString()))
                {
                    String email1=email.getText().toString().trim();
                    String password1=password.getText().toString().trim();
                    String username1=username.getText().toString().trim();


                    createaccountbyemailandpass(email1,password1,username1);

            }
                else
                {
                    Toast.makeText(CreateAccountActitvity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }

            private void createaccountbyemailandpass(String email, String password, String username) {

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                currentuser=firebaseAuth.getCurrentUser();
                                String userid=currentuser.getUid();
                                Map<String,Object> user=new HashMap<>();
                                user.put("username",username);
                                user.put("userid",userid);

                                db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.getResult().exists())
                                                {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    String name=task.getResult().getString("username");
                                                    JournalAPI.getInstance().setUsername(name);
                                                    JournalAPI.getInstance().setUserid(userid);
                                                    Intent intent=new Intent(CreateAccountActitvity.this,PostActivityContent.class);
                                                    intent.putExtra("username",name);
                                                    intent.putExtra("userid",userid);
                                                    startActivity(intent);

                                                }
                                                else
                                                {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d("Create Accountkoushal", "onFailure: failed ");

                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(CreateAccountActitvity.this, "Account with Email Already exist", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}