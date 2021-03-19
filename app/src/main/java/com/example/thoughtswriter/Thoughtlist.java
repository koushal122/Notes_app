package com.example.thoughtswriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import modal.Thoughtclass;
import util.JournalAPI;
import util.RecyclerViewAdapter;

public class Thoughtlist extends AppCompatActivity {


    private RecyclerView recyclerView;
    private List<Thoughtclass> thoughtclassList;
    private TextView nothought;

    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private StorageReference storageReference;
    RecyclerViewAdapter recyclerViewAdapter;


    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Toughts");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughtlist);
        getSupportActionBar().setElevation(0);
        nothought=findViewById(R.id.nothoghtTextview);
        firebaseAuth=FirebaseAuth.getInstance();
        currentuser=firebaseAuth.getCurrentUser();

        thoughtclassList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerViewthoughtlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser=firebaseAuth.getCurrentUser();
                if(currentuser!=null)
                {

                }
                else
                {

                }
            }
        };


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.addbuton:
                //add the jouranl
                if(currentuser!=null && firebaseAuth!=null)
                {
                    startActivity(new Intent(Thoughtlist.this,PostActivityContent.class));
                    finish();
                }
                break;
            case R.id.signout:
            {
                if(currentuser!=null && firebaseAuth!=null)
                {
                    firebaseAuth.signOut();
                    startActivity(new Intent(Thoughtlist.this,MainActivity.class));
                    Toast.makeText(this, "You Were Logged out Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        collectionReference.whereEqualTo("userid", JournalAPI.getInstance().getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       if(!queryDocumentSnapshots.isEmpty()) {
                           for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                              Thoughtclass thoughtclass=snapshot.toObject(Thoughtclass.class);
                               thoughtclassList.add(thoughtclass);

                           }
                           recyclerViewAdapter=new RecyclerViewAdapter(thoughtclassList,Thoughtlist.this);
                           recyclerView.setAdapter(recyclerViewAdapter);
                           recyclerViewAdapter.notifyDataSetChanged();
                       }
                       else
                       {
                         nothought.setVisibility(View.VISIBLE);
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }
}