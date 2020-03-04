package com.tafh.tugasakhir.keluarga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.MainWargaActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.akun.AkunDetailActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.notifications.APIService;
import com.tafh.tugasakhir.notifications.Client;
import com.tafh.tugasakhir.notifications.Data;
import com.tafh.tugasakhir.notifications.Sender;
import com.tafh.tugasakhir.notifications.Token;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDataKeluargaWargaActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = ListDataKeluargaWargaActivity.this;
    private ArrayList<ModelUser> dataUser;
    private ArrayList<ModelKeluarga> dataKeluarga;
    private TextView tvNotif, noRumah, noKK,
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

    private String noKKuser,getEmailUser, getPasswordUser, getNomorRumahUser, getNamaKepalaKeluargaUser, getNomorKKuser, setNomorKK, setNamaKK, setNikNamaKK, setNamaIstri, setNikIstri, setNamaAnak, setNikAnak, setKeluargaId;
    private String gKeluargaId, gUserId, gImageKK, gNoRumah, gNoKK,
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
    private Button btnKeluar, btnKembali;
    private FloatingActionButton btnFab;
    private FirebaseAuth auth;
    private LinearLayout LL2, l3, l4, l5, l6, l7, l8, l9, l10;
    private RelativeLayout RLFAB;
    private String CIPHERTEXT, PLAINTEXT = "RTLIMABELASRWKOSONGENAMKEMBANGANUTARA", KEY = "SYAFWAN";
    private String myUserId;
    private ActionBar actionBar;
    protected ProgressDialog pd;
    APIService apiService;

    private DatabaseReference databaseKeluarga, databaseUser;

    private String userIdAdmin;


    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_keluarga_warga);
        pd = new ProgressDialog(this);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Data Keluarga Saya");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();


        noRumah = findViewById(R.id.tv_norumah_keluarga_warga);
        noKK = findViewById(R.id.tv_no_kk_keluarga_warga);
        namaKK = findViewById(R.id.tv_nama_kk_keluarga_warga);
        nik1 = findViewById(R.id.tv_nik1_keluarga_warga);
        jenisKelamin1 = findViewById(R.id.tv_jeniskelamin1_keluarga_warga);
        tempatLahir1 = findViewById(R.id.tv_tempatlahir1_keluarga_warga);
        tanggalLahir1 = findViewById(R.id.tv_tanggallahir1_keluarga_warga);
        agama1 = findViewById(R.id.tv_agama1_keluarga_warga);

        namaIstri = findViewById(R.id.tv_nama_istri_keluarga_warga);
        nik2 = findViewById(R.id.tv_nik2_keluarga_warga);
        statusHubungan2 = findViewById(R.id.tv_statushubungan2_keluarga_warga);
        jenisKelamin2 = findViewById(R.id.tv_jeniskelamin2_keluarga_warga);
        tempatLahir2 = findViewById(R.id.tv_tempatlahir2_keluarga_warga);
        tanggalLahir2 = findViewById(R.id.tv_tanggallahir2_keluarga_warga);
        agama2 = findViewById(R.id.tv_agama2_keluarga_warga);

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

        mImageIv = findViewById(R.id.iv_ImageView_keluarga_warga);

        btnKembali = findViewById(R.id.btn_kembali_keluarga_warga);
        btnKeluar = findViewById(R.id.btn_keluar_rt_warga);
        btnFab = findViewById(R.id.fab_warga);

        l3 = findViewById(R.id.layout_anggota_keluarga3);
        l4 = findViewById(R.id.layout_anggota_keluarga4);
        l5 = findViewById(R.id.layout_anggota_keluarga5);
        l6 = findViewById(R.id.layout_anggota_keluarga6);
        l7 = findViewById(R.id.layout_anggota_keluarga7);
        l8 = findViewById(R.id.layout_anggota_keluarga8);
        l9 = findViewById(R.id.layout_anggota_keluarga9);
        l10 = findViewById(R.id.layout_anggota_keluarga10);

        LL2 = findViewById(R.id.LL_2);
        RLFAB = findViewById(R.id.RL_FAB_warga);
        tvNotif = findViewById(R.id.tv_notif);

        getDataIntent();

        getAllUser();
        getData();

        btnKembali.setOnClickListener(this);
        btnKeluar.setOnClickListener(this);
        btnFab.setOnClickListener(this);

    }

    private void getData() {
        pd.setMessage("Memuat data...");
        pd.show();


        final DatabaseReference databaseKeluarga;
        databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");
        databaseKeluarga.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataKeluarga = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelKeluarga keluarga = snapshot.getValue(ModelKeluarga.class);
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    keluarga.setKeluargaId(snapshot.getKey());
                    dataKeluarga.add(keluarga);
                }

                int n = dataKeluarga.size();

                for (int i = 0; i < n; i++) {

                    gNoKK = dataKeluarga.get(i).getNoKK();

                    if (gNoKK != null && gNoKK.equals(getNomorKKuser)) {
                        gNoKK = dataKeluarga.get(i).getNoKK();

                        gKeluargaId = dataKeluarga.get(i).getKeluargaId();
                        gUserId = dataKeluarga.get(i).getUserId();
                        gImageKK = dataKeluarga.get(i).getImageKK();
                        gNoRumah = dataKeluarga.get(i).getNoRumah();
                        gNoKK = dataKeluarga.get(i).getNoKK();

                        gNamaKK = dataKeluarga.get(i).getNamaKK();
                        gNik1 = dataKeluarga.get(i).getNik1();
                        gJenisKelamin1 = dataKeluarga.get(i).getJenisKelamin1();
                        gTempatLahir1 = dataKeluarga.get(i).getTempatLahir1();
                        gtglLahir1 = dataKeluarga.get(i).getTglLahir1();
                        gAgama1 = dataKeluarga.get(i).getAgama1();

                        gNamaIstri = dataKeluarga.get(i).getNamaIstri();
                        gNik2 = dataKeluarga.get(i).getNik2();
                        gStatusHubungan2 = dataKeluarga.get(i).getStatusHubungan2();
                        gJenisKelamin2 = dataKeluarga.get(i).getJenisKelamin2();
                        gTempatLahir2 = dataKeluarga.get(i).getTempatLahir2();
                        gtglLahir2 = dataKeluarga.get(i).getTglLahir2();
                        gAgama2 = dataKeluarga.get(i).getAgama2();

                        gNama3 = dataKeluarga.get(i).getNama3();
                        gNik3 = dataKeluarga.get(i).getNik3();
                        gStatusHubungan3 = dataKeluarga.get(i).getStatusHubungan3();
                        gJenisKelamin3 = dataKeluarga.get(i).getJenisKelamin3();
                        gTempatLahir3 = dataKeluarga.get(i).getTempatLahir3();
                        gtglLahir3 = dataKeluarga.get(i).getTglLahir3();
                        gAgama3 = dataKeluarga.get(i).getAgama3();

                        gNama4 = dataKeluarga.get(i).getNama4();
                        gNik4 = dataKeluarga.get(i).getNik4();
                        gStatusHubungan4 = dataKeluarga.get(i).getStatusHubungan4();
                        gJenisKelamin4 = dataKeluarga.get(i).getJenisKelamin4();
                        gTempatLahir4 = dataKeluarga.get(i).getTempatLahir4();
                        gtglLahir4 = dataKeluarga.get(i).getTglLahir4();
                        gAgama4 = dataKeluarga.get(i).getAgama4();

                        gNama5 = dataKeluarga.get(i).getNama5();
                        gNik5 = dataKeluarga.get(i).getNik5();
                        gStatusHubungan5 = dataKeluarga.get(i).getStatusHubungan5();
                        gJenisKelamin5 = dataKeluarga.get(i).getJenisKelamin5();
                        gTempatLahir5 = dataKeluarga.get(i).getTempatLahir5();
                        gtglLahir5 = dataKeluarga.get(i).getTglLahir5();
                        gAgama5 = dataKeluarga.get(i).getAgama5();

                        gNama6 = dataKeluarga.get(i).getNama6();
                        gNik6 = dataKeluarga.get(i).getNik6();
                        gStatusHubungan6 = dataKeluarga.get(i).getStatusHubungan6();
                        gJenisKelamin6 = dataKeluarga.get(i).getJenisKelamin6();
                        gTempatLahir6 = dataKeluarga.get(i).getTempatLahir6();
                        gtglLahir6 = dataKeluarga.get(i).getTglLahir6();
                        gAgama6 = dataKeluarga.get(i).getAgama6();

                        gNama7 = dataKeluarga.get(i).getNama7();
                        gNik7 = dataKeluarga.get(i).getNik7();
                        gStatusHubungan7 = dataKeluarga.get(i).getStatusHubungan7();
                        gJenisKelamin7 = dataKeluarga.get(i).getJenisKelamin7();
                        gTempatLahir7 = dataKeluarga.get(i).getTempatLahir7();
                        gtglLahir7 = dataKeluarga.get(i).getTglLahir7();
                        gAgama7 = dataKeluarga.get(i).getAgama7();

                        gNama8 = dataKeluarga.get(i).getNama8();
                        gNik8 = dataKeluarga.get(i).getNik8();
                        gStatusHubungan8 = dataKeluarga.get(i).getStatusHubungan8();
                        gJenisKelamin8 = dataKeluarga.get(i).getJenisKelamin8();
                        gTempatLahir8 = dataKeluarga.get(i).getTempatLahir8();
                        gtglLahir8 = dataKeluarga.get(i).getTglLahir8();
                        gAgama8 = dataKeluarga.get(i).getAgama8();

                        gNama9 = dataKeluarga.get(i).getNama9();
                        gNik9 = dataKeluarga.get(i).getNik9();
                        gStatusHubungan9 = dataKeluarga.get(i).getStatusHubungan9();
                        gJenisKelamin9 = dataKeluarga.get(i).getJenisKelamin9();
                        gTempatLahir9 = dataKeluarga.get(i).getTempatLahir9();
                        gtglLahir9 = dataKeluarga.get(i).getTglLahir9();
                        gAgama9 = dataKeluarga.get(i).getAgama9();

                        gNama10 = dataKeluarga.get(i).getNama10();
                        gNik10 = dataKeluarga.get(i).getNik10();
                        gStatusHubungan10 = dataKeluarga.get(i).getStatusHubungan10();
                        gJenisKelamin10 = dataKeluarga.get(i).getJenisKelamin10();
                        gTempatLahir10 = dataKeluarga.get(i).getTempatLahir10();
                        gtglLahir10 = dataKeluarga.get(i).getTglLahir10();
                        gAgama10 = dataKeluarga.get(i).getAgama10();


                        i=n;
                    }

                    else {
                        gNoKK = null;
                    }

                }
