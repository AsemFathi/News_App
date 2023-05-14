package com.asem.newsapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UserPage extends AppCompatActivity implements RecyclerInterface , NavigationView.OnNavigationItemSelectedListener {

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


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
       /* username = findViewById(R.id.uuser_name);
        photo = findViewById(R.id.user_photo);
        recyclerView = findViewById(R.id.userShowNews);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference=FirebaseDatabase.getInstance().getReference().child("Posts");
        email = auth.getCurrentUser().getEmail();
        email = email.replaceAll("@gmail.com" , "");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

       /* if (savedInstanceState == null )
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        // to make the Navigation drawer icon always appear on the action bar
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        //load name and photo

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(email).child("Full Name").getValue().toString();
                Log.i(TAG, "onDataChange: Email" + user);
                username.setText("Asem");

                String url = snapshot.child(email).child("image").getValue().toString();
                Glide.with(UserPage.this)
                        .load(url)
                        .into(photo);
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
                    String title = "", des = "", time ="", image = "", likes = "0", comments = "0";
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


                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(time));
                    String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                    posts.add(new Posts(timedate, title, des, image, likes, comments));
                }
                recyclerView.setAdapter(new NewsAdapter(UserPage.this, posts, UserPage.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        email = auth.getCurrentUser().getEmail();
        email = email.replaceAll("@gmail.com" , "");



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_menu);


        View headerView = navigationView.getHeaderView(0);
        photo = headerView.findViewById(R.id.userPhoto);
        username = headerView.findViewById(R.id.nav_user_name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             String userName = snapshot.child(email).child("Full Name").getValue().toString();
             String ima = snapshot.child(email).child("image").getValue().toString();

             Glide.with(UserPage.this)
                     .load(ima)
                     .into(photo);
             username.setText(userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.nav_open , R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new Home()).commit();
            navigationView.setCheckedItem(R.id.home);

        }



        

    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Home"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new Home()).commit();
        if (item.getTitle().equals("My Account"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new My_Account()).commit();
        if (item.getTitle().equals("Favourites"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new Favourite()).commit();
        if (item.getTitle().equals("Logout"))
        {
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserPage.this , Login.class);
            startActivity(intent);
        }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}