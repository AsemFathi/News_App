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
import android.widget.ImageButton;
import android.widget.Toast;

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
public class Favourite extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    DatabaseReference reference;
    String email;
    ArrayList<String> postsTitle = new ArrayList<>();
    ArrayList<Posts> posts = new ArrayList<>();
    NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.userShowFavourites);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       // Create an adapter with the posts ArrayList
        adapter = new NewsAdapter(getContext(), posts, (RecyclerInterface) getContext());
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        email = auth.getCurrentUser().getEmail();
        email = email.replaceAll("@gmail.com", "");
        posts = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites").child(email);
        reference = FirebaseDatabase.getInstance().getReference().child("Posts");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsTitle.clear(); // Clear the list before adding new titles

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String title = dataSnapshot.getKey();
                    postsTitle.add(title);
                    Log.i(TAG, "onDataChange: " + title);
                }

                // Fetch the favorite posts based on the titles
                for (String postTitle : postsTitle) {
                    reference.child(postTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Retrieve post data
                            String title = snapshot.child("title").getValue(String.class);
                            String description = snapshot.child("description").getValue(String.class);
                            String time = snapshot.child("time").getValue(String.class);
                            String image = snapshot.child("image").getValue(String.class);
                            String likes = snapshot.child("likes").getValue(String.class);
                            String comments = snapshot.child("comment").getValue(String.class);
                            String author = snapshot.child("author").getValue(String.class);
                            String authorImage = snapshot.child("authorImage").getValue(String.class);

                            // Create a new Posts object with the retrieved data
                            Posts post = new Posts(time, title, description, image, likes, comments, author, authorImage);
                            posts.add(post); // Add the post to the ArrayList
                            //adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                            Log.i(TAG, "onDataChange: " + post.getDesc());
                            recyclerView.setAdapter(new NewsAdapter(getContext(), posts, (RecyclerInterface) getContext()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error if retrieval is unsuccessful
                        }
                    });

                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if retrieval is unsuccessful
            }
        });

        return view;
    }
}
