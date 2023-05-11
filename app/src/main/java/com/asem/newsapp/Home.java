package com.asem.newsapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Home extends Fragment implements RecyclerInterface{
    TextView username;
    ImageView photo;
    RecyclerView recyclerView;

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    DatabaseReference reference;
    String user;
    String email;


    StorageReference storageReference;

    ArrayList<Posts> posts = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.userShowNews);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference=FirebaseDatabase.getInstance().getReference().child("Posts");
        email = auth.getCurrentUser().getEmail();
        email = email.replaceAll("@gmail.com" , "");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //load name and photo

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(email).child("Full Name").getValue().toString();
                Log.i(TAG, "onDataChange: Email" + user);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String title = "", des = "", time ="", image = "", likes = "0", comments = "0",author ="" , authorImage = "";
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

                    posts.add(new Posts(timedate, title, des, image, likes, comments,author , authorImage));
                }
                recyclerView.setAdapter(new NewsAdapter(getContext(), posts, (RecyclerInterface) getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }

    @Override
    public void onItemClick(int pos) {

    }
}