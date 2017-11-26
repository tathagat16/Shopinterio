package com.example.application.shopinterio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import Fragments.UserProfile;

public class UserProfileActivity extends AppCompatActivity {
    public static final int GALLERY_INTENT = 1234;
    String email;
    EditText fname, lname, age, address;
    ImageView image;
    Button addPhoto;
    CollectionReference db = FirebaseFirestore.getInstance().collection("userData");
    FirebaseUser currentUser;
    ProgressDialog mProgressDialogue;
    FirebaseAuth mAuth;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();
        fname = findViewById(R.id.fname1);
        lname = findViewById(R.id.lname1);
        age = findViewById(R.id.age1);
        address = findViewById(R.id.address1);

        Button update = findViewById(R.id.updateProfile);

        mProgressDialogue = new ProgressDialog(this);

        image = findViewById(R.id.image_view);
        addPhoto = findViewById(R.id.addPhoto);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),GALLERY_INTENT);

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> docData = new HashMap<>();
                docData.put("email", email);
                docData.put("first_name", fname.getText().toString());
                docData.put("last_name", lname.getText().toString());
                docData.put("age", age.getText().toString());
                docData.put("address", address.getText().toString());

                db.document(email).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Patient ", "Data saved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error ", "Data Not Saved !");
                    }
                });

                startActivity(new Intent(UserProfileActivity.this, UserProfile.class));
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mProgressDialogue.setMessage("Uploading Image...");
            mProgressDialogue.show();

            final Uri uri = data.getData();

            StorageReference filePath = mStorage.child("Photos").child(email);
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    image.setImageURI(uri);
                    mProgressDialogue.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Photo Upload Successful !", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfileActivity.this, "Phot upload Failed !", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}