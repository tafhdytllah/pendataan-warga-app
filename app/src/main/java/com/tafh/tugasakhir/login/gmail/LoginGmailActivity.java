package com.tafh.tugasakhir.login.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.ProgressDialogHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.MainWargaActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.profile.SettingProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginGmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etGmail, etPassword;
    private TextInputLayout etGmailLayout,etPasswordLayout;
    private TextView tv1;
    private Button btnLogin;
    private TextView tvLupaPassword;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    private ArrayList<ModelUser> dataUser;
    private Context context = LoginGmailActivity.this;


//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        layoutVISIBLE();
//
//    }
//    private void layoutINVISIBLE() {
//
//        progressBar.setVisibility(View.VISIBLE);
//        etGmailLayout.setVisibility(View.INVISIBLE);
//        etPasswordLayout.setVisibility(View.INVISIBLE);
//        btnLogin.setVisibility(View.INVISIBLE);
//        tv1.setVisibility(View.INVISIBLE);
//    }
//
//    private void layoutVISIBLE(){
//
//        progressBar.setVisibility(View.INVISIBLE);
//        etGmailLayout.setVisibility(View.VISIBLE);
//        etPasswordLayout.setVisibility(View.VISIBLE);
//        btnLogin.setVisibility(View.VISIBLE);
//        tv1.setVisibility(View.VISIBLE);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_gmail);
        //get firebase auth intance
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        etGmailLayout = findViewById(R.id.etGmailLayout);
        etPasswordLayout = findViewById(R.id.etPasswordLayout);
        tv1 = findViewById(R.id.tv1);
        etGmail = findViewById(R.id.et_gmail);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvLupaPassword = findViewById(R.id.tv_lupa_password);

//        layoutINVISIBLE();
        tvLupaPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                authLogin();
                break;
            case R.id.tv_lupa_password:
                startActivity(new Intent(context, LupaPasswordActivity.class));
