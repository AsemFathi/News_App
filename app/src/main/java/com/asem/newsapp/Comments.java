package com.asem.newsapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    String img = "";
    List<ModelComment> commentList;
    AdapterComment adapterComment;

    CircleImageView AuthorImage , SenderImage;
    TextView AuthorName;
    TextView PostTime , Title , Description , Likes , Comments;
    ImageView Image;
    RecyclerView recyclerView;
    EditText comment;
    ImageButton Send;
    DatabaseReference databaseReference , ref;
    String tit;
    FirebaseAuth auth;
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
        Comments = findViewById(R.id.postComments);
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

                String title = "" , des="" , time ="", image="" , likes ="0" , comments="0" , author ="" , authorImage="";
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


                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.setTimeInMillis(Long.parseLong(time));
                String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                Title.setText(title);
                Description.setText(des);
                Glide.with(Comments.this)
                        .load(image)
                        .into(Image);
                Likes.setText(likes + " Likes");
                Comments.setText(comments + " Comments");
                PostTime.setText(timedate);
                AuthorName.setText(author);
                Glide.with(Comments.this)
                        .load(authorImage)
                        .into(AuthorImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    /*
    private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelComment modelComment = dataSnapshot1.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    adapterComment = new AdapterComment(getApplicationContext(), commentList, myuid, postId);
                    recyclerView.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setLikes() {
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(postId).hasChild(myuid)) {
                    likebtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_button_icon, 0, 0, 0);
                    likebtn.setText("Liked");
                } else {
                    likebtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_svgrepo_com, 0, 0, 0);
                    likebtn.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void likepost() {

        mlike = true;
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (mlike) {
                    if (dataSnapshot.child(postId).hasChild(myuid)) {
                        postref.child(postId).child("plike").setValue("" + (Integer.parseInt(plike) - 1));
                        liekeref.child(postId).child(myuid).removeValue();
                        mlike = false;

                    } else {
                        postref.child(postId).child("plike").setValue("" + (Integer.parseInt(plike) + 1));
                        liekeref.child(postId).child(myuid).setValue("Liked");
                        mlike = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postComment() {
        progressDialog.setMessage("Adding Comment");

        final String commentss = comment.getText().toString().trim();
        if (TextUtils.isEmpty(commentss)) {
            Toast.makeText(Comments.this, "Empty comment", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.show();
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference datarf = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timestamp);
        hashMap.put("comment", commentss);
        hashMap.put("ptime", timestamp);
        hashMap.put("uid", myuid);
        hashMap.put("uemail", myemail);
        hashMap.put("udp", mydp);
        hashMap.put("uname", myname);
        datarf.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(Comments.this, "Added", Toast.LENGTH_LONG).show();
                comment.setText("");
                updatecommetcount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Comments.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean count = false;

    private void updatecommetcount() {
        count = true;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (count) {
                    String comments = "" + dataSnapshot.child("pcomments").getValue();
                    int newcomment = Integer.parseInt(comments) + 1;
                    reference.child("pcomments").setValue("" + newcomment);
                    count = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {

        Query myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.orderByChild("uid").equalTo(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    myname = dataSnapshot1.child("name").getValue().toString();
                    mydp = dataSnapshot1.child("image").getValue().toString();
                    try {
                        Glide.with(Comments.this).load(mydp).into(imagep);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        DatabaseReference query = databaseReference.child(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    //String tit = dataSnapshot1.child("title").getValue().toString();
                    String des= dataSnapshot1.child("description").getValue().toString();
                    String img = dataSnapshot1.child("image").getValue().toString();
                    String likes = dataSnapshot1.child("likes").getValue().toString();
                    if (dataSnapshot1.child("comments").exists())
                    {
                        String comments = dataSnapshot1.child("comments").getValue().toString();
                    }

                    title.setText(postId);
                    description.setText(des);
                    Glide.with(Comments.this)
                            .load(img)
                            .into(image);


                    String ptitle = dataSnapshot1.child("title").getValue().toString();
                    String descriptions = dataSnapshot1.child("description").getValue().toString();
                    uimage = dataSnapshot1.child("image").getValue().toString();
                    plike = dataSnapshot1.child("likes").getValue().toString();


                    // hisuid = dataSnapshot1.child("uid").getValue().toString();
                    //String uemail = dataSnapshot1.child("uemail").getValue().toString();
                    //hisname = dataSnapshot1.child("name").getValue().toString();
                    //ptime = dataSnapshot1.child("ptime").getValue().toString();
                    //String commentcount = dataSnapshot1.child("pcomments").getValue().toString();
                    //Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    //calendar.setTimeInMillis(Long.parseLong(ptime));
                    //String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();


                    name.setText(hisname);
                    title.setText(ptitle);
                    description.setText(descriptions);
                    like.setText(plike + " Likes");
                    //time.setText(timedate);
                    //tcomment.setText(commentcount + " Comments");
                    if (uimage.equals("noImage")) {
                        image.setVisibility(View.GONE);
                    } else {
                        image.setVisibility(View.VISIBLE);
                        try {
                            Glide.with(Comments.this).load(uimage).into(image);
                        } catch (Exception e) {

                        }
                    }
                    try {
                        Glide.with(Comments.this).load(hisdp).into(picture);
                    } catch (Exception e) {

                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }*/

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