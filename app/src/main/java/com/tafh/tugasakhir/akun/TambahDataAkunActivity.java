package com.tafh.tugasakhir.akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TambahDataAkunActivity extends AppCompatActivity implements View.OnClickListener {
    private String noRumahAktif;

    private EditText noRumah, namaKepalaKeluarga, noKK, noHP, gmail, password, userType;
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;
    private String userId;
    private DatabaseReference reference, drChats;
    private ProgressDialog pd;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ActionBar actionBar;
    private Context context = TambahDataAkunActivity.this;

    private ArrayList<ModelUser> userList;
    private String noRumahTerisi;
    private ArrayList<ModelKeluarga> dataKeluarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_akun);

        mAuth1 = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://tugas-akhir-6dbe1.firebaseio.com/")
                .setApiKey("AIzaSyB2_m26hKHrwCWgniyenBivp-LKtdQfINM")
                .setApplicationId("tugas-akhir-6dbe1").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }

        actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah User");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        noRumah = findViewById(R.id.et_no_rumah);
        noKK = findViewById(R.id.et_no_kk);
        namaKepalaKeluarga = findViewById(R.id.et_nama);
        noHP = findViewById(R.id.et_nohp);
        gmail = findViewById(R.id.et_gmail);
        password = findViewById(R.id.et_password);
        userType = findViewById(R.id.et_user_type);

        pd = new ProgressDialog(this);
//
//        //assign database instances
//        mAuth1 = FirebaseAuth.getInstance();
//        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user != null) {
//                }
//                else{
//                    startActivity(new Intent(TambahDataAkunActivity.this, LoginGmailActivity.class));
//                }
//            }
//        };
        findViewById(R.id.btn_simpan).setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_simpan:
                //tambah data user kemudian di registerkan gmail nya.


                addUser();
                break;
