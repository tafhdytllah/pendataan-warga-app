package com.tafh.tugasakhir.keluarga;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;

public class KeluargaKeluarDetailActivity extends AppCompatActivity{


    private TextView
            noRumah,
            noKK,
            namaKK,
            nik1,
            jenisKelamin1,
            tempatLahir1,
            tanggalLahir1,
            agama1,
            namaIstri,
            nik2,
            statusHubungan2,
            jenisKelamin2,
            tempatLahir2,
            tanggalLahir2,
            agama2,
            nama3,
            nik3,
            statusHubungan3,
            jenisKelamin3,
            tempatLahir3,
            tanggalLahir3,
            agama3,

    nama4,
            nik4,
            statusHubungan4,
            jenisKelamin4,
            tempatLahir4,
            tanggalLahir4,
            agama4,

    nama5,
            nik5,
            statusHubungan5,
            jenisKelamin5,
            tempatLahir5,
            tanggalLahir5,
            agama5,

    nama6,
            nik6,
            statusHubungan6,
            jenisKelamin6,
            tempatLahir6,
            tanggalLahir6,
            agama6,

    nama7,
            nik7,
            statusHubungan7,
            jenisKelamin7,
            tempatLahir7,
            tanggalLahir7,
            agama7,

    nama8,
            nik8,
            statusHubungan8,
            jenisKelamin8,
            tempatLahir8,
            tanggalLahir8,
            agama8,

    nama9,
            nik9,
            statusHubungan9,
            jenisKelamin9,
            tempatLahir9,
            tanggalLahir9,
            agama9,

    nama10,
            nik10,
            statusHubungan10,
            jenisKelamin10,
            tempatLahir10,
            tanggalLahir10,
            agama10;

    private String
            gKeluargaNonAktifId,
            gKeluargaId,
            gUserId,
            gImageKK,
            gNoRumah,
            gNoKK,
            gNamaKK,
            gNik1,
            gJenisKelamin1,
            gTempatLahir1,
            gtglLahir1,
            gAgama1,
            gNamaIstri,
            gNik2,
            gStatusHubungan2,
            gJenisKelamin2,
            gTempatLahir2,
            gtglLahir2,
            gAgama2,

    gNama3,
            gNik3,
            gStatusHubungan3,
            gJenisKelamin3,
            gTempatLahir3,
            gtglLahir3,
            gAgama3,

    gNama4,
            gNik4,
            gStatusHubungan4,
            gJenisKelamin4,
            gTempatLahir4,
            gtglLahir4,
            gAgama4,

    gNama5,
            gNik5,
            gStatusHubungan5,
            gJenisKelamin5,
            gTempatLahir5,
            gtglLahir5,
            gAgama5,

    gNama6,
            gNik6,
            gStatusHubungan6,
            gJenisKelamin6,
            gTempatLahir6,
            gtglLahir6,
            gAgama6,

    gNama7,
            gNik7,
            gStatusHubungan7,
            gJenisKelamin7,
            gTempatLahir7,
            gtglLahir7,
            gAgama7,

    gNama8,
            gNik8,
            gStatusHubungan8,
            gJenisKelamin8,
            gTempatLahir8,
            gtglLahir8,
            gAgama8,

    gNama9,
            gNik9,
            gStatusHubungan9,
            gJenisKelamin9,
            gTempatLahir9,
            gtglLahir9,
            gAgama9,

    gNama10,
            gNik10,
            gStatusHubungan10,
            gJenisKelamin10,
            gTempatLahir10,
            gtglLahir10,
            gAgama10;

