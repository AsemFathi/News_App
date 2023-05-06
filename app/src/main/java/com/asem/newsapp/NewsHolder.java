package com.asem.newsapp;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    TextView Date,title,desc;
    ImageView imageView;
    ImageButton imageButton;
    DatabaseReference databaseReference;
    public NewsHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
        super(itemView);
        Date = itemView.findViewById(R.id.date);
        title = itemView.findViewById(R.id.title_recycler);
        desc = itemView.findViewById(R.id.desc_recycler);
        imageView = itemView.findViewById(R.id.new_photo);
        imageButton = itemView.findViewById(R.id.popupMenuBtn);
        imageButton.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");

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
        } else if (item.getTitle().equals("Delete")) {
            databaseReference.child(title.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(imageButton.getContext(), "Done Deleting", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return false;
    }
}
