package com.tafh.tugasakhir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tafh.tugasakhir.fragments.AkunFragment;
import com.tafh.tugasakhir.fragments.ChatsFragment;
import com.tafh.tugasakhir.fragments.HalloFragment;
import com.tafh.tugasakhir.fragments.HomeFragment;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.notifications.Token;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<ModelUser> dataUsers;
    private DatabaseReference databaseIuran, databaseUsers, databaseKeluarga;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String myUserId;
    private static final String TAG = "GET_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Intent intent = getIntent();
        myUserId = intent.getStringExtra("MY_USERID");

        auth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        //bottom navigation dan frame layoutnya
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(myUserId)).commit();

        checkUserStatus();
        //        update token
//        updateToken(FirebaseInstanceId.getInstance().getToken());
        //mengambil token pendaftaran terbaru
        getToken();

    }

    private void getAllUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            final String getEmailUser = user.getEmail();
            databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
            databaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataUsers = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Mapping data pada DataSnapshot ke dalam objek akun
                        ModelUser user = snapshot.getValue(ModelUser.class);
                        //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                        user.setUserid(snapshot.getKey());
                        dataUsers.add(user);
                    }
                    int n = dataUsers.size();
                    for (int i = 0; i < n; i++) {

                        String gmail = dataUsers.get(i).getGmail();

                        if (gmail.equals(getEmailUser)) {
                            //ambil nomor kk user
                            myUserId = dataUsers.get(i).getUserid();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(MainActivity.this, ""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            startActivity(new Intent(MainActivity.this, LoginGmailActivity.class));
            finish();
        }
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        updateToken(token);
                        //Log and Toast
                        String msg = token;

                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }


    public void updateToken(String token){

        Intent intent = getIntent();
        myUserId = intent.getStringExtra("MY_USERID");
        if (myUserId == null){
            getAllUser();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
//        Toast.makeText(MainActivity.this, "key : "+myUserId, Toast.LENGTH_SHORT).show();
        ref.child(myUserId).setValue(mToken);
    }

    private void checkUserStatus() {
        getAllUser();
        FirebaseUser user = auth.getCurrentUser();


        if (user != null) {

//        Toast.makeText(MainActivity.this, "key : "+myUserId, Toast.LENGTH_SHORT).show();
            //save uid of currently signed in user in shared preferences
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("myUserId", myUserId);
            editor.apply();
        }
        else {
            startActivity(new Intent(MainActivity.this, LoginGmailActivity.class));
            finish();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void signOut() {
        auth.signOut();
        Toast.makeText(MainActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        finish();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;



                    switch (item.getItemId()) {

                        case R.id.nav_hallo:
                            selectedFragment = new HalloFragment(myUserId);
                            break;

                        case R.id.nav_chats:
                            selectedFragment = new ChatsFragment(myUserId);
                            break;

                        case R.id.nav_home:
                            selectedFragment = new HomeFragment(myUserId);
                            break;

                        case R.id.nav_akun:
                            selectedFragment = new AkunFragment(myUserId);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
