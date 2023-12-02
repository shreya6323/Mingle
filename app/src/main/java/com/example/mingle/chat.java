package com.example.mingle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mingle.Models.Messages;
import com.example.mingle.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import Adapters.Chatadapter;

public class chat extends AppCompatActivity {

    private Toolbar toolbar;
    ActivityChatBinding binding;



    FirebaseDatabase db;
   FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate((getLayoutInflater()));
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderid = auth.getUid();
        String receiverid = getIntent().getStringExtra("userid");
        String username = getIntent().getStringExtra("username");
        String profilepic = getIntent().getStringExtra("profilepic");

        binding.username.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.person_black).into(binding.profileImage);


        binding.arrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(chat.this, homepage.class);
               startActivity(intent);
           }
       });

        final ArrayList<Messages> mlist = new ArrayList<>();
        final Chatadapter chatadapter = new Chatadapter(mlist,this,receiverid);
        binding.chats.setAdapter(chatadapter);

        LinearLayoutManager  layoutManager = new LinearLayoutManager(this);
        binding.chats.setLayoutManager(layoutManager);

        final String senderRoom = senderid+receiverid;
        final String receiverRoom = receiverid+senderid;

         db.getReference().child("chats")
                         .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                mlist.clear();
                                 for(DataSnapshot snapshot1 : snapshot.getChildren())
                                 {
                                     Messages messages = snapshot1.getValue(Messages.class);
                                     messages.setMsgid(snapshot1.getKey());
                                     mlist.add(messages);
                                 }
                                 chatadapter.notifyDataSetChanged();
                                if (mlist.size() > 0) {
                                    binding.chats.smoothScrollToPosition(mlist.size() - 1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });





        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String sendermsg =  binding.msg.getText().toString();
            final Messages messages = new Messages(senderid,sendermsg);
            messages.setTimestamp(new Date().getTime());
            binding.msg.setText("");

            db.getReference().child("chats")
                    .child(senderRoom)
                    .push()
                    .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (!((senderid+receiverid).equals(receiverid+senderid))) {
                                db.getReference().child("chats").child(receiverRoom)
                                        .push()
                                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        }
                    });


            }
        });






    }




}