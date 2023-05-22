package com.asem.newsapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    TextView Date,
            title,desc , likes;
    ImageView imageView;
    ImageButton imageButton , fav;
    DatabaseReference databaseReference;

    Button like , comment;
    FirebaseAuth auth;
    FirebaseUser user;
    boolean found;
    CircleImageView postsAuthorPhoto;
    TextView postsAuthorName, postsPostTime , postsPostTitle , postsPostDescription,
    postsPostLikes , postsPostComments;
    Button LikeBTN, CommentBtn;
    ImageView postsPostImage;
    ImageButton postsMoreButton;
    private DatabaseReference favoritesRef; // Reference to the "favorites" node in the database
    private FirebaseAuth firebaseAuth; // Firebase Authentication instance


    boolean likeClicked = true;
    public NewsHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
        super(itemView);
       /* Date = itemView.findViewById(R.id.user_date);
        title = itemView.findViewById(R.id.user_title_recycler);
        desc = itemView.findViewById(R.id.user_desc_recycler);
        imageView = itemView.findViewById(R.id.user_new_photo);
        imageButton = itemView.findViewById(R.id.user_popupMenuBtn);
        fav = itemView.findViewById(R.id.favorite);
*/
        postsAuthorPhoto = itemView.findViewById(R.id.posts_authorPhoto);
        postsAuthorName = itemView.findViewById(R.id.post_autharName);
        postsPostTime = itemView.findViewById(R.id.posts_postTime);
        postsPostTitle= itemView.findViewById(R.id.posts_postTitle);
        postsPostDescription = itemView.findViewById(R.id.post_postDescription);
        postsPostLikes = itemView.findViewById(R.id.posts_postLikes);
        postsPostComments = itemView.findViewById(R.id.posts_postComments);
        postsPostImage = itemView.findViewById(R.id.posts_postImage);
        LikeBTN = itemView.findViewById(R.id.posts_likeButton);
        CommentBtn = itemView.findViewById(R.id.posts_commentButton);
        imageButton = itemView.findViewById(R.id.user_popupMenuBtn);
        fav = itemView.findViewById(R.id.favorite);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        email = email.replaceAll("@gmail.com" , "");
        favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites")
                .child(email); // Assuming you have authenticated the user


       imageButton.setOnClickListener(this);
       /* like = itemView.findViewById(R.id.like);
        comment = itemView.findViewById(R.id.comment);
        likes = itemView.findViewById(R.id.likes);
*/
        LikeBTN.setEnabled(likeClicked);
        LikeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClicked = false;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(postsPostTitle.getText().toString()).child("likes").exists())
                        {
                            int likes =Integer.parseInt(snapshot.child(postsPostTitle.getText().toString()).child("likes").getValue().toString());
                            likes++;
                            databaseReference.child(postsPostTitle.getText().toString()).child("likes").setValue(String.valueOf(likes));
                        }
                        else
                        {
                            databaseReference.child(postsPostTitle.getText().toString()).child("likes").setValue("1");
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        LikeBTN.setEnabled(true);
                    }
                });

            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = postsPostTitle.getText().toString();
                isPostFavorite(postID);
                // Check if the post is already a favorite
                if (found){
                    // Remove it from favorites
                    removeFromFavorites(postID);
                    fav.setImageResource(R.drawable.baseline_favorite_border_24);
                } else {
                    // Add it to favorites
                    addToFavorites(postID);
                    fav.setImageResource(R.drawable.baseline_favorite_24);
                }
            }
        });

        CommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postsPostTitle.getContext() , Comments.class);
                intent.putExtra("pid" , postsPostTitle.getText().toString());
                postsPostTitle.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        showPopupMenu(v);
    }

    private void showPopupMenu(View view)
    {
        PopupMenu popupMenu = new PopupMenu(view.getContext() , view);
        popupMenu.inflate(R.menu.menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.d(TAG, "onMenuItemClick: You clicked in : " + item.getTitle());
        if (item.getTitle().equals("Edit"))
        {
            //Navigate to Edit screen
            Intent intent = new Intent(postsAuthorName.getContext() , EditActivity.class);
            intent.putExtra("title" , postsPostTitle.getText().toString());
            postsPostTitle.getContext().startActivity(intent);
        } else if (item.getTitle().equals("Delete")) {
            databaseReference.child(postsPostTitle.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(imageButton.getContext(), "Done Deleting", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return false;
    }
    private void isPostFavorite(String postID) {
        favoritesRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                found = snapshot.exists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if retrieval is unsuccessful
            }
        });
    }


    // Add a post to favorites for the current user
    private void addToFavorites(String postID) {
        // Add the postID under the current user's favorites node in the database
        // For example:
        favoritesRef.child(postID).setValue(true);
    }

    private void removeFromFavorites(String postID) {

        favoritesRef.child(postID).removeValue();
    }
}
