package com.tafh.tugasakhir.login.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.akun.AkunDetailActivity;

public class LupaPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;

    private Context context = LupaPasswordActivity.this;

    private ProgressDialog pd;
    ActionBar actionBar;

    private EditText gmailLupaPassword;
    private Button btnLupaPassword, btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        gmailLupaPassword = findViewById(R.id.et_gmail_lupa_password);
        btnLupaPassword = findViewById(R.id.btn_lupa_pass);
        btnKembali = findViewById(R.id.btn_kembali);

        btnKembali.setOnClickListener(this);
        btnLupaPassword.setOnClickListener(this);

    }

    private void kirimGmailLupaPassword() {
        String gmail = gmailLupaPassword.getText().toString().trim();

        auth.sendPasswordResetEmail(gmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            gmailLupaPassword.setText("");
                            Toast.makeText(context, "Email terkirim....",Toast.LENGTH_LONG).show();

                        } else {
                            pd.dismiss();
                            Toast.makeText(context, "Gagal....",Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(context, "Masukkan Email yang sesuai!",Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kembali:
                onSupportNavigateUp();
                finish();
                break;
            case R.id.btn_lupa_pass:
                pd.setMessage("Tunggu Sebentar...");
                pd.show();
                kirimGmailLupaPassword();
                break;
        }
    }
}
