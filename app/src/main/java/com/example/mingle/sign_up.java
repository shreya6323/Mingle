package com.example.mingle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mingle.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

FirebaseDatabase db;
TextView t ;
private FirebaseAuth auth;
ProgressDialog pg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        t = findViewById(R.id.already_have_acc);
        getSupportActionBar().hide();
        Button b = (Button)findViewById(R.id.sign_up);
        EditText name,pass,mail;
        name = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        mail = (EditText) findViewById(R.id.email);

       auth = FirebaseAuth.getInstance();
       db = FirebaseDatabase.getInstance();
       pg = new ProgressDialog(sign_up.this);
       pg.setTitle("Creating Account");
       pg.setMessage("Hang tight! We're setting things up just for you.");

       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pg.show();
              auth.createUserWithEmailAndPassword(mail.getText().toString(),pass.getText().toString())
                      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              pg.dismiss();
                              if(task.isSuccessful())
                              {
                                  User u = new User(name.getText().toString(),mail.getText().toString(),pass.getText().toString());
                                  String id = task.getResult().getUser().getUid();

                                  db.getReference().child("CUSTOMER").child(id).setValue(u);

                                  Toast.makeText(sign_up.this, "You are successfully signed up !", Toast.LENGTH_SHORT).show();
                                  Intent intent = new Intent(sign_up.this,homepage.class);
                                  startActivity(intent);
                              }
                              else {
                                  Toast.makeText(sign_up.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              }

                          }
                      });

           }
       });

        t.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up.this,Login.class);
                startActivity(intent);
            }
        });



    }
}