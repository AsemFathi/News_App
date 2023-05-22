package com.asem.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comments extends AppCompatActivity {

    String myname,
            myemail;
    LatLng latLng;
    String img = "";
    List<ModelComment> commentList;
    AdapterComment adapterComment;

    CircleImageView AuthorImage , SenderImage;
    TextView AuthorName;
    TextView PostTime , Title , Description , Likes , commentsss, tempView;
    ImageView Image;
    RecyclerView recyclerView;
    EditText comment;
    ImageButton Send;
    DatabaseReference databaseReference , ref;
    String tit;
    FirebaseAuth auth;
    Button location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        AuthorImage = findViewById(R.id.authorPhoto);
        AuthorName = findViewById(R.id.autharName);
        PostTime = findViewById(R.id.postTime);
        Title = findViewById(R.id.postTitle);
        Description = findViewById(R.id.postDescription);
        Likes = findViewById(R.id.postLikes);
        commentsss = findViewById(R.id.postComments);
        Image = findViewById(R.id.postImage);
        recyclerView = findViewById(R.id.recyclerComment);
        comment = findViewById(R.id.typeComment);
        Send = findViewById(R.id.sendComment);
        tit = getIntent().getStringExtra("pid");
        auth = FirebaseAuth.getInstance();
        SenderImage = findViewById(R.id.commentImg);
        myemail = auth.getCurrentUser().getEmail();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        String mail = myemail.replaceAll("@gmail.com" , "");
        location = findViewById(R.id.locationBTN);
        tempView = findViewById(R.id.tempView);


        //to get username and image
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myname = snapshot.child(mail).child("Full Name").getValue().toString();
                img = snapshot.child(mail).child("image").getValue().toString();
                Glide.with(com.asem.newsapp.Comments.this)
                        .load(img)
                        .into(SenderImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        DatabaseReference reference = databaseReference.child(tit);

        //load Post info
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String title = "" , des="" , time ="", image="" , likes ="0" , comments="0" , author ="" , authorImage="" , temp = "" ;

                if (dataSnapshot.child("title").exists())
                    title = dataSnapshot.child("title").getValue().toString();
                if (dataSnapshot.child("description").exists())
                    des = dataSnapshot.child("description").getValue().toString();
                if (dataSnapshot.child("time").exists())
                    time =  dataSnapshot.child("time").getValue().toString();
                if (dataSnapshot.child("image").exists())
                    image = dataSnapshot.child("image").getValue().toString();
                if (dataSnapshot.child("likes").exists())
                    likes = dataSnapshot.child("likes").getValue().toString();
                if (dataSnapshot.child("comment").exists())
                    comments = dataSnapshot.child("comment").getValue().toString();
                if (dataSnapshot.child("author").exists())
                    author = dataSnapshot.child("author").getValue().toString();
                if (dataSnapshot.child("authorImage").exists())
                    authorImage = dataSnapshot.child("authorImage").getValue().toString();
                if (dataSnapshot.child("latlng").exists()){
                    double latitude = dataSnapshot.child("latlng").child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("latlng").child("longitude").getValue(Double.class);
                    latLng = new LatLng(latitude , longitude);
                }
                if (dataSnapshot.child("temp").exists()){
                    temp = dataSnapshot.child("temp").getValue().toString();
                }


                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.setTimeInMillis(Long.parseLong(time));
                String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                Title.setText(title);
                Description.setText(des);
                Glide.with(Comments.this)
                        .load(image)
                        .into(Image);
                Likes.setText(likes + " Likes");
                commentsss.setText(comments + " Comments");
                PostTime.setText(timedate);
                AuthorName.setText(author);
                Glide.with(Comments.this)
                        .load(authorImage)
                        .into(AuthorImage);

                tempView.setText(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Show location
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLng != null)
                {
                    Intent intent = new Intent(com.asem.newsapp.Comments.this , MapActivity.class);
                    intent.putExtra("latlng" , latLng);
                    startActivity(intent);
                }

                else
                {
                    Toast.makeText(Comments.this, "No Location", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //EnterComment
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EnterComment();
            }
        });

        //Load Comments
        LoadComments();


    }


    private void EnterComment() {
        final String commentss = comment.getText().toString().trim();
        if (TextUtils.isEmpty(commentss)) {
            Toast.makeText(Comments.this, "Empty comment", Toast.LENGTH_LONG).show();
            return;
        }
        String timestamp = String.valueOf(System.currentTimeMillis());


        DatabaseReference datareference = FirebaseDatabase.getInstance().getReference("Posts").child(tit);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timestamp);
        hashMap.put("comment", commentss);
        hashMap.put("ptime", timestamp);
        hashMap.put("uemail", myemail);
        hashMap.put("uname", myname);
        hashMap.put("image" , img);

        datareference.child("Comments").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Comments.this, "Added", Toast.LENGTH_LONG).show();
                comment.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Comments.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        datareference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count =Integer.parseInt( snapshot.child("comment").getValue().toString());
                count ++;
                datareference.child("comment").setValue(String.valueOf(count));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(tit).child("Comments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String cid = dataSnapshot1.child("cId").getValue().toString();
                    String comment = dataSnapshot1.child("comment").getValue().toString();
                    String img = dataSnapshot1.child("image").getValue().toString();
                    String ptime = dataSnapshot1.child("ptime").getValue().toString();
                    String uemail = dataSnapshot1.child("uemail").getValue().toString();
                    String uname = dataSnapshot1.child("uname").getValue().toString();

                    commentList.add(new ModelComment(cid , comment,img,ptime , uemail , uname));

                }
                adapterComment = new AdapterComment(getApplicationContext(), commentList, tit);
                recyclerView.setAdapter(adapterComment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}