package com.example.mingle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mingle.Models.User;
import com.example.mingle.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    ProgressDialog pg;

    GoogleSignInClient gsc;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        pg = new ProgressDialog(Login.this);
        auth = FirebaseAuth.getInstance();
        pg.setTitle("Signing in...");
        pg.setMessage("Hang tight! We're securely logging you in.");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);


        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        binding.signIn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                if(binding.email.getText().toString().isEmpty())
                {
                  binding.email.setError("Please enter your Email !");
                  return ;
                }

                if(binding.password.getText().toString().isEmpty())
                {
                    binding.password.setError("Please enter your Password !");
                    return ;
                }
                pg.show();
                auth.signInWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pg.dismiss();
                                if(task.isSuccessful())
                                {
                                    Intent intent  = new Intent(Login.this,homepage.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        binding.newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,sign_up.class);
                startActivity(intent);
            }
        });
        if(auth.getCurrentUser() != null)
        {
            Intent intent = new Intent(Login.this,homepage.class);
            startActivity(intent);
        }



    }
    void signIn()
    {
        Intent i = gsc.getSignInIntent();
        startActivityForResult(i,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account =  task.getResult(ApiException.class);
                Log.d("Tag","firebase auth with google"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Log.d("Tag","firebase auth with google failed");
                Toast.makeText(this, "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("Tag","signed in with credential: success");
                            FirebaseUser user = auth.getCurrentUser();
                            User u = new User();
                            u.setUserId(user.getUid());
                            u.setMail(user.getEmail());
                            u.setUsername(user.getDisplayName());
                            u.setProfile(user.getPhotoUrl().toString());
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            // Store user data in Firebase Realtime Database
                            db.getReference().child("CUSTOMER").child(user.getUid()).setValue(u)
                                    .addOnSuccessListener(aVoid -> {
                                        // On successful data storage, navigate to the homepage
                                        Intent intent = new Intent(Login.this, homepage.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(Login.this, "Failed to store user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                        }
                        else {
                            Log.w("Tag","sign in with credential :failed", task.getException());

                        }
                    }
                });
    }


}