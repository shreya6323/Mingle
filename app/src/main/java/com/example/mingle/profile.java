package com.example.mingle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mingle.Models.User;
import com.example.mingle.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class profile extends AppCompatActivity {
ActivityProfileBinding binding;
FirebaseStorage storage;
FirebaseAuth auth;
FirebaseDatabase db;
Uri file;
final int profile_code = 33;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();



        db.getReference().child("CUSTOMER").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User u = snapshot.getValue(User.class);
                                binding.name.setText(u.getUsername());
                              binding.about.setText(u.getAbout());

                                Picasso.get().load(u.getProfile()).placeholder(R.drawable.person_grey)
                                        .into(binding.profileImage);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, profile_code);
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file != null) {
                    final StorageReference reference = storage.getReference().child("profile pic")
                            .child(auth.getUid());
                    reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase.getInstance().getReference().child("CUSTOMER").child(FirebaseAuth.getInstance().getUid())
                                            .child("profile").setValue(uri.toString());


                                 /*   FirebaseDatabase.getInstance().getReference().child("CUSTOMER").child(FirebaseAuth.getInstance().getUid())
                                            .child("about").setValue(binding.about.getText());*/
                                   // Toast.makeText(profile.this, "Profile updated !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                String newUsername = binding.name.getText().toString();
                String newAbout = binding.about.getText().toString();
                HashMap<String, Object> obj = new HashMap<>();
                obj.put("username",newUsername);
                obj.put("about",newAbout);
               if(newUsername != null)
               {
                   FirebaseDatabase.getInstance().getReference().child("CUSTOMER")
                           .child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
               }

                Toast.makeText(profile.this, "Profile updated !", Toast.LENGTH_SHORT).show();


            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == profile_code)
        {
                  if(data.getData() != null)
                     {
                           file = data.getData();
                           binding.profileImage.setImageURI(file);
                     }

        }
        else {
            Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
        }
    }

    }



