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
import com.tafh.tugasakhir.keluarga.ListDataKeluargaActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;

public class UpdateKeluargaDialog extends AppCompatDialogFragment {

    private EditText mNoRumahBaru, mNoKkBaru, mNamaKKBaru,  mNikNamaKKBaru, mNamaIstriBaru, mNikIstriBaru, mNamaAnakBaru, mNikAnakBaru;
    private String cekNoRUmah, cekNoKK, cekNamaKK, cekNiknamaKK, cekNamaIstri, cekNikIstri, cekNamaAnak, cekNikAnak;
    private DatabaseReference reference;

    private FirebaseAuth auth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_view_update_keluarga, null);
//        mKeluargaIdTv = view.findViewById(R.id.tv_keluargaId);
        mNoRumahBaru = view.findViewById(R.id.new_no_rumah_keluarga);
        mNoKkBaru = view.findViewById(R.id.new_no_kk_keluarga);
        mNamaKKBaru = view.findViewById(R.id.new_nama_kk_keluarga);
        mNikNamaKKBaru = view.findViewById(R.id.new_nik_nama_kk_keluarga);

        mNamaIstriBaru = view.findViewById(R.id.new_nama_istri_keluarga);
        mNikIstriBaru = view.findViewById(R.id.new_nik_istri_keluarga);
        mNamaAnakBaru = view.findViewById(R.id.new_nama_anak_keluarga);
        mNikAnakBaru = view.findViewById(R.id.new_nik_anak_keluarga);

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
                        cekNoRUmah = mNoRumahBaru.getText().toString();
                        cekNoKK = mNoKkBaru.getText().toString();
                        cekNamaKK = mNamaKKBaru.getText().toString();
                        cekNiknamaKK = mNikNamaKKBaru.getText().toString();

                        cekNamaIstri = mNamaIstriBaru.getText().toString();
                        cekNikIstri = mNikIstriBaru.getText().toString();
                        cekNamaAnak = mNamaAnakBaru.getText().toString();
                        cekNikAnak = mNikAnakBaru.getText().toString();

                        //Mengecek agar tidak ada data yang kosong, saat proses update
                        if (isEmpty(cekNoRUmah)
                                || isEmpty(cekNoKK)
                                || isEmpty(cekNamaKK)
                                || isEmpty(cekNiknamaKK)

                                || isEmpty(cekNamaIstri)
                                || isEmpty(cekNikIstri)
                                || isEmpty(cekNamaAnak)
                                || isEmpty(cekNikIstri)) {

                            Toast.makeText(getContext(), "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();

                        } else {
                            /*
                                Menjalankan proses update data.
                                Method Setter digunakan untuk mendapakan data baru yang diinputkan User.
                            */


                            //ubah database firebase nya
//                            updateKeluarga(keluarga);

                            Toast.makeText(getContext(), "Data Berhasil di Ubah", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getContext(), ListDataKeluargaActivity.class);
                            startActivity(intent);

                        }

                    }
                });



        return builder.create();
    }

    private void updateKeluarga(ModelKeluarga setKeluarga) {
        Intent intent = getActivity().getIntent();
//
//        String userId = auth.getUid();
        String keluargaId = intent.getExtras().getString("iKeluargaId");

        reference.child("Keluarga")
                .child(keluargaId)
                .setValue(setKeluarga)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mNoRumahBaru.setText(cekNoRUmah);
                        mNoKkBaru.setText(cekNoKK);
                        mNamaKKBaru.setText(cekNamaKK);
                        mNikNamaKKBaru.setText(cekNiknamaKK);

                        mNamaIstriBaru.setText(cekNamaIstri);
                        mNikIstriBaru.setText(cekNikIstri);
                        mNamaAnakBaru.setText(cekNamaAnak);
                        mNikAnakBaru.setText(cekNikAnak);
                    }
                });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void getData() {
        Intent intent = getActivity().getIntent();
//        String getUserId = intent.getStringExtra("iUserId");
        String getNoRumah = intent.getStringExtra("iNoRumah");
        String getNoKK = intent.getStringExtra("iNoKK");
        String getNamaKK = intent.getStringExtra("iNamaKK");
        String getNikNamaKK = intent.getStringExtra("iNikNamaKK");

        String getNamaIstri = intent.getStringExtra("iNamaIstri");
        String getNikIstri = intent.getStringExtra("iNikIstri");
        String getNamaAnak = intent.getStringExtra("iNamaAnak");
        String getNikAnak = intent.getStringExtra("iNikAnak");

        //now set our data in our view, which we get in our previous actiivity
//        mUserIdTv.setText(getUserId);

        mNoRumahBaru.setText(getNoRumah);
        mNoKkBaru.setText(getNoKK);
        mNamaKKBaru.setText(getNamaKK);
        mNikNamaKKBaru.setText(getNikNamaKK);

        mNamaIstriBaru.setText(getNamaIstri);
        mNikIstriBaru.setText(getNikIstri);
        mNamaAnakBaru.setText(getNamaAnak);
        mNikAnakBaru.setText(getNikAnak);
    }


}