//
//                String getUserID = mAuth.getCurrentUser().getUid();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference getReference;
//                getReference = database.getReference();
//
//                else {
//
//                    getReference.child("Admin")
//                            .child(getUserID)
//                            .child("Akun")
//                            .push()
//                            .setValue(new ModelUser(getNoRumah, getNoKK, getNamaKepalaKeluarga, getNoHP, getGmail, getPassword, getLevel))
//                            .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    noRumah.setText("");
//                                    noKK.setText("");
//                                    namaKepalaKeluarga.setText("");
//                                    noHP.setText("");
//                                    gmail.setText("");
//                                    password.setText("");
//                                    Toast.makeText(TambahDataAkunActivity.this, "Data Berhasil Tersimpan", Toast.LENGTH_SHORT).show();
//
//                                    Intent intent = new Intent(TambahDataAkunActivity.this, ListDataAkunActivity.class);
//
//                                    startActivity(intent);
//                                }
//                            });
//                }
        }
    }

    private void addUser() {

        final String getNoRumah = noRumah.getText().toString().trim();

        final String getNoKK = noKK.getText().toString().trim();
        final String getNamaKepalaKeluarga = namaKepalaKeluarga.getText().toString().trim();
        final String getNoHP = noHP.getText().toString().trim();
        final String getGmail = gmail.getText().toString().trim();
        final String getPassword = password.getText().toString().trim();
        final String getImg = "noImage";
        final String getSearch = "noSearch";
        final String getCover = "noCover";

        userType.setText("warga");
        final String getUserType = userType.getText().toString().trim();

        if (getNoRumah.isEmpty()) {
            noRumah.setError("No Rumah tidak boleh kosong!");
            noRumah.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(getNoKK)) {
            noKK.setError("No KK tidak boleh kosong!");
            noKK.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getNamaKepalaKeluarga)) {
            namaKepalaKeluarga.setError("Nama tidak boleh kosong!");
            namaKepalaKeluarga.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getNoHP)) {
            noHP.setError("No Hp tidak boleh kosong!");
            noHP.requestFocus();
            return;
        }
        if (getNoHP.length() <= 10){
            noHP.setError("Masukkan Nomor Hp yang sesuai!");
            noHP.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(getGmail)) {
            gmail.setError("Email tidak boleh kosong!");
            gmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(getGmail).matches()) {
            gmail.setError("Masukan email yang sesuai!");
            gmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getPassword)) {
            password.setError("Password tidak boleh kosong!");
            password.requestFocus();
            return;

        }
        if (getPassword.length() < 6) {
            password.setError(getString(R.string.minimum_password));
            password.requestFocus();
            return;
        }
        if (getNoKK.length() != 16) {
            noKK.setError("Nomor KK Harus 16 digit");
            noKK.requestFocus();
            Toast.makeText(this, "Nomor KK Harus 16 digit", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            getallNoRumah(getNoRumah, getNoKK, getNamaKepalaKeluarga, getNoHP, getGmail, getPassword, getImg, getSearch, getCover, getUserType);
        }


    }

    private void tambahAkun(final String getNoRumah, final String getNoKK, final String getNamaKepalaKeluarga,
                            final String getNoHP, final String getGmail, final String getPassword,
                            final String getImg, final String getSearch, final String getCover, final String getUserType) {
        pd.setMessage("Tunggu sebentar...");
        pd.show();
        //referen untuk tabel Users

        mAuth2.createUserWithEmailAndPassword(getGmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mAuth2.getCurrentUser()
                                    .sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //email verifikasi sudah di kirim
                                            if (task.isSuccessful()) {
                                                reference = FirebaseDatabase.getInstance().getReference("Users");
                                                userId = reference.push().getKey();
                                                //String id = databaseUsers.push().getKey();
                                                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                //
                                                //DatabaseReference reference = database.getReference("Users");
                                                //progressBar.setVisibility(View.INVISIBLE);
                                                //Toast.makeText(TambahDataAkunActivity.this, "Berhasil menambah User",Toast.LENGTH_LONG).show();

                                                //we will store the additional fields in firebase database
                                                HashMap<Object, String> hashMap = new HashMap<>();
                                                hashMap.put("userId", userId);
                                                hashMap.put("userType", getUserType);
                                                hashMap.put("noRumah", getNoRumah);
                                                hashMap.put("noKK", getNoKK);
                                                hashMap.put("namaKepalaKeluarga", getNamaKepalaKeluarga);
                                                hashMap.put("noHp", getNoHP);
                                                hashMap.put("gmail", getGmail);
                                                hashMap.put("password", getPassword);
                                                hashMap.put("img", getImg);
                                                hashMap.put("search", getSearch);
                                                hashMap.put("cover", getCover);
                                                hashMap.put("onlineStatus", "online");

                                                reference.child(userId)
                                                        .setValue(hashMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                pd.dismiss();

                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(TambahDataAkunActivity.this, "Berhasil menambah User, cek Email untuk Verifikasi!",Toast.LENGTH_LONG).show();

                                                                    noRumah.setText("");
                                                                    noKK.setText("");
                                                                    namaKepalaKeluarga.setText("");
                                                                    noHP.setText("");
                                                                    gmail.setText("");
                                                                    password.setText("");

                                                                    mAuth2.signOut();

//                                                                    startActivity(new Intent(context, ListDataAkunActivity.class));
                                                                    finish();
                                                                } else {
                                                                    pd.dismiss();
                                                                    //display failure message
                                                                    Toast.makeText(TambahDataAkunActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            }
                                            else {
                                                pd.dismiss();
                                                Toast.makeText(TambahDataAkunActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });


                        }
                        else {
                            pd.dismiss();
                            Log.w("NILAI", "email sudah terdaftar");
                            gmail.setError("Email ini sudah terdaftar!");
                            gmail.requestFocus();
                        }
                    }
                });
    }


    private void getallNoRumah(final String getNoRumah, final String getNoKK, final String getNamaKepalaKeluarga,
                               final String getNoHP, final String getGmail, final String getPassword,
                               final String getImg, final String getSearch, final String getCover, final String getUserType) {
        userList = new ArrayList<>();
        userList.clear();

        Log.w("Nilai", "No Rumah Tabel par: " + getNoRumah);

        final List<String> noRumahList;
        noRumahList = new ArrayList<>();
        noRumahList.clear();

        final DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("Users");
        Query query = dbUsers.orderByChild("noRumah");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelUser user = ds.getValue(ModelUser.class);
                    userList.add(user);
                }

                int n = userList.size();
                String no = "";
                for(int i=0;i<n;i++)
                {

                    final String noRUmah = userList.get(i).getNoRumah();

                    Log.w("Nilai", "No Rumah Tabel 1: " + noRUmah);
                    Log.w("Nilai", "No Rumah Tabel 2: " + getNoRumah);
                    if (noRUmah.equals(getNoRumah)) {
                        no = userList.get(i).getNoRumah();

                        Log.w("Nilai", "No 1 : " + no);
                        noRumah.setError("No Rumah Sudah digunakan!");
                        noRumah.requestFocus();
                        return;
                    }

                }
                if (no == ""){

                    Log.w("Nilai", "No 2 : " + no);
                    tambahAkun(getNoRumah, getNoKK, getNamaKepalaKeluarga, getNoHP, getGmail, getPassword, getImg, getSearch, getCover, getUserType);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth1.getCurrentUser();
        if (user != null) {

            //save uid of currently signed in user in shared preferences
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", userId);
            editor.apply();
        }
        else {
            startActivity(new Intent(TambahDataAkunActivity.this, LoginGmailActivity.class));
            finish();
        }
    }
}
