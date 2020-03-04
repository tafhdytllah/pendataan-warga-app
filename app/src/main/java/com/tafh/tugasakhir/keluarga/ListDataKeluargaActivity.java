package com.tafh.tugasakhir.keluarga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.AkunAdapter;
import com.tafh.tugasakhir.adapter.SectionsPagerAdapter;
import com.tafh.tugasakhir.akun.ListDataAkunActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class ListDataKeluargaActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private ViewPager viewPager;
    private TabLayout tabs;

    private SectionsPagerAdapter sectionsPagerAdapter;

    private ActionBar actionBar;

    private String myUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_keluarga);

        getDataIntent();
        
        actionBar = getSupportActionBar();
        actionBar.setTitle("Data Keluarga");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(ListDataKeluargaActivity.this, getSupportFragmentManager(), myUserId);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }



    private void getDataIntent() {
        Intent intent = getIntent();
        myUserId = intent.getStringExtra("MY_USERID");
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null ){

        }
        else {
            startActivity(new Intent(this, LoginGmailActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