//                Log.w("Nilai",gNoKK+"+"+getNomorKKuser);
                if (gNoKK == null) {
                    pd.dismiss();

                    tvNotif.setVisibility(View.VISIBLE);
                    LL2.setVisibility(View.GONE);
                    RLFAB.setVisibility(View.VISIBLE);

                }
                else {
                    pd.dismiss();

                    tvNotif.setVisibility(View.GONE);
                    LL2.setVisibility(View.VISIBLE);
                    RLFAB.setVisibility(View.GONE);

                    try {
                        Picasso.with(context)
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

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();

                Toast.makeText(ListDataKeluargaWargaActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllUser() {
        final DatabaseReference databaseUsers;
        final String emailUser = auth.getCurrentUser().getEmail();

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

                int n = dataUser.size();

                //get data user
                for (int i = 0; i < n; i++) {

                    String gmail = dataUser.get(i).getGmail();

                    if (gmail.equals(emailUser)) {
                        getNomorKKuser = dataUser.get(i).getNoKK();
                        getNomorRumahUser = dataUser.get(i).getNoRumah();
                        getNamaKepalaKeluargaUser = dataUser.get(i).getNamaKepalaKeluarga();
                        getEmailUser = dataUser.get(i).getGmail();
                        getPasswordUser = dataUser.get(i).getPassword();
                        i=n;

                    }
                }
                //get data admin
                for (int i=0; i<n; i++) {
                    String userType = dataUser.get(i).getUserType();

                    if (userType.equals("admin")) {
                        userIdAdmin = dataUser.get(i).getUserid();

                    }
                }

//                Log.w("Nilai", ""+userIdAdmin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataIntent() {
        Intent intent = new Intent(getIntent());
        myUserId = intent.getStringExtra("MY_USERID");

//        Log.w("Nilai", ""+myUserId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kembali_keluarga_warga:
                onSupportNavigateUp();
                finish();
                break;
            case R.id.btn_keluar_rt_warga:
                notify = true;
                if (notify) {
                    dialogKonfirmasi();
                }
                notify = false;
                break;
            case R.id.fab_warga:
//                Intent intent = new Intent(ListDataKeluargaWargaActivity.this, TambahDataKeluargaWargaActivity.class);
//                intent.putExtra("NO_KK", getNomorKKuser);
//                intent.putExtra("NO_RUMAH_USER", getNomorRumahUser);
//                intent.putExtra("NAMA_KK_USER", getNamaKepalaKeluargaUser);
//                startActivity(intent);
                fabScanQR();
                break;
        }
    }

    private void dialogKonfirmasi() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        String hisUserId = userIdAdmin; //userid admin penerima pesan
                        String message = "Sudah pindah tempat tinggal"; //pesan
                        sendNotification(hisUserId, getNamaKepalaKeluargaUser, message);
//                        Log.w("nilai", ""+userIdAdmin);
                        hapusDataKeluarga();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Apakah Anda yakin?").setPositiveButton("Ya", dialogClickListener)
                .setNegativeButton("Tidak", dialogClickListener).show();
    }

    private void sendNotification(final String hisUserId, final String nama, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUserId, "Warga di rumah No "+getNomorRumahUser+" "+message,
                            "Warga non-aktif",
                            hisUserId, getNomorKKuser, R.drawable.ic_notif);
                    Sender sender = new Sender(data, token.getToken());
//                    final String Mtoken = token.getToken();
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.w("PESAN","Notifikasi terkirim.");
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.w("PESAN","Gagal.");
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fabScanQR() {
        IntentIntegrator integrator = new IntentIntegrator(ListDataKeluargaWargaActivity.this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Pemindaian");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
//                Intent intent = new Intent(ListDataKeluargaWargaActivity.this, TambahDataKeluargaWargaActivity.class);
//                intent.putExtra("NO_KK", getNomorKKuser);
//                intent.putExtra("NO_RUMAH_USER", getNomorRumahUser);
//                intent.putExtra("NAMA_KK_USER", getNamaKepalaKeluargaUser);
//                startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {
            //decode ciphertext nya
            final String text = result.getContents();
            final String key = KEY;

            decrypt(text, key);

            if (CIPHERTEXT.equals(PLAINTEXT)) {
                Intent intent = new Intent(ListDataKeluargaWargaActivity.this, TambahDataKeluargaWargaActivity.class);
                intent.putExtra("NO_KK", getNomorKKuser);
                intent.putExtra("NO_RUMAH_USER", getNomorRumahUser);
                intent.putExtra("NAMA_KK_USER", getNamaKepalaKeluargaUser);
                startActivity(intent);
            } else {
                Toast.makeText(ListDataKeluargaWargaActivity.this, "Maaf Kode Tidak Cocok", Toast.LENGTH_LONG).show();
                onSupportNavigateUp();
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

    private void decrypt(String text1, String key1) {
        String res1 = "";
        text1 = text1.toUpperCase();
        key1 = key1.toUpperCase();
        int j = 0;
        for (int i = 0; i < text1.length(); i++) {
            char c = text1.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                res1 = new StringBuilder(String.valueOf(res1)).append((char) ((((c - key1.charAt(j)) + 26) % 26) + 65)).toString();
                j = (j + 1) % key1.length();
            }
        }
        CIPHERTEXT = res1;
    }

    private void hapusDataKeluarga() {
        pd.setMessage("Tunggu sebentar...");
        pd.show();

        final FirebaseUser user = auth.getCurrentUser();

        final String userId = myUserId;
//                    ModelUser akun = new ModelUser();
//                    String keyID = mKey;
//                    String userID = auth.getUid();
        String password = getPasswordUser;
        String email = getEmailUser;
        Log.w("nilai",""+email+" "+password);
        final AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        //for post-image name, post-id, post-publish-time
        String filePathAndName = "Users/" + "user_profile_" + userId;

        databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        if (gKeluargaId == null) {
            onSupportNavigateUp();
            Toast.makeText(context, "Silahkan Ulangi", Toast.LENGTH_SHORT).show();
        }
        else {

            //pindahkan data ke tabel keluargakeluar
            moveDataToKeluargaKeluar();

            // Delete foto sebelumnya
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileref = storageRef.child("Users/user_profile_" + userId);
            profileref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
            Log.w("HASIL","1");
            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Log.w("HASIL","2");
                        databaseKeluarga.child(gKeluargaId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //database keluarga terhapus

                                        Log.w("HASIL","3");
                                        databaseUser.child(userId).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //database user terhapus

                                                        Log.w("HASIL","4");
                                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    checkUserStatus();
                                                                    Log.w("HASIL","5");
                                                                    //akun gmail dan password user terhapus dari firebase
                                                                    pd.dismiss();
                                                                    Toast.makeText(context, "Anda sudah bukan warga RT15.", Toast.LENGTH_SHORT).show();

                                                                    Log.w("HASIL","6");
//                                                                    startActivity(new Intent(ListDataKeluargaWargaActivity.this, LoginGmailActivity.class));

                                                                    Log.w("HASIL","7");
                                                                    finish();
                                                                }

                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });

                    }
                    else {
                        pd.dismiss();

                        Log.w("HASIL","0");
                        onSupportNavigateUp();
                        Toast.makeText(ListDataKeluargaWargaActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }

    }
    private void moveDataToKeluargaKeluar() {
        DatabaseReference databaseKeluargaKeluar = FirebaseDatabase.getInstance().getReference("Keluarga_NonAktif");
        String id = databaseKeluargaKeluar.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("keluargaNonAktifId", id);
        hashMap.put("keluargaId", gKeluargaId);
        hashMap.put("userId", gUserId);
        hashMap.put("imageKK", gImageKK) ;
        hashMap.put("noRumah", gNoRumah);
        hashMap.put("noKK", gNoKK);

        hashMap.put("namaKK", gNamaKK);
        hashMap.put("nik1", gNik1);
        hashMap.put("jenisKelamin1", gJenisKelamin1);
        hashMap.put("tempatLahir1", gTempatLahir1);
        hashMap.put("tglLahir1", gtglLahir1);
        hashMap.put("agama1", gAgama1);

        hashMap.put("namaIstri", gNamaIstri);
        hashMap.put("nik2", gNik2);
        hashMap.put("statusHubungan2", gStatusHubungan2);
        hashMap.put("jenisKelamin2", gJenisKelamin2);
        hashMap.put("tempatLahir2", gTempatLahir2);
        hashMap.put("tglLahir2", gtglLahir2);
        hashMap.put("agama2", gAgama2);

        hashMap.put("nama3", gNama3);
        hashMap.put("nik3", gNik3);
        hashMap.put("statusHubungan3", gStatusHubungan3);
        hashMap.put("jenisKelamin3", gJenisKelamin3);
        hashMap.put("tempatLahir3", gTempatLahir3);
        hashMap.put("tglLahir3", gtglLahir3);
        hashMap.put("agama3", gAgama3);

        hashMap.put("nama4", gNama4);
        hashMap.put("nik4", gNik4);
        hashMap.put("statusHubungan4", gStatusHubungan4);
        hashMap.put("jenisKelamin4", gJenisKelamin4);
        hashMap.put("tempatLahir4", gTempatLahir4);
        hashMap.put("tglLahir4", gtglLahir4);
        hashMap.put("agama4", gAgama4);

        hashMap.put("nama5", gNama5);
        hashMap.put("nik5", gNik5);
        hashMap.put("statusHubungan5", gStatusHubungan5);
        hashMap.put("jenisKelamin5", gJenisKelamin5);
        hashMap.put("tempatLahir5", gTempatLahir5);
        hashMap.put("tglLahir5", gtglLahir5);
        hashMap.put("agama5", gAgama5);

        hashMap.put("nama6", gNama6);
        hashMap.put("nik6", gNik6);
        hashMap.put("statusHubungan6", gStatusHubungan6);
        hashMap.put("jenisKelamin6", gJenisKelamin6);
        hashMap.put("tempatLahir6", gTempatLahir6);
        hashMap.put("tglLahir6", gtglLahir6);
        hashMap.put("agama6", gAgama6);

        hashMap.put("nama7", gNama7);
        hashMap.put("nik7", gNik7);
        hashMap.put("statusHubungan7", gStatusHubungan7);
        hashMap.put("jenisKelamin7", gJenisKelamin7);
        hashMap.put("tempatLahir7", gTempatLahir7);
        hashMap.put("tglLahir7", gtglLahir7);
        hashMap.put("agama7", gAgama7);

        hashMap.put("nama8", gNama8);
        hashMap.put("nik8", gNik8);
        hashMap.put("statusHubungan8", gStatusHubungan8);
        hashMap.put("jenisKelamin8", gJenisKelamin8);
        hashMap.put("tempatLahir8", gTempatLahir8);
        hashMap.put("tglLahir8", gtglLahir8);
        hashMap.put("agama8", gAgama8);

        hashMap.put("nama9", gNama9);
        hashMap.put("nik9", gNik9);
        hashMap.put("statusHubungan9", gStatusHubungan9);
        hashMap.put("jenisKelamin9", gJenisKelamin9);
        hashMap.put("tempatLahir9", gTempatLahir9);
        hashMap.put("tglLahir9", gtglLahir9);
        hashMap.put("agama9", gAgama9);

        hashMap.put("nama10", gNama10);
        hashMap.put("nik10", gNik10);
        hashMap.put("statusHubungan10", gStatusHubungan10);
        hashMap.put("jenisKelamin10", gJenisKelamin10);
        hashMap.put("tempatLahir10", gTempatLahir10);
        hashMap.put("tglLahir10", gtglLahir10);
        hashMap.put("agama10", gAgama10);


        //we will store the additional fields in firebase database

        databaseKeluargaKeluar.child(id).setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //DATA SUDAH PINDAH KE DATA KELUARGA KELUAR
                        }
                    }
                });
    }

    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null ){
//            Toast.makeText(getContext(), "userid : "+userId,Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(this, LoginGmailActivity.class));
            this.finish();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }
}
