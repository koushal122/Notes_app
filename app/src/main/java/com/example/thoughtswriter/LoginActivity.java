package com.example.thoughtswriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalAPI;

public class LoginActivity extends AppCompatActivity {

    private Button createbutton;
    private Button loginbutton;
    private EditText Emailedittext;
    private  EditText Passwordedittext;
    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;


    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setElevation(0);
        createbutton=findViewById(R.id.CreateAccountbutton);
        loginbutton=findViewById(R.id.loginbutton);
        Emailedittext=findViewById(R.id.Emailedittext);
        Passwordedittext=findViewById(R.id.passwordedittext);
        progressBar=findViewById(R.id.progressBar);

        firebaseAuth=FirebaseAuth.getInstance();


        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActitvity.class));
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginwithemailandpwd(Emailedittext.getText().toString().trim()
                                     ,Passwordedittext.getText().toString().trim());
            }
        });
    }

    private void Loginwithemailandpwd(String email, String pwd) {

        if(!TextUtils.isEmpty(email)
        &&!TextUtils.isEmpty(pwd))
        {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            String userid=user.getUid();

                            collectionReference.whereEqualTo("userid",userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException error) {

                                    if(error!=null)
                                    {
                                        Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        if(!value.isEmpty())
                                        {
                                            for(QueryDocumentSnapshot querySnapshot:value)
                                            {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                JournalAPI journalAPI=JournalAPI.getInstance();
                                                journalAPI.setUsername(querySnapshot.getString("username"));
                                                journalAPI.setUserid(querySnapshot.getString("userid"));
                                                startActivity(new Intent(LoginActivity.this,PostActivityContent.class));
                                            }
                                        }


                                    }


                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Email not registered Create account First", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "please enter email and password", Toast.LENGTH_SHORT).show();
        }

    }
}