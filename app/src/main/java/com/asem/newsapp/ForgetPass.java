package com.asem.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {
    EditText Email;
    FirebaseAuth auth;
    Button Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        Email =findViewById(R.id.editTextemail);
        Submit = findViewById(R.id.submit_btn);
        auth = FirebaseAuth.getInstance();
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgetPass.this,"Email sent" ,Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ForgetPass.this , Login.class);
                            startActivity(intent);

                        }
                        else
                            Toast.makeText(ForgetPass.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}