//                Toast.makeText(context, "Silahkan hubungi ketua RT, jika anda lupa password untuk Login",Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void authLogin() {
        pd.setMessage("Tunggu sebentar...");
        pd.show();

        Log.w("NILAI", "LOGIN CLICKED.");

        final String cekGmail = etGmail.getText().toString();
        final String cekPassword = etPassword.getText().toString();

        if (TextUtils.isEmpty(cekGmail)) {
            pd.dismiss();
            etGmail.setError("Masukan alamat email!");
            etGmail.requestFocus();
            Toast.makeText(getApplicationContext(), "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cekPassword)) {
            pd.dismiss();
            etPassword.setError("Masukan password!");
            etPassword.requestFocus();
            Toast.makeText(getApplicationContext(), "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        //authenticate user
        auth.signInWithEmailAndPassword(cekGmail, cekPassword)
                .addOnCompleteListener(LoginGmailActivity.this
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                //cek verifikasi email
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser().isEmailVerified()){
                                        Log.w("NILAI", "LOGIN TERFIRIFKASI.");
                                        getDataUser(cekGmail, cekPassword);
                                    }
                                    else {
                                        pd.dismiss();

                                        Log.w("NILAI", "BELUM TERVERIFIKASI.");
                                        Toast.makeText(LoginGmailActivity.this, "Email belum terverifikasi", Toast.LENGTH_LONG).show();
                                        auth.signOut();
                                    }

                                }
                                else {

                                    //error
                                    pd.dismiss();
                                    if (cekPassword.length() < 6) {
                                        etPassword.setError(getString(R.string.minimum_password));
                                    }
                                    if (cekPassword.length() >=6 ){
                                        Toast.makeText(LoginGmailActivity.this, getString(R.string.logingmail_failed), Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(LoginGmailActivity.this, "Periksa Koneksi Internet", Toast.LENGTH_LONG).show();

                                    }

                                }
                            }
                        });
    }

    private void getDataUser(final String cekGmail, final String cekPassword) {
        pd.setMessage("Memuat...");
        pd.show();


        Log.w("NILAI", "data user");

        final DatabaseReference databaseUsers;
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                checkUserStatus();
                Log.w("NILAI", "ondatachange");
                dataUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.w("NILAI", "snapshot.");
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    user.setUserid(snapshot.getKey());
                    dataUser.add(user);
                }


                int n = dataUser.size();
                String mUserType = "";
                String mUserId = "";
                String mGmail = "";
                String mPassword = "";
                String mImg = "";
                String mNoRUmah = "";
                String mNoKK = "";
                String mNamaKK = "";
                String mNoHp = "";
                String mOnlineStatus = "";
                for (int i=0; i<n; i++) {

                    Log.w("NILAI", "for.");
                    String gmail = dataUser.get(i).getGmail();

                    if (gmail.equals(cekGmail)){

                        Log.w("NILAI", "data.");
                        mUserType = dataUser.get(i).getUserType();
                        mUserId = dataUser.get(i).getUserid();
                        mGmail = dataUser.get(i).getGmail();
                        mPassword = dataUser.get(i).getPassword(); //password pada tabel user database
                        mImg = dataUser.get(i).getImg();

                        mNoRUmah = dataUser.get(i).getNoRumah();
                        mNoKK = dataUser.get(i).getNoKK();
                        mNamaKK = dataUser.get(i).getNamaKepalaKeluarga();
                        mNoHp = dataUser.get(i).getNoHp();
                        mOnlineStatus = dataUser.get(i).getOnlineStatus();


                        i=n;
                    }
                }

                Log.w("Nilai", "for selesai "+mPassword+" "+cekPassword);

                login(cekGmail, cekPassword, mUserType, mUserId, mGmail, mPassword, mImg, mNoRUmah, mNoKK, mNamaKK, mNoHp, mOnlineStatus);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();

                Toast.makeText(LoginGmailActivity.this,"error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void login(String cekGmail, String cekPassword, String mUserType, String mUserId, String mGmail,
                       String mPassword, String mImg, String mNoRUmah, String mNoKK, String mNamaKK, String mNoHp, String mOnlineStatus) {

        Log.w("Nilai", "fungsi login "+mPassword+" "+cekPassword);
//              mGmail = gmail pada tabel
//              mPassword = password pada tabel
//              cek password = password inputan
        if (mPassword.equals(cekPassword)) {
            Log.w("NILAI", "LOGIN JIKA PW SAMA : "+mPassword+" "+cekPassword);
            Log.w("NILAI", "Langsung cek user type");
            cekUserType(mUserType, mUserId);

        }
        else {
            FirebaseAuth auth;
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            if (user != null ){

                Log.w("NILAI", "LOGIN JIKA PW BEDA DENGAN TABEL : "+mPassword+" "+cekPassword);
                Log.w("NILAI", "merubah password lama pada database baru kemudian login");
                changePassword(cekGmail, cekPassword, mUserType, mUserId, mGmail, mPassword, mImg, mNoRUmah, mNoKK, mNamaKK, mNoHp, mOnlineStatus);
            }
            else {
                Log.w("NILAI", "User logout");
                Log.w("NILAI", "Tidak bisa mengganti password");
                finish();
            }
        }


    }

    private void changePassword(String cekGmail, String cekPassword, String mUserType, String mUserId, String mGmail,
                                String mPassword, String mImg, String mNoRUmah, String mNoKK, String mNamaKK, String mNoHp, String mOnlineStatus) {
                    Log.w("NILAI", "prosess....");
                  //update password baru
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("userId", mUserId);
                    hashMap.put("userType", mUserType);
                    hashMap.put("noRumah", mNoRUmah);
                    hashMap.put("noKK", mNoKK);
                    hashMap.put("namaKepalaKeluarga", mNamaKK);
                    hashMap.put("noHp", mNoHp);
                    hashMap.put("gmail", mGmail);
                    hashMap.put("password", cekPassword);
                    hashMap.put("img", mImg);
                    hashMap.put("onlineStatus", "online");

                    Log.w("NILAI", ".......");
                    updateAkun(hashMap, mUserType, mUserId);


    }

    private void updateAkun(HashMap<Object, String> hashMap, final String mUserType, final String mUserId) {

        Log.w("NILAI", "update akun.");

        final DatabaseReference databaseUsers;
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        Log.w("NILAI", "...");
        databaseUsers.child(mUserId).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w("NILAI", "data terupdate.");
                    }
                });

    }

