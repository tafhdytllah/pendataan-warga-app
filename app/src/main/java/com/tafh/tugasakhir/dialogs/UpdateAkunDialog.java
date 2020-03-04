package com.tafh.tugasakhir.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tafh.tugasakhir.akun.AkunDetailActivity;
import com.tafh.tugasakhir.akun.ListDataAkunActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.HashMap;

public class UpdateAkunDialog extends AppCompatDialogFragment {
    private TextView mKeyTv;
    private EditText mNamaBaru, mNoRumahBaru, mNoKkBaru, mNoHpBaru, mGmailBaru, mPasswordBaru;
    private String cekNama, cekNoRUmah, cekNoKK, cekNoHp, cekGmail, cekPassword;
    private String getUserId,getNama,getNoRumah,getNoKK,getNoHp,getGmail,getPassword,getUserType,getImage;
    private DatabaseReference reference;

    private FirebaseAuth auth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_view_update_akun,
                null);
//        mUserIdTv = view.findViewById(R.id.tv_userid);
        mNamaBaru = view.findViewById(R.id.new_nama);
        mNoRumahBaru = view.findViewById(R.id.new_no_rumah);
        mNoKkBaru = view.findViewById(R.id.new_no_kk);

        mNoHpBaru = view.findViewById(R.id.new_no_hp);
        mGmailBaru = view.findViewById(R.id.new_gmail);
        mPasswordBaru = view.findViewById(R.id.new_password);

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        getData();

        builder.setView(view).setTitle(" ")
                .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Mendapatkan Data yang akan dicek
                        cekNama = mNamaBaru.getText().toString();
                        cekNoRUmah = mNoRumahBaru.getText().toString();
                        cekNoKK = mNoKkBaru.getText().toString();

                        cekNoHp = mNoHpBaru.getText().toString();
                        cekGmail = mGmailBaru.getText().toString();
                        cekPassword = mPasswordBaru.getText().toString();

                        //Mengecek agar tidak ada data yang kosong, saat proses update
                        if (isEmpty(cekNama)
                                || isEmpty(cekNoRUmah)
                                || isEmpty(cekNoKK)
                                || isEmpty(cekNoHp)
                                || isEmpty(cekGmail)
                                || isEmpty(cekPassword)) {

                            Toast.makeText(getContext(), "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();

                        } else {
                            /*
                                Menjalankan proses update data.
                                Method Setter digunakan untuk mendapakan data baru yang diinputkan User.
                            */
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("userType", getUserType);
                            hashMap.put("noRumah", cekNoRUmah);
                            hashMap.put("noKK", cekNoKK);
                            hashMap.put("namaKepalaKeluarga", cekNama);
                            hashMap.put("noHp", cekNoHp);

                            hashMap.put("gmail", cekGmail);
                            hashMap.put("password", cekPassword);
                            hashMap.put("img", getImage);
                            hashMap.put("search", "noSearch");
                            hashMap.put("cover", "noCover");
                            hashMap.put("onlineStatus", "online");


                            //ubah database firebase nya
                            updateAkun(hashMap);

                            Toast.makeText(getContext(), "Data Berhasil di Ubah", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getContext(), ListDataAkunActivity.class);
                            startActivity(intent);

                        }

                    }
                });



        return builder.create();
    }

    private void updateAkun(HashMap<Object, String> hashMap) {
        Intent intent = getActivity().getIntent();
        String userId = intent.getExtras().getString("iUserId");

        reference.child("Users")
                .child(userId)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mNamaBaru.setText(cekNama);
                        mNoRumahBaru.setText(cekNoRUmah);
                        mNoKkBaru.setText(cekNoKK);
                        mNoHpBaru.setText(cekNoHp);
                        mGmailBaru.setText(cekGmail);
                        mPasswordBaru.setText(cekPassword);
                    }
                });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void getData() {
        Intent intent = getActivity().getIntent();
//        String getUserId = intent.getStringExtra("iUserId");
        getUserId = intent.getStringExtra("iUserId");
        getNama = intent.getStringExtra("iNama");
        getNoRumah = intent.getStringExtra("iNoRumah");
        getNoKK = intent.getStringExtra("iNoKK");
        getNoHp = intent.getStringExtra("iNoHP");

        getGmail = intent.getStringExtra("iGmail");
        getPassword = intent.getStringExtra("iPassword");
        getImage = intent.getStringExtra("image");
        getUserType = intent.getStringExtra("iUserType");

        //now set our data in our view, which we get in our previous actiivity
//        mUserIdTv.setText(getUserId);
        mNamaBaru.setText(getNama);
        mNoRumahBaru.setText(getNoRumah);
        mNoKkBaru.setText(getNoKK);

        mNoHpBaru.setText(getNoHp);
        mGmailBaru.setText(getGmail);
        mPasswordBaru.setText(getPassword);
    }


}
