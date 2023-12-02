package com.example.mingle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mingle.Models.Messages;
import com.example.mingle.databinding.ActivityGrpChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import Adapters.Chatadapter;

public class grp_chat extends AppCompatActivity {

    ActivityGrpChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrpChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(grp_chat.this, homepage.class);
                startActivity(intent);
            }
        });
        final String senderId = FirebaseAuth.getInstance().getUid();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final ArrayList<Messages> list = new ArrayList<>();
        final Chatadapter chatadapter = new Chatadapter(list, this);
        binding.chats.setAdapter(chatadapter);

        db.getReference().child("Group chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                 Messages model = ds.getValue(Messages.class);
                 list.add(model);
                }
                chatadapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    binding.chats.smoothScrollToPosition(list.size() - 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chats.setLayoutManager(layoutManager);

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg = binding.msg.getText().toString();
                final Messages model = new Messages(senderId,msg);
                model.setTimestamp(new Date().getTime());
                binding.msg.setText("");
                db.getReference().child("Group chat").push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });
    }
}