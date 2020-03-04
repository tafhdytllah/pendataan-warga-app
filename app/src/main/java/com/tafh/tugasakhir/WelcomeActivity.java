package com.tafh.tugasakhir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    private ArrayList<ModelUser> dataUser;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    private Context context = WelcomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        requestWindowFeature(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {

                    //cekLogin
                    cekLogin();

                }}, SPLASH_TIME_OUT);




    }

    @Override
    protected void onStart() {
//        auth.signOut();

        super.onStart();
    }

    private void cekLogin() {

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            checkUserLogin();
        }
        else {
            startActivity(new Intent(WelcomeActivity.this, LoginGmailActivity.class));
            finish();
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


                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("MY_USERID", mUserId);
                    startActivity(intent);
                    finish();

                }
                if (mUserType.equals("warga")) {
                    //login sebagai admin
                    pd.dismiss();

                    Intent intent = new Intent(context, MainWargaActivity.class);
                    intent.putExtra("MY_USERID", mUserId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(context,"error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
