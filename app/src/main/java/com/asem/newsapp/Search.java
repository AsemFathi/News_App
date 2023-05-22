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
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Search extends Fragment {

    EditText search;
    ImageButton searchButton;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    String edit;

    ArrayList<Posts> posts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search = view.findViewById(R.id.searchText);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reference = FirebaseDatabase.getInstance().getReference().child("Posts");


        //get the user text and search by it in the posts title any post contain this text show it
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        posts.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            edit = search.getText().toString();
                            String searchInput = dataSnapshot.getKey().toString();
                            Log.i(TAG, "onDataChange: " + searchInput +" "+ search.getText().toString());
                            if(searchInput.toLowerCase().contains(search.getText().toString().toLowerCase()))
                            {
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


                                //convert time from millis to dd/mm/yyyy hh:mm:aa
                                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                                calendar.setTimeInMillis(Long.parseLong(time));
                                String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                                posts.add(new Posts(timedate, title, des, image, likes, comments,author , authorImage));
                            }

                        }
                        recyclerView.setAdapter(new NewsAdapter(getContext(), posts, (RecyclerInterface) getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //to search automatically when the user enter any text without press in the button
        if (!search.getText().toString().isEmpty())
        {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        if(dataSnapshot.getKey().contains(edit))
                        {
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        return view;
    }
}