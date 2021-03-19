package com.example.thoughtswriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import modal.Thoughtclass;
import util.JournalAPI;

public class PostActivityContent extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private Button savebutton;
    private EditText tittle;
    private EditText thought;
    private ProgressBar progressBar;
    private ImageView camerabutton;
    private ImageView backgroundimage;
    private TextView postusername;
    private TextView date;
    private  Uri imageURi;
    String username;
    String userid;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    StorageReference storageRef;

    private final CollectionReference collectionReference = db.collection("Toughts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        getSupportActionBar().setElevation(0);
        storageRef= FirebaseStorage.getInstance().getReference();
        savebutton = findViewById(R.id.postsavebutton);
        tittle = findViewById(R.id.posttittle_et);
        thought = findViewById(R.id.postthought_et);
        progressBar = findViewById(R.id.postprogressbar);
        camerabutton = findViewById(R.id.post_camerabutton);
        backgroundimage = findViewById(R.id.postImageview);
        postusername = findViewById(R.id.postusername);
        date = findViewById(R.id.postdate);

        savebutton.setOnClickListener(this);
        camerabutton.setOnClickListener(this);


        if(JournalAPI.getInstance()!=null)
        {
            username=JournalAPI.getInstance().getUsername();
            userid=JournalAPI.getInstance().getUserid();
            postusername.setText(username);
        }


        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                if(user!=null)
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postsavebutton:
                createjournal();
                break;
            case R.id.post_camerabutton:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;

        }
    }

    private void createjournal() {
        progressBar.setVisibility(View.VISIBLE);
        final String tittlestirng=tittle.getText().toString().trim();
        final String thoughtstring=thought.getText().toString().trim();

        if(!TextUtils.isEmpty(tittlestirng)
        &&!TextUtils.isEmpty(thoughtstring) && imageURi!=null)
        {
            final StorageReference reference=storageRef
                    .child("Thought_image")
                    .child("image "+ Timestamp.now().getSeconds());


            reference.putFile(imageURi)
                     .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                       reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String imageurl=uri.toString();


                            Thoughtclass thoughtclass=new Thoughtclass();
                            thoughtclass.setTittle(tittlestirng);
                            thoughtclass.setThought(thoughtstring);
                            thoughtclass.setUsername(username);
                            thoughtclass.setUserid(userid);
                            thoughtclass.setTimeadded(new Timestamp(new Date()));
                            thoughtclass.setImageurl(imageurl);

                            collectionReference.add(thoughtclass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressBar.setVisibility(View.INVISIBLE);

                                    startActivity(new Intent(PostActivityContent.this,Thoughtlist.class));
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            });

                        }
                        });

                        }

                     }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("PostActivity", "onFailure: photocannotbe uploaded");
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please add image, tittle and thought", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
               imageURi = data.getData();
                backgroundimage.setImageURI(imageURi);
                Log.d("Imagetest", "onActivityResult: came");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
             if(firebaseAuth!=null) {
            user=firebaseAuth.getCurrentUser();
            firebaseAuth.addAuthStateListener(authStateListener);
             }

    }


}