//    @Override
//    protected void onStart() {
//        checkUserStatus();
//        super.onStart();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        checkUserStatus();
//    }
//
//    private void checkUserStatus() {
//        FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//            //user is signed in stay here
////            userId = user.getUid();
//            cekUserType();
//            Log.w("STATUS_USER","LOGIN "+user);
//        } else {
//
//            Log.w("STATUS_USER","SIGNOUT "+user);
//        }
//    }


    private void cekUserType(String mUserType, String mUserId) {
        if (mUserType.equals("admin")) {

            Log.w("NILAI", "sebagai admin.");
            //login sebagai admin
            pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Admin ", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("MY_USERID", mUserId);
            startActivity(intent);
            finish();

        }
        if (mUserType.equals("warga")) {

            Log.w("NILAI", "sebagai warga.");
            //login sebagai admin
            pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Warga ", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MainWargaActivity.class);
            intent.putExtra("MY_USERID", mUserId);
            startActivity(intent);
            finish();
        }
    }


    private void checkUserStatus() {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null ){
            Log.w("NILAI", "User logged");
            checkUserLogin();
        }
        else {
            Log.w("NILAI", "User logout");

            finish();
        }
    }

    private void checkUserType() {

        final DatabaseReference databaseUsers;
        final String getEmailUser = auth.getCurrentUser().getEmail();

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    user.setUserid(snapshot.getKey());
                    dataUser.add(user);
                }

                String mUserType = "";
                String mUserId = "";
                int n = dataUser.size();

                for (int i=0; i<n; i++) {

                    String gmail = dataUser.get(i).getGmail();

                    if (gmail.equals(getEmailUser)){
                        mUserType = dataUser.get(i).getUserType();
//                        etGmail.setText(mUserType);
                        mUserId = dataUser.get(i).getUserid();
                    }
                }
//
//                etPassword.setText(mUserType);

                if (mUserType.equals("admin")) {
                    //login sebagai admin
                    pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Admin ", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("MY_USERID", mUserId);
                    startActivity(intent);
                    finish();
                }
                else {
                    //login sebagai warga
                    pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Warga ", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, MainWargaActivity.class);
                    intent.putExtra("MY_USERID", mUserId);
                    startActivity(intent);
                    finish();
                }
//                if (mUserType.equals("warga")) {
//                    //login sebagai warga
//                    pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Warga", Toast.LENGTH_LONG).show();
//
//                    Intent intent = new Intent(LoginGmailActivity.this, MainWargaActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void cekLogin() {

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
//            checkUserLogin();
        }
    }

    private void checkUserLogin() {
        pd.setMessage("Memuat...");
        pd.show();

        final DatabaseReference databaseUsers;
        final String getEmailUser = auth.getCurrentUser().getEmail();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    user.setUserid(snapshot.getKey());
                    dataUser.add(user);
                }

                String mUserType = "";
                String mUserId = "";

                int n = dataUser.size();

                for (int i=0; i<n; i++) {

                    String gmail = dataUser.get(i).getGmail();

                    if (gmail.equals(getEmailUser)){
                        mUserType = dataUser.get(i).getUserType();
                        mUserId = dataUser.get(i).getUserid();
                    }
                }

                if (mUserType.equals("admin")) {
                    //login sebagai admin
                    pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Admin ", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("MY_USERID", mUserId);
                    startActivity(intent);
                    finish();

                }
                if (mUserType.equals("warga")) {
                    //login sebagai admin
                    pd.dismiss();
//                    Toast.makeText(LoginGmailActivity.this, "Hallo Warga ", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, MainWargaActivity.class);
                    intent.putExtra("MY_USERID", mUserId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(LoginGmailActivity.this,"error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}






















