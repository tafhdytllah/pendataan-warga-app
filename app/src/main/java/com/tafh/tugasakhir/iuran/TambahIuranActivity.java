package com.tafh.tugasakhir.iuran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaActivity;
import com.tafh.tugasakhir.keluarga.TambahDataKeluargaActivity;
import com.tafh.tugasakhir.model.ModelIuran;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

public class TambahIuranActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText judulIuran, jumlahIuran, pembayaranIuran, tanggalIuran;
    private TextView noRumahIuran, noKKiuran, namaKKiuran;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseIuran;
    private ProgressBar progressBar;
    private String noKKUser, noRumahUser, keluargaId;

    private MaterialSpinner spinnerJenisPembayran;
    private Calendar myCalender = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    private List<String> jenisPembayaranList;
    private ArrayAdapter<String> adapterJenisPembayaran;
    private String item, jenisPembayaran, namaKK;
    private Button btn_simpan, btn_batal;
    ActionBar actionBar;
    //progress bar
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_iuran);

        pd = new ProgressDialog(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Pembayaran Iuran");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

//        progressBar = findViewById(R.id.progress_bar_tambah_iuran);

        Intent getIntent = getIntent();
        namaKK = getIntent.getStringExtra("NAMAKK");
//        Log.w("Nilai",""+namaKK);
        keluargaId = getIntent.getStringExtra("KELUARGAID");
        noKKUser = getIntent.getStringExtra("MNOKK");
        noRumahUser = getIntent.getStringExtra("MNORUMAH");

        namaKKiuran = findViewById(R.id.tv_nama_kk_iuran);
        noRumahIuran = findViewById(R.id.tv_norumah_iuran);
        noKKiuran = findViewById(R.id.tv_no_kk_iuran);

        judulIuran = findViewById(R.id.et_judul_iuran);
        jumlahIuran = findViewById(R.id.et_jumlah_iuran);
        jumlahIuran.addTextChangedListener(onTextChangedListener());
        spinnerJenisPembayran = findViewById(R.id.spinner_pembayaran_iuran);
        tanggalIuran = findViewById(R.id.et_tgl_iuran);

        btn_simpan = findViewById(R.id.btn_simpan_tambah_iuran);
        btn_batal = findViewById(R.id.btn_batal_tambah_iuran);

        namaKKiuran.setText(namaKK);
        noRumahIuran.setText(noRumahUser);
        noKKiuran.setText(noKKUser);


        layoutSpinner();
        layoutCalender();

        tanggalIuran.setOnClickListener(this);
        btn_simpan.setOnClickListener(this);
        btn_batal.setOnClickListener(this);

    }
    private void layoutCalender() {

        String myFormat = "dd/MM/yyyy"; //in which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, monthOfYear);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender.getTime());

                tanggalIuran.setText(tanggal);

            }
        };

    }

    private void layoutSpinner() {
        getJenisPembayaran();

        // Spinner Drop down elements
        adapterJenisPembayaran = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisPembayaranList);
        // Drop down layout style - list view with radio button
        adapterJenisPembayaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner

        spinnerJenisPembayran.setAdapter(adapterJenisPembayaran);

        spinnerJenisPembayran.setOnItemSelectedListener(this);


    }

    private void getJenisPembayaran() {
        jenisPembayaranList = new ArrayList<String>();
        jenisPembayaranList.add("Tunai");
        jenisPembayaranList.add("Non-Tunai");
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                jumlahIuran.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    jumlahIuran.setText(formattedString);
                    jumlahIuran.setSelection(jumlahIuran.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                jumlahIuran.addTextChangedListener(onTextChangedListener());
            }
        };
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_simpan_tambah_iuran:
                //tambah data iuran.
                addIuran();

                break;
            case R.id.btn_batal_tambah_iuran:
                onSupportNavigateUp();
                break;

            case R.id.et_tgl_iuran:
                new DatePickerDialog(TambahIuranActivity.this, date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void addIuran() {
        final String getJudulIuran = judulIuran.getText().toString().trim();
        final String getJumlahIuran = jumlahIuran.getText().toString().trim();
        final String getJenisPembayaran = jenisPembayaran.trim();
        final String getTanggalIuran = tanggalIuran.getText().toString().trim();

        if (TextUtils.isEmpty(getJudulIuran)) {
            judulIuran.setError("Judul tidak boleh kosong!");
            judulIuran.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getJumlahIuran)) {
            jumlahIuran.setError("Nominal tidak boleh kosong!");
            jumlahIuran.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getJenisPembayaran ) || getJenisPembayaran.equals("Jenis Pembayaran")) {
            ((TextView)spinnerJenisPembayran.getSelectedView()).setError("Error message");
            spinnerJenisPembayran.setError("Pilih Jenis Pembayaran!");
            spinnerJenisPembayran.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(getTanggalIuran)) {
            Toast.makeText(this, "Pilih Tanggal!", Toast.LENGTH_SHORT).show();
            tanggalIuran.requestFocus();
            return;
        }
        pd.setMessage("Menambahkan data keluarga...");
        pd.show();

//
//        Log.w("Nilai", ""+getJudulIuran);
//        Log.w("Nilai", ""+getJumlahIuran);
//        Log.w("Nilai", ""+getJenisPembayaran);
//        Log.w("Nilai", ""+getTanggalIuran);


        //referen untuk tabel Keluarga
        databaseIuran = FirebaseDatabase.getInstance().getReference("Iuran");
        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("iuranId", timestamp);
        hashMap.put("keluargaId", keluargaId);
        hashMap.put("noRumah", noRumahUser);
        hashMap.put("noKK", noKKUser);
        hashMap.put("namaKK", namaKK);
        hashMap.put("judulIuran", getJudulIuran);
        hashMap.put("jumlahIuran", getJumlahIuran);
        hashMap.put("pembayaranIuran", getJenisPembayaran);
        hashMap.put("tanggalIuran", getTanggalIuran);

        databaseIuran.child(keluargaId).child(timestamp).setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()){
                            pd.dismiss();
//                            judulIuran.setText("");
//                            jumlahIuran.setText("");
//                            pembayaranIuran.setText("");
//                            tanggalIuran.setText("");

                            Toast.makeText(TambahIuranActivity.this, "Berhasil menambah data iuran",Toast.LENGTH_LONG).show();
                            onSupportNavigateUp();
                            finish();
                        } else {
                            pd.dismiss();
                            //display failure message
                            Toast.makeText(TambahIuranActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        item = adapterView.getItemAtPosition(position).toString();

        switch (adapterView.getId()) {

            case R.id.spinner_pembayaran_iuran:
                jenisPembayaran = item;
//                Log.w("NILAI", ""+jenisPembayaran);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
