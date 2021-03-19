package com.example.thoughtswriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.FeatureGroupInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import modal.Thoughtclass;
import util.JournalAPI;

public class MainActivity extends AppCompatActivity {
    private Button getstartedbutton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("users");
    private FirebaseUser currentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getstartedbutton=findViewById(R.id.Getstarted);
        firebaseAuth=FirebaseAuth.getInstance();



        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser=firebaseAuth.getCurrentUser();

                if(currentuser!=null)
                {
                    //it mean user is logged in
                    currentuser=firebaseAuth.getCurrentUser();
                    collectionReference.whereEqualTo("userid",currentuser.getUid())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException error) {
                                    if(error!=null)
                                    {
                                        return;
                                    }
                                    else
                                    {
                                        if(!value.isEmpty())
                                        {
                                            for(QueryDocumentSnapshot snapshot:value)
                                            {
                                                JournalAPI journalAPI=JournalAPI.getInstance();
                                                journalAPI.setUsername(snapshot.getString("username"));
                                                journalAPI.setUserid(snapshot.getString("userid"));
                                                startActivity(new Intent(MainActivity.this, Thoughtlist.class));
                                                finish();
                                            }
                                        }
                                    }
                                }
                            });

                }else {
                    //user is not logged in

                }



            }
        };

        getstartedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}