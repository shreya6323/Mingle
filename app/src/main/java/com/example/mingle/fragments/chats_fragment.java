package com.example.mingle.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mingle.Models.User;
import com.example.mingle.databinding.FragmentChatsFragmentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.UsersAdapter;

public class chats_fragment extends Fragment {

    public chats_fragment() {
    }
  FragmentChatsFragmentBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        binding = FragmentChatsFragmentBinding.inflate(inflater, container, false);
        UsersAdapter adapter = new UsersAdapter(list,getContext());
        binding.rc.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rc.setLayoutManager(layoutManager);
        db.getReference().child("CUSTOMER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    User u = ds.getValue(User.class);
                    u.setUserId(ds.getKey());
                    list.add(u);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }




}