    private ImageView mImageIv;
    private Button btnHapus, btnUbah;
    private DatabaseReference databaseKeluargaKeluar;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    private LinearLayout LL2, l3, l4, l5, l6, l7, l8, l9, l10;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluarga_keluar_detail);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Keluarga Non-Aktif");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress_bar_keluarga_detail_nonAktif);

        noRumah = findViewById(R.id.tv_norumah_keluarga_nonAktif);
        noKK = findViewById(R.id.tv_no_kk_keluarga_nonAktif);
        namaKK = findViewById(R.id.tv_nama_kk_keluarga_nonAktif);
        nik1 = findViewById(R.id.tv_nik1_keluarga_nonAktif);
        jenisKelamin1 = findViewById(R.id.tv_jeniskelamin1_keluarga_nonAktif);
        tempatLahir1 = findViewById(R.id.tv_tempatlahir1_keluarga_nonAktif);
        tanggalLahir1 = findViewById(R.id.tv_tanggallahir1_keluarga_nonAktif);
        agama1 = findViewById(R.id.tv_agama1_keluarga_nonAktif);
        mImageIv = findViewById(R.id.iv_ImageView_keluarga_nonAktif);

        namaIstri = findViewById(R.id.tv_nama_istri_keluarga_nonAktif);
        nik2 = findViewById(R.id.tv_nik2_keluarga_nonAktif);
        statusHubungan2 = findViewById(R.id.tv_statushubungan2_keluarga_nonAktif);
        jenisKelamin2 = findViewById(R.id.tv_jeniskelamin2_keluarga_nonAktif);
        tempatLahir2 = findViewById(R.id.tv_tempatlahir2_keluarga_nonAktif);
        tanggalLahir2 = findViewById(R.id.tv_tanggallahir2_keluarga_nonAktif);
        agama2 = findViewById(R.id.tv_agama2_keluarga_nonAktif);


        nama3 = findViewById(R.id.tv_nama3_keluarga_warga);
        nik3 = findViewById(R.id.tv_nik3_keluarga_warga);
        statusHubungan3 = findViewById(R.id.tv_statushubungan3_keluarga_warga);
        jenisKelamin3 = findViewById(R.id.tv_jeniskelamin3_keluarga_warga);
        tempatLahir3 = findViewById(R.id.tv_tempatlahir3_keluarga_warga);
        tanggalLahir3 = findViewById(R.id.tv_tanggallahir3_keluarga_warga);
        agama3 = findViewById(R.id.tv_agama3_keluarga_warga);

        nama4 = findViewById(R.id.tv_nama4_keluarga_warga);
        nik4 = findViewById(R.id.tv_nik4_keluarga_warga);
        statusHubungan4 = findViewById(R.id.tv_statushubungan4_keluarga_warga);
        jenisKelamin4 = findViewById(R.id.tv_jeniskelamin4_keluarga_warga);
        tempatLahir4 = findViewById(R.id.tv_tempatlahir4_keluarga_warga);
        tanggalLahir4 = findViewById(R.id.tv_tanggallahir4_keluarga_warga);
        agama4 = findViewById(R.id.tv_agama4_keluarga_warga);

        nama5 = findViewById(R.id.tv_nama5_keluarga_warga);
        nik5 = findViewById(R.id.tv_nik5_keluarga_warga);
        statusHubungan5 = findViewById(R.id.tv_statushubungan5_keluarga_warga);
        jenisKelamin5 = findViewById(R.id.tv_jeniskelamin5_keluarga_warga);
        tempatLahir5 = findViewById(R.id.tv_tempatlahir5_keluarga_warga);
        tanggalLahir5 = findViewById(R.id.tv_tanggallahir5_keluarga_warga);
        agama5 = findViewById(R.id.tv_agama5_keluarga_warga);

        nama6 = findViewById(R.id.tv_nama6_keluarga_warga);
        nik6 = findViewById(R.id.tv_nik6_keluarga_warga);
        statusHubungan6 = findViewById(R.id.tv_statushubungan6_keluarga_warga);
        jenisKelamin6 = findViewById(R.id.tv_jeniskelamin6_keluarga_warga);
        tempatLahir6 = findViewById(R.id.tv_tempatlahir6_keluarga_warga);
        tanggalLahir6 = findViewById(R.id.tv_tanggallahir6_keluarga_warga);
        agama6 = findViewById(R.id.tv_agama6_keluarga_warga);

        nama7 = findViewById(R.id.tv_nama7_keluarga_warga);
        nik7 = findViewById(R.id.tv_nik7_keluarga_warga);
        statusHubungan7 = findViewById(R.id.tv_statushubungan7_keluarga_warga);
        jenisKelamin7 = findViewById(R.id.tv_jeniskelamin7_keluarga_warga);
        tempatLahir7 = findViewById(R.id.tv_tempatlahir7_keluarga_warga);
        tanggalLahir7 = findViewById(R.id.tv_tanggallahir7_keluarga_warga);
        agama7 = findViewById(R.id.tv_agama7_keluarga_warga);

        nama8 = findViewById(R.id.tv_nama8_keluarga_warga);
        nik8 = findViewById(R.id.tv_nik8_keluarga_warga);
        statusHubungan8 = findViewById(R.id.tv_statushubungan8_keluarga_warga);
        jenisKelamin8 = findViewById(R.id.tv_jeniskelamin8_keluarga_warga);
        tempatLahir8 = findViewById(R.id.tv_tempatlahir8_keluarga_warga);
        tanggalLahir8 = findViewById(R.id.tv_tanggallahir8_keluarga_warga);
        agama8 = findViewById(R.id.tv_agama8_keluarga_warga);

        nama9 = findViewById(R.id.tv_nama9_keluarga_warga);
        nik9 = findViewById(R.id.tv_nik9_keluarga_warga);
        statusHubungan9 = findViewById(R.id.tv_statushubungan9_keluarga_warga);
        jenisKelamin9 = findViewById(R.id.tv_jeniskelamin9_keluarga_warga);
        tempatLahir9 = findViewById(R.id.tv_tempatlahir9_keluarga_warga);
        tanggalLahir9 = findViewById(R.id.tv_tanggallahir9_keluarga_warga);
        agama9 = findViewById(R.id.tv_agama9_keluarga_warga);

        nama10 = findViewById(R.id.tv_nama10_keluarga_warga);
        nik10 = findViewById(R.id.tv_nik10_keluarga_warga);
        statusHubungan10 = findViewById(R.id.tv_statushubungan10_keluarga_warga);
        jenisKelamin10 = findViewById(R.id.tv_jeniskelamin10_keluarga_warga);
        tempatLahir10 = findViewById(R.id.tv_tempatlahir10_keluarga_warga);
        tanggalLahir10 = findViewById(R.id.tv_tanggallahir10_keluarga_warga);
        agama10 = findViewById(R.id.tv_agama10_keluarga_warga);

        l3 = findViewById(R.id.layout_anggota_keluarga3);
        l4 = findViewById(R.id.layout_anggota_keluarga4);
        l5 = findViewById(R.id.layout_anggota_keluarga5);
        l6 = findViewById(R.id.layout_anggota_keluarga6);
        l7 = findViewById(R.id.layout_anggota_keluarga7);
        l8 = findViewById(R.id.layout_anggota_keluarga8);
        l9 = findViewById(R.id.layout_anggota_keluarga9);
        l10 = findViewById(R.id.layout_anggota_keluarga10);

        mImageIv = findViewById(R.id.iv_ImageView_keluarga_nonAktif);

        btnHapus = findViewById(R.id.btn_hapus_keluarga_nonAktif);

        getDataView();

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hapus keluarga berdasarkan keluargaId
                hapusKeluargaNonAktif();
            }
        });

        auth = FirebaseAuth.getInstance();

        //now get our data from intent in which we put our data


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }

    private void getDataView() {

        Intent intent = getIntent();
        gKeluargaNonAktifId = intent.getStringExtra("gKeluargaNonAktifId");
        gKeluargaId = intent.getStringExtra("gKeluargaId");
        gUserId = intent.getStringExtra("gUserId");
        gImageKK = intent.getStringExtra("gImageKK");
        gNoRumah = intent.getStringExtra("gNoRumah");
        gNoKK = intent.getStringExtra("gNoKK");

        gNamaKK = intent.getStringExtra("gNamaKK");
        gNik1 = intent.getStringExtra("gNik1");
        gJenisKelamin1 = intent.getStringExtra("gJenisKelamin1");
        gTempatLahir1 = intent.getStringExtra("gTempatLahir1");
        gtglLahir1 = intent.getStringExtra("gtglLahir1");
        gAgama1 = intent.getStringExtra("gAgama1");

        gNamaIstri = intent.getStringExtra("gNamaIstri");
        gStatusHubungan2 = intent.getStringExtra("gStatusHubungan2");
        gNik2 = intent.getStringExtra("gNik2");
        gJenisKelamin2 = intent.getStringExtra("gJenisKelamin2");
        gTempatLahir2 = intent.getStringExtra("gTempatLahir2");
        gtglLahir2 = intent.getStringExtra("gtglLahir2");
        gAgama2 = intent.getStringExtra("gAgama2");


        gNama3 = intent.getStringExtra("gNama3");
        gStatusHubungan3 = intent.getStringExtra("gStatusHubungan3");
        gNik3 = intent.getStringExtra("gNik3");
        gJenisKelamin3 = intent.getStringExtra("gJenisKelamin3");
        gTempatLahir3 = intent.getStringExtra("gTempatLahir3");
        gtglLahir3 = intent.getStringExtra("gtglLahir3");
        gAgama3 = intent.getStringExtra("gAgama3");

        gNama4 = intent.getStringExtra("gNama4");
        gStatusHubungan4 = intent.getStringExtra("gStatusHubungan4");
        gNik4 = intent.getStringExtra("gNik4");
        gJenisKelamin4 = intent.getStringExtra("gJenisKelamin4");
        gTempatLahir4 = intent.getStringExtra("gTempatLahir4");
        gtglLahir4 = intent.getStringExtra("gtglLahir4");
        gAgama4 = intent.getStringExtra("gAgama4");

        gNama5 = intent.getStringExtra("gNama5");
        gStatusHubungan5 = intent.getStringExtra("gStatusHubungan5");
        gNik5 = intent.getStringExtra("gNik5");
        gJenisKelamin5 = intent.getStringExtra("gJenisKelamin5");
        gTempatLahir5 = intent.getStringExtra("gTempatLahir5");
        gtglLahir5 = intent.getStringExtra("gtglLahir5");
        gAgama5 = intent.getStringExtra("gAgama5");

        gNama6 = intent.getStringExtra("gNama6");
        gStatusHubungan6 = intent.getStringExtra("gStatusHubungan6");
        gNik6 = intent.getStringExtra("gNik6");
        gJenisKelamin6 = intent.getStringExtra("gJenisKelamin6");
        gTempatLahir6 = intent.getStringExtra("gTempatLahir6");
        gtglLahir6 = intent.getStringExtra("gtglLahir6");
        gAgama6 = intent.getStringExtra("gAgama6");

        gNama7 = intent.getStringExtra("gNama7");
        gStatusHubungan7 = intent.getStringExtra("gStatusHubungan7");
        gNik7 = intent.getStringExtra("gNik7");
        gJenisKelamin7 = intent.getStringExtra("gJenisKelamin7");
        gTempatLahir7 = intent.getStringExtra("gTempatLahir7");
        gtglLahir7 = intent.getStringExtra("gtglLahir7");
        gAgama7 = intent.getStringExtra("gAgama7");

        gNama8 = intent.getStringExtra("gNama8");
        gStatusHubungan8 = intent.getStringExtra("gStatusHubungan8");
        gNik8 = intent.getStringExtra("gNik8");
        gJenisKelamin8 = intent.getStringExtra("gJenisKelamin8");
        gTempatLahir8 = intent.getStringExtra("gTempatLahir8");
        gtglLahir8 = intent.getStringExtra("gtglLahir8");
        gAgama8 = intent.getStringExtra("gAgama8");

        gNama9 = intent.getStringExtra("gNama9");
        gStatusHubungan9 = intent.getStringExtra("gStatusHubungan9");
        gNik9 = intent.getStringExtra("gNik9");
        gJenisKelamin9 = intent.getStringExtra("gJenisKelamin9");
        gTempatLahir9 = intent.getStringExtra("gTempatLahir9");
        gtglLahir9 = intent.getStringExtra("gtglLahir9");
        gAgama9 = intent.getStringExtra("gAgama9");

        gNama10 = intent.getStringExtra("gNama10");
        gStatusHubungan10 = intent.getStringExtra("gStatusHubungan10");
        gNik10 = intent.getStringExtra("gNik10");
        gJenisKelamin10 = intent.getStringExtra("gJenisKelamin10");
        gTempatLahir10 = intent.getStringExtra("gTempatLahir10");
        gtglLahir10 = intent.getStringExtra("gtglLahir10");
        gAgama10 = intent.getStringExtra("gAgama10");


        try {
            Picasso.with(this)
                    .load(gImageKK)
                    .placeholder(R.drawable.ic_akun_black_24dp)
                    .into(mImageIv);
        } catch (Exception e) {

        }
        noRumah.setText(gNoRumah);
        noKK.setText(gNoKK);
        namaKK.setText(gNamaKK);
        nik1.setText(gNik1);
        jenisKelamin1.setText(gJenisKelamin1);
        tempatLahir1.setText(gTempatLahir1);
        tanggalLahir1.setText(gtglLahir1);
        agama1.setText(gAgama1);

        namaIstri.setText(gNamaIstri);
        nik2.setText(gNik2);
        statusHubungan2.setText(gStatusHubungan2);
        jenisKelamin2.setText(gJenisKelamin2);
        tempatLahir2.setText(gTempatLahir2);
        tanggalLahir2.setText(gtglLahir2);
        agama2.setText(gAgama2);


        if (gNik3.equals("") && gNama3.equals("")) {
            l3.setVisibility(View.GONE);
        } else {
            l3.setVisibility(View.VISIBLE);

            nama3.setText(gNama3);
            nik3.setText(gNik3);
            statusHubungan3.setText(gStatusHubungan3);
            jenisKelamin3.setText(gJenisKelamin3);
            tempatLahir3.setText(gTempatLahir3);
            tanggalLahir3.setText(gtglLahir3);
            agama3.setText(gAgama3);
        }

        if (gNik4.equals("") && gNama4.equals("")) {
            l4.setVisibility(View.GONE);
        } else {
            l4.setVisibility(View.VISIBLE);

            nama4.setText(gNama4);
            nik4.setText(gNik4);
            statusHubungan4.setText(gStatusHubungan4);
            jenisKelamin4.setText(gJenisKelamin4);
            tempatLahir4.setText(gTempatLahir4);
            tanggalLahir4.setText(gtglLahir4);
            agama4.setText(gAgama4);
        }

        if (gNik5.equals("") && gNama5.equals("")) {
            l5.setVisibility(View.GONE);
        } else {
            l5.setVisibility(View.VISIBLE);

            nama5.setText(gNama5);
            nik5.setText(gNik5);
            statusHubungan5.setText(gStatusHubungan5);
            jenisKelamin5.setText(gJenisKelamin5);
            tempatLahir5.setText(gTempatLahir5);
            tanggalLahir5.setText(gtglLahir5);
            agama5.setText(gAgama5);
        }

        if (gNik6.equals("") && gNama6.equals("")) {
            l6.setVisibility(View.GONE);
        } else {
            l6.setVisibility(View.VISIBLE);

            nama6.setText(gNama6);
            nik6.setText(gNik6);
            statusHubungan6.setText(gStatusHubungan6);
            jenisKelamin6.setText(gJenisKelamin6);
            tempatLahir6.setText(gTempatLahir6);
            tanggalLahir6.setText(gtglLahir6);
            agama6.setText(gAgama6);
        }

        if (gNik7.equals("") && gNama7.equals("")) {
            l7.setVisibility(View.GONE);
        } else {
            l7.setVisibility(View.VISIBLE);

            nama7.setText(gNama7);
            nik7.setText(gNik7);
            statusHubungan7.setText(gStatusHubungan7);
            jenisKelamin7.setText(gJenisKelamin7);
            tempatLahir7.setText(gTempatLahir7);
            tanggalLahir7.setText(gtglLahir7);
            agama7.setText(gAgama7);
        }

        if (gNik8.equals("") && gNama8.equals("")) {
            l8.setVisibility(View.GONE);
        } else {
            l8.setVisibility(View.VISIBLE);

            nama8.setText(gNama8);
            nik8.setText(gNik8);
            statusHubungan8.setText(gStatusHubungan8);
            jenisKelamin8.setText(gJenisKelamin8);
            tempatLahir8.setText(gTempatLahir8);
            tanggalLahir8.setText(gtglLahir8);
            agama8.setText(gAgama8);
        }

        if (gNik9.equals("") && gNama9.equals("")) {
            l9.setVisibility(View.GONE);
        } else {
            l9.setVisibility(View.VISIBLE);

            nama9.setText(gNama9);
            nik9.setText(gNik9);
            statusHubungan9.setText(gStatusHubungan9);
            jenisKelamin9.setText(gJenisKelamin9);
            tempatLahir9.setText(gTempatLahir9);
            tanggalLahir9.setText(gtglLahir9);
            agama9.setText(gAgama9);
        }

        if (gNik10.equals("") && gNama10.equals("")) {
            l10.setVisibility(View.GONE);
        } else {
            l10.setVisibility(View.VISIBLE);

            nama10.setText(gNama10);
            nik10.setText(gNik10);
            statusHubungan10.setText(gStatusHubungan10);
            jenisKelamin10.setText(gJenisKelamin10);
            tempatLahir10.setText(gTempatLahir10);
            tanggalLahir10.setText(gtglLahir10);
            agama10.setText(gAgama10);
        }

//
//        byte[] mBytes = getIntent().getByteArrayExtra("iImage");
//        //now decode image because from previous activity we got our image in bytes
//        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
//
//        mNoRumahTv.setText("Rumah No."+mNoRumah);
//        mNoKkTv.setText(mNoKK);
//        mNamaKKTv.setText(mNamaKK);
//        mNikNamaKKTv.setText(mNikNamaKK);
//
//        mNamaIstriTv.setText(mNamaIstri);
//        mNikIstriTv.setText(mNikIstri);
//        mNamaAnakTv.setText(mNamaAnak);
//        mNikAnakTV.setText(mNikAnak);
//
//        mImageIv.setImageBitmap(bitmap);

    }


    private void hapusKeluargaNonAktif() {
        databaseKeluargaKeluar = FirebaseDatabase.getInstance().getReference("Keluarga_NonAktif");
        databaseKeluargaKeluar
                .child(gKeluargaNonAktifId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(KeluargaKeluarDetailActivity.this, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show();
                        onSupportNavigateUp();
                        finish();
                    }
                });

    }

}


























