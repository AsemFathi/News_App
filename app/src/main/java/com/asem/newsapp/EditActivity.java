package com.asem.newsapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    EditText title, desc , day, month, year;
    ImageButton imageButton;
    Button post;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    StorageReference imagesRef;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    String tit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.edit_news_title);
        desc = findViewById(R.id.edit_news_post);
        day = findViewById(R.id.edit_day);
        month = findViewById(R.id.edit_month);
        year = findViewById(R.id.edit_year);
        imageButton = findViewById(R.id.edit_news_image);
        post = findViewById(R.id.edit_btn_save);

        tit = getIntent().getStringExtra("title");

        title.setText(tit);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        imagesRef = storageReference.child("posts");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String des = desc.getText().toString();
                String dday = day.getText().toString();
                String mmonth = month.getText().toString();
                String yyear = year.getText().toString();
                databaseReference.child(tit).removeValue();
                uploadImage();
                DatabaseReference reference = databaseReference.child(Title);
                reference.child("title").setValue(Title);
                reference.child("description").setValue(des);
                reference.child("day").setValue(dday);
                reference.child("month").setValue(mmonth);
                reference.child("year").setValue(yyear);



            }
        });
    }


    void SelectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                imageButton.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(){
        StorageReference imageRef = imagesRef.child(filePath.getLastPathSegment());

        // Upload the file to Firebase Storage
        imageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded file
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                // Use the download URL as needed
                                Log.i(TAG, "onSuccess: url" + downloadUrl);
                                String Title = title.getText().toString();
                                DatabaseReference reference = databaseReference.child(Title);
                                reference.child("image").setValue(downloadUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle errors
                    }
                });
    }
}