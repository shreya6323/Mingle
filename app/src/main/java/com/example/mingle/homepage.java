package com.example.mingle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import Adapters.Tabadapter;

public class homepage extends AppCompatActivity {





   FirebaseAuth auth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        ActionBar actionbar = getSupportActionBar();
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        Tabadapter t = new Tabadapter(this);
        ViewPager2 viewpager = findViewById(R.id.viewpager);
       viewpager.setAdapter(t);
       tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewpager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });

       viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);
               tablayout.getTabAt(position).select();
           }
       });


      auth = FirebaseAuth.getInstance();
     }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.settings) {
            Intent intent = new Intent(homepage.this, profile.class
            );
            startActivity(intent);
        } else if (itemId == R.id.new_group) {
            Intent intent = new Intent(homepage.this, grp_chat.class
            );
            startActivity(intent);

        }
        else if (itemId == R.id.logout) {
        auth.signOut();
        Intent intent = new Intent(homepage.this, Login.class);
        startActivity(intent);
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}