package com.asem.newsapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

;public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyHolder> {
    Context context;
    List<ModelComment> list;
    DatabaseReference  databaseReference1;
    String email;

    public AdapterComment(Context context, List<ModelComment> list,String postid) {
        this.context = context;
        this.list = list;
        this.postid = postid;
    }
    String postid;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comment, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String name = list.get(position).getUname();
        email = list.get(position).getUemail();
        String image = list.get(position).getImage();
        final String cid = list.get(position).getcId();
        String comment = list.get(position).getComment();
        String timestamp = list.get(position).getPtime();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        email = email.replaceAll("@gmail.com" , "");

        holder.name.setText(name);
        holder.time.setText(timedate);
        holder.comment.setText(comment);
        try {
            Glide.with(context).load(image).into(holder.image);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, comment, time;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.loadcomment);
            name = itemView.findViewById(R.id.commentname);
            comment = itemView.findViewById(R.id.commenttext);
            time = itemView.findViewById(R.id.commenttime);
        }
    }
}
