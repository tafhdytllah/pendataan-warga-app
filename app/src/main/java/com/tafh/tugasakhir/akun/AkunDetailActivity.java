package com.tafh.tugasakhir.akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.dialogs.UpdateAkunDialog;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.profile.SettingProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class AkunDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mNamaTv, mNoRumahTv, mNoKkTv, mNoHpTv, mGmailTv, mPasswordTv;
    private ImageView mImageIv;
    private Button btnHapus, btnUbah, btnLupaPassword;

    private DatabaseReference databaseUser;
    private FirebaseAuth auth;
    private FirebaseAuth auth2;

    private ArrayList<ModelUser> dataAkun;

    private String mImage,mNama, mNoRumah, mNoKK, mNoHp, mGmail, mPassword, mUserId, mUserType;

    private ProgressDialog pd;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_detail);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Detail User");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        pd = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://tugas-akhir-6dbe1.firebaseio.com/")
                .setApiKey("AIzaSyB2_m26hKHrwCWgniyenBivp-LKtdQfINM")
                .setApplicationId("tugas-akhir-6dbe1").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            auth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            auth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }



//        mKeyTv = findViewById(R.id.tv_key);

        mNamaTv = findViewById(R.id.tv_nama);
        mNoRumahTv = findViewById(R.id.tv_no_rumah);
        mNoKkTv = findViewById(R.id.tv_no_kk);

        mNoHpTv = findViewById(R.id.tv_no_hp);
        mGmailTv = findViewById(R.id.tv_gmail);
        mPasswordTv = findViewById(R.id.tv_password);

        mImageIv = findViewById(R.id.iv_ImageView);
        mImageIv.setVisibility(View.GONE);


        btnLupaPassword = findViewById(R.id.btn_lupa_password);
        btnLupaPassword.setOnClickListener(this);

        btnUbah = findViewById(R.id.btn_ubah);
        btnUbah.setVisibility(View.GONE);
        btnUbah.setOnClickListener(this);

        btnHapus = findViewById(R.id.btn_hapus);
        btnHapus.setOnClickListener(this);

        //Mendapatkan Instance autentikasi dan Referensi dari Database
// mengambil referensi ke Firebase Database


        //now get our data from intent in which we put our data
        getDataView();

//        getData();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //    private void getData() {
//
//        reference.child("Admin")
//                .child(auth.getUid())
//                .child("Akun")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        //Inisialisasi ArrayList
//                        dataAkun = new ArrayList<>();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            //Mapping data pada DataSnapshot ke dalam objek akun
//                            ModelUser akun = snapshot.getValue(ModelUser.class);
//                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
//                            akun.setKey(snapshot.getKey());
//                            dataAkun.add(akun);
//                        }
//
////                        //Inisialisasi Adapter dan data akun dalam bentuk Array
////                        adapter = new AkunAdapter(dataAkun, ListDataAkunActivity.this);
//////                        //Memasang Adapter pada RecyclerView
////                        recyclerView.setAdapter(adapter);
////                        Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat", Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        /*
//                        Kode ini akan dijalankan ketika ada error dan
//                        pengambilan data error tersebut lalu memprint error nya
//                        ke LogCat
//                        */
//                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
//                        Log.e("ListDataAkunActivity", databaseError.getDetails()+" "+databaseError.getMessage());
//
//                    }
//                });
//    }

    private void getDataView() {

        Intent intent = getIntent();

        mUserId = intent.getStringExtra("iUserId");
        mNama = intent.getStringExtra("iNama");
        mNoRumah = intent.getStringExtra("iNoRumah");
        mNoKK = intent.getStringExtra("iNoKK");
        mNoHp = intent.getStringExtra("iNoHP");

        mGmail = intent.getStringExtra("iGmail");
        mPassword = intent.getStringExtra("iPassword");
        mImage = intent.getStringExtra("image");
        mUserType = intent.getStringExtra("iUserType");
//        String mImage = intent.getStringExtra("iImage");

//        try {
//            Picasso.with(AkunDetailActivity.this)
//                    .load(mImage)
//                    .placeholder(R.drawable.ic_akun_black_24dp)
//                    .into(mImageIv);
//
//        } catch (Exception e) {
//
//        }
//
//        byte[] mBytes = getIntent().getByteArrayExtra("iImage");
//        //now decode image because from previous activity we got our image in bytes
//        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);


        //now set our data in our view, which we get in our previous actiivity
//        mKeyTv.setText(mKey);

        mNamaTv.setText(mNama);
        mNoRumahTv.setText(mNoRumah);
        mNoKkTv.setText(mNoKK);

        mNoHpTv.setText(mNoHp);
        mGmailTv.setText(mGmail);
        mPasswordTv.setText(mPassword);
//
//        mImageIv.setImageBitmap(bitmap);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ubah:
//                Intent intent = new Intent(AkunDetailActivity.this, DaftarAkunActivity.class);
//                startActivity(intent);

                UpdateAkunDialog updateAkunDialog = new UpdateAkunDialog();
                //open layout dialog
                updateAkunDialog.show(getSupportFragmentManager(), "update akun dialog");

                break;
            case R.id.btn_hapus:
                //hapus user berdasarkan userId
                dialogKonfirmasi();

                break;

            case R.id.btn_lupa_password:
                    lupaPassword();
                break;
        }
    }

    private void lupaPassword() {

        //alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Setel Ulang Password");
        //set layout linier layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in dialog
        final TextView emailTv = new EditText(this);
        emailTv.setText(mGmail);
        emailTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        emailTv.setEnabled(false);
        emailTv.setFocusable(false);
        emailTv.setCursorVisible(false);
        emailTv.setClickable(false);

        linearLayout.addView(emailTv);
        linearLayout.setPadding(32, 10, 10, 10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd.setMessage("Mengirim email....");
                pd.show();
                //inpit email recover
                String email = mGmail.trim();
                beginRecovery(email);
            }
        });
        //buttons cancel
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss
                pd.dismiss();
            }
        });

        //show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {


        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(AkunDetailActivity.this, "Email terkirim....",Toast.LENGTH_LONG).show();

                        } else {
                            pd.dismiss();
                            Toast.makeText(AkunDetailActivity.this, "Gagal....",Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(AkunDetailActivity.this, "Masukkan Email yang sesuai!",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void dialogKonfirmasi() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        hapusUser();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(AkunDetailActivity.this);
        builder.setMessage("Apakah Anda yakin?").setPositiveButton("Ya", dialogClickListener)
                .setNegativeButton("Tidak", dialogClickListener).show();
    }


    private void hapusUser() {
        pd.setMessage("Menghapus data...");
        pd.show();

        databaseUser = FirebaseDatabase.getInstance().getReference("Users");

        final String userId = mUserId;
        String password = mPassword;
        String email = mGmail;

        final AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        //for post-image name, post-id, post-publish-time
        String filePathAndName = "Users/" + "user_profile_" + userId;


        if (databaseUser != null){

            // Delete foto sebelumnya
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileref = storageRef.child("Users/user_profile_" + userId);
            profileref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

            auth2.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final FirebaseUser user = auth2.getCurrentUser();

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    databaseUser.child(userId).removeValue()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(AkunDetailActivity.this, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show();

                                                                    pd.dismiss();
                                                                    auth2.signOut();
                                                                    finish();
                                                                }
                                                            });
                                                }
                                                else {
                                                    Log.w("Error", "Error no user delete");
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Log.w("Error", "Error not updated");
                                    }
                                }
                            });
                        }
                    });

        }
    }
}
