package com.tafh.tugasakhir;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tafh.tugasakhir.fragments.AkunFragment;
import com.tafh.tugasakhir.fragments.AkunWargaFragment;
import com.tafh.tugasakhir.fragments.ChatsFragment;
import com.tafh.tugasakhir.fragments.ChatsWargaFragment;
import com.tafh.tugasakhir.fragments.HalloFragment;
import com.tafh.tugasakhir.fragments.HalloWargaFragment;
import com.tafh.tugasakhir.fragments.HomeFragment;
import com.tafh.tugasakhir.fragments.HomeWargaFragment;
import com.tafh.tugasakhir.iuran.DetailIuranBulananWargaActivity;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaWargaActivity;
import com.tafh.tugasakhir.keluarga.TambahDataKeluargaActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.notifications.Token;

import java.util.ArrayList;

public class MainWargaActivity extends AppCompatActivity {
//    private FirebaseAuth auth;
//    private FirebaseAuth.AuthStateListener authListener;
//    private Button myButton, btnScanQrCode;
    private ArrayList<ModelUser> dataUsers;
    private DatabaseReference databaseIuran, databaseUsers, databaseKeluarga;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String myUserId;
    private static final String TAG = "GET_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_warga);
        Intent intent = getIntent();
        myUserId = intent.getStringExtra("MY_USERID");


        auth = FirebaseAuth.getInstance();
        //bottom navigation dan frame layoutnya
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeWargaFragment(myUserId)).commit();


        checkUserStatus();
//        //update token
        getToken();

        //bottom navigation dan frame layoutnya
//        auth = FirebaseAuth.getInstance();
//
//        btnScanQrCode = findViewById(R.id.btn_scan_qrcode);
//        btnScanQrCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                IntentIntegrator integrator = new IntentIntegrator(MainWargaActivity.this);
//
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setOrientationLocked(false);
//                integrator.setPrompt("Scanning");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
//            }
//        });
//
//
//        myButton = findViewById(R.id.btn_signout);
//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//
//                Intent intent = new Intent(MainWargaActivity.this, WelcomeActivity.class);
//                startActivity(intent);
//
//            }
//        });
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

                    Toast.makeText(MainWargaActivity.this, ""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            startActivity(new Intent(MainWargaActivity.this, LoginGmailActivity.class));
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
//        Log.w("NILAI", myUserId);
        ref.child(myUserId).setValue(mToken);
    }

    private void checkUserStatus() {
        getAllUser();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
//            Log.w("Nilai",""+myUserId );
            //save uid of currently signed in user in shared preferences
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("myUserId", myUserId);
            editor.apply();
        }
        else {
            startActivity(new Intent(MainWargaActivity.this, LoginGmailActivity.class));
            finish();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            if (result.getContents().equals("RT15")){
                Intent intent = new Intent(MainWargaActivity.this, TambahDataKeluargaActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainWargaActivity.this, "Maaf Kode Tidak Cocok", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainWargaActivity.this, MainWargaActivity.class);
                startActivity(intent);
            }


//            new AlertDialog.Builder(MainWargaActivity.this)
//                    .setTitle("Scan Result")
//                    //isi pesan
//                    .setMessage(result.getContents())
//
//                    .setPositiveButton("copy", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                            ClipData data = ClipData.newPlainText("result", result.getContents());
//                            manager.setPrimaryClip(data);
//
//                        }
//                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    dialogInterface.dismiss();
//                }
//            }).create().show();


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void signOut() {
        auth.signOut();
        Toast.makeText(MainWargaActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        finish();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.nav_hallo:
                            selectedFragment = new HalloWargaFragment(myUserId);
                            break;

                        case R.id.nav_chats:
                            selectedFragment = new ChatsWargaFragment(myUserId);
                            break;

                        case R.id.nav_home:
                            selectedFragment = new HomeWargaFragment(myUserId);
                            break;

                        case R.id.nav_akun:
                            selectedFragment = new AkunWargaFragment(myUserId);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
