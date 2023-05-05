package com.asem.newsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsHolder extends RecyclerView.ViewHolder {
    TextView Date,title,desc;
    ImageView imageView;
    public NewsHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
        super(itemView);
        Date = itemView.findViewById(R.id.date);
        title = itemView.findViewById(R.id.title_recycler);
        desc = itemView.findViewById(R.id.desc_recycler);
        imageView = itemView.findViewById(R.id.new_photo);
    }
}
