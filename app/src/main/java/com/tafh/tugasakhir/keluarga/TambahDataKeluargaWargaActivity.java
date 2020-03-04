package com.tafh.tugasakhir.keluarga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.akun.ListDataAkunActivity;
import com.tafh.tugasakhir.akun.TambahDataAkunActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

public class TambahDataKeluargaWargaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ConstraintLayout expendableView1, expendableView2, expendableView3, expendableView4, expendableView5, expendableView6, expendableView7, expendableView8;
    private Button arrowBtn1, arrowBtn2, arrowBtn3, arrowBtn4, arrowBtn5, arrowBtn6, arrowBtn7, arrowBtn8;
    private CardView cardView;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;

    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constats
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //image picked will be samed in this uri
    private Uri image_uri = null;

    private EditText noKK,
            namaKK, nik1, tempatLahir1, tglLahir1, agama1,
            namaIstri, nik2, statusHubungan2, tempatLahir2, tglLahir2, agama2,
            nama3, nik3, statusHubungan3, tempatLahir3, tglLahir3, agama3,
            nama4, nik4, statusHubungan4, tempatLahir4, tglLahir4, agama4,
            nama5, nik5, statusHubungan5, tempatLahir5, tglLahir5, agama5,
            nama6, nik6, statusHubungan6, tempatLahir6, tglLahir6, agama6,
            nama7, nik7, statusHubungan7, tempatLahir7, tglLahir7, agama7,
            nama8, nik8, statusHubungan8, tempatLahir8, tglLahir8, agama8,
            nama9, nik9, statusHubungan9, tempatLahir9, tglLahir9, agama9,
            nama10, nik10, statusHubungan10, tempatLahir10, tglLahir10, agama10;


    private Button btn_simpan, btn_batal;
    private ImageView imageKK;

    private MaterialSpinner spinnerNoRumah, spinnerjenisKelamin1, spinnerjenisKelamin2,
            spinnerjenisKelamin3, spinnerjenisKelamin4, spinnerjenisKelamin5, spinnerjenisKelamin6, spinnerjenisKelamin7, spinnerjenisKelamin8, spinnerjenisKelamin9, spinnerjenisKelamin10;
    private Calendar
            myCalender = Calendar.getInstance(), myCalender2 = Calendar.getInstance(),
            myCalender3 = Calendar.getInstance(),
            myCalender4 = Calendar.getInstance(),
            myCalender5 = Calendar.getInstance(),
            myCalender6 = Calendar.getInstance(),
            myCalender7 = Calendar.getInstance(),
            myCalender8 = Calendar.getInstance(),
            myCalender9 = Calendar.getInstance(),
            myCalender10 = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date, date2, date3, date4, date5, date6, date7, date8, date9, date10;

    private ArrayList<ModelUser> userList;

    private ArrayAdapter<String> adapterNoRumah;
    private ArrayAdapter<String> adapterJenisKelamin;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseKeluarga;

    private String item, noKKuser, noRumahUser, namaKKuser, noRumahSelect, noRumah, jenisKelamin1, jeniskelamin2, setIdUserSelect, setImageUserSelect
            , jenisKelamin3, jenisKelamin4, jenisKelamin5, jenisKelamin6, jenisKelamin7, jenisKelamin8, jenisKelamin9, jenisKelamin10;

    private ActionBar actionBar;
    private ProgressDialog pd;

    private Context context = TambahDataKeluargaWargaActivity.this;

    private String jenisK1, tglL1, jenisK2, tglL2, jenisK3, tglL3, jenisK4, tglL4, jenisK5, tglL5
            , jenisK6, tglL6, jenisK7, tglL7, jenisK8, tglL8, jenisK9, tglL9, jenisK10, tglL10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_keluarga_warga);

        pd = new ProgressDialog(this);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Data Keluarga");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        expendableView1 = findViewById(R.id.expandableView1);
        expendableView2 = findViewById(R.id.expandableView2);
        expendableView3 = findViewById(R.id.expandableView3);
        expendableView4 = findViewById(R.id.expandableView4);

        expendableView5 = findViewById(R.id.expandableView5);
        expendableView6 = findViewById(R.id.expandableView6);
        expendableView7 = findViewById(R.id.expandableView7);
        expendableView8 = findViewById(R.id.expandableView8);

        arrowBtn1 = findViewById(R.id.arrowBtn1);
        arrowBtn2 = findViewById(R.id.arrowBtn2);
        arrowBtn3 = findViewById(R.id.arrowBtn3);
        arrowBtn4 = findViewById(R.id.arrowBtn4);

        arrowBtn5 = findViewById(R.id.arrowBtn5);
        arrowBtn6 = findViewById(R.id.arrowBtn6);
        arrowBtn7 = findViewById(R.id.arrowBtn7);
        arrowBtn8 = findViewById(R.id.arrowBtn8);

        cardView = findViewById(R.id.cardView);


        spinnerNoRumah = findViewById(R.id.spinner_norumah_warga);
        noKK = findViewById(R.id.et_no_kk_warga);
        imageKK = findViewById(R.id.iv_image_tambahkk_warga);
        namaKK = findViewById(R.id.et_nama_kk_warga);
        nik1 = findViewById(R.id.et_nik_1_warga);
        spinnerjenisKelamin1 = findViewById(R.id.spinner_jeniskelamin_1_warga);
        tempatLahir1 = findViewById(R.id.et_tempat_lahir_1_warga);
        tglLahir1 = findViewById(R.id.et_tgl_lahir_1_warga);
        agama1 = findViewById(R.id.et_agama_1_warga);

        namaIstri = findViewById(R.id.et_nama_istri_warga);
        nik2 = findViewById(R.id.et_nik_2_warga);
        statusHubungan2 = findViewById(R.id.et_status_hubungan_2_warga);
        spinnerjenisKelamin2 = findViewById(R.id.spinner_jeniskelamin_2_warga);
        tempatLahir2 = findViewById(R.id.et_tempat_lahir_2_warga);
        tglLahir2 = findViewById(R.id.et_tgl_lahir_2_warga);
        agama2 = findViewById(R.id.et_agama_2_warga);

        nama3 = findViewById(R.id.et_nama_anggota1_warga);
        nama4 = findViewById(R.id.et_nama_anggota2_warga);
        nama5 = findViewById(R.id.et_nama_anggota3_warga);
        nama6 = findViewById(R.id.et_nama_anggota4_warga);
        nama7 = findViewById(R.id.et_nama_anggota5_warga);
        nama8 = findViewById(R.id.et_nama_anggota6_warga);
        nama9 = findViewById(R.id.et_nama_anggota7_warga);
        nama10 = findViewById(R.id.et_nama_anggota8_warga);

        nik3 = findViewById(R.id.et_nik_anggota1_warga);
        nik4 = findViewById(R.id.et_nik_anggota2_warga);
        nik5 = findViewById(R.id.et_nik_anggota3_warga);
        nik6 = findViewById(R.id.et_nik_anggota4_warga);
        nik7 = findViewById(R.id.et_nik_anggota5_warga);
        nik8 = findViewById(R.id.et_nik_anggota6_warga);
        nik9 = findViewById(R.id.et_nik_anggota7_warga);
        nik10 = findViewById(R.id.et_nik_anggota8_warga);

        statusHubungan3 = findViewById(R.id.et_status_hubungan_anggota1_warga);
        statusHubungan4 = findViewById(R.id.et_status_hubungan_anggota2_warga);
        statusHubungan5 = findViewById(R.id.et_status_hubungan_anggota3_warga);
        statusHubungan6 = findViewById(R.id.et_status_hubungan_anggota4_warga);
        statusHubungan7 = findViewById(R.id.et_status_hubungan_anggota5_warga);
        statusHubungan8 = findViewById(R.id.et_status_hubungan_anggota6_warga);
        statusHubungan9 = findViewById(R.id.et_status_hubungan_anggota7_warga);
        statusHubungan10 = findViewById(R.id.et_status_hubungan_anggota8_warga);

        spinnerjenisKelamin3 = findViewById(R.id.spinner_jeniskelamin_anggota1_warga);
        spinnerjenisKelamin4 = findViewById(R.id.spinner_jeniskelamin_anggota2_warga);
        spinnerjenisKelamin5 = findViewById(R.id.spinner_jeniskelamin_anggota3_warga);
        spinnerjenisKelamin6 = findViewById(R.id.spinner_jeniskelamin_anggota4_warga);
        spinnerjenisKelamin7 = findViewById(R.id.spinner_jeniskelamin_anggota5_warga);
        spinnerjenisKelamin8 = findViewById(R.id.spinner_jeniskelamin_anggota6_warga);
        spinnerjenisKelamin9 = findViewById(R.id.spinner_jeniskelamin_anggota7_warga);
        spinnerjenisKelamin10 = findViewById(R.id.spinner_jeniskelamin_anggota8_warga);

        tempatLahir3 = findViewById(R.id.et_tempat_lahir_anggota1_warga);
        tempatLahir4 = findViewById(R.id.et_tempat_lahir_anggota2_warga);
        tempatLahir5 = findViewById(R.id.et_tempat_lahir_anggota3_warga);
        tempatLahir6 = findViewById(R.id.et_tempat_lahir_anggota4_warga);
        tempatLahir7 = findViewById(R.id.et_tempat_lahir_anggota5_warga);
        tempatLahir8 = findViewById(R.id.et_tempat_lahir_anggota6_warga);
        tempatLahir9 = findViewById(R.id.et_tempat_lahir_anggota7_warga);
        tempatLahir10 = findViewById(R.id.et_tempat_lahir_anggota8_warga);

        tglLahir3 = findViewById(R.id.et_tgl_lahir_anggota1_warga);
        tglLahir4 = findViewById(R.id.et_tgl_lahir_anggota2_warga);
        tglLahir5 = findViewById(R.id.et_tgl_lahir_anggota3_warga);
        tglLahir6 = findViewById(R.id.et_tgl_lahir_anggota4_warga);
        tglLahir7 = findViewById(R.id.et_tgl_lahir_anggota5_warga);
        tglLahir8 = findViewById(R.id.et_tgl_lahir_anggota6_warga);
        tglLahir9 = findViewById(R.id.et_tgl_lahir_anggota7_warga);
        tglLahir10 = findViewById(R.id.et_tgl_lahir_anggota8_warga);

        agama3 = findViewById(R.id.et_agama_anggota1_warga);
        agama4 = findViewById(R.id.et_agama_anggota2_warga);
        agama5 = findViewById(R.id.et_agama_anggota3_warga);
        agama6 = findViewById(R.id.et_agama_anggota4_warga);
        agama7 = findViewById(R.id.et_agama_anggota5_warga);
        agama8 = findViewById(R.id.et_agama_anggota6_warga);
        agama9 = findViewById(R.id.et_agama_anggota7_warga);
        agama10 = findViewById(R.id.et_agama_anggota8_warga);


        btn_simpan = findViewById(R.id.btn_simpan_warga);
        btn_batal = findViewById(R.id.btn_batal_warga);
//        noKK.setText(noKKuser);
//        nor.setText(noRumahUser);
//        namaKepalaKeluarga.setText(namaKKuser);

        getDataIntent();
        Log.w("HASIL", " "+noRumahUser+
                " + "+noKKuser+" + "+ namaKKuser);
        layoutSpinner();

        namaKK.setText(namaKKuser);
        noKK.setText(noKKuser);
        disableEditText(namaKK);
        disableEditText(noKK);

        layoutCalender();

        //get image from camera/gallery on click
        imageKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show image pick dialog
                showImagePickDialog();
            }
        });

        tglLahir1.setOnClickListener(this);
        tglLahir2.setOnClickListener(this);

        tglLahir3.setOnClickListener(this);
        tglLahir4.setOnClickListener(this);
        tglLahir5.setOnClickListener(this);
        tglLahir6.setOnClickListener(this);
        tglLahir7.setOnClickListener(this);
        tglLahir8.setOnClickListener(this);
        tglLahir9.setOnClickListener(this);
        tglLahir10.setOnClickListener(this);

        arrowBtn1.setOnClickListener(this);
        arrowBtn2.setOnClickListener(this);
        arrowBtn3.setOnClickListener(this);
        arrowBtn4.setOnClickListener(this);

        arrowBtn5.setOnClickListener(this);
        arrowBtn6.setOnClickListener(this);
        arrowBtn7.setOnClickListener(this);
        arrowBtn8.setOnClickListener(this);

        btn_simpan.setOnClickListener(this);
        btn_batal.setOnClickListener(this);
    }

    //method image and camera
    private void showImagePickDialog() {
        //options(camera, gallery) to show in dialog
        String[] options = {"Camera", "Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Gambar menggunakan");
        //set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //item click handle
                if (i == 0) {
                    //camera clicked
                    //need to check permissions first
                    if (!checkCameraPermission()) {

                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                if (i == 1) {
                    //gallery clicked
                    if (!checkStoragePermission()) {

                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //request runtime Camera permission
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking camera

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery,get uri of image
                image_uri = data.getData();

                //set to imageview
                imageKK.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                imageKK.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //method calender
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

                tglLahir1.setText(tanggal);

            }
        };
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender2.set(Calendar.YEAR, year);
                myCalender2.set(Calendar.MONTH, monthOfYear);
                myCalender2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender2.getTime());
                tglLahir2.setText(tanggal);


            }
        };
        date3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender3.set(Calendar.YEAR, year);
                myCalender3.set(Calendar.MONTH, monthOfYear);
                myCalender3.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender3.getTime());
                tglLahir3.setText(tanggal);


            }
        };
        date4 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender4.set(Calendar.YEAR, year);
                myCalender4.set(Calendar.MONTH, monthOfYear);
                myCalender4.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender4.getTime());
                tglLahir4.setText(tanggal);


            }
        };
        date5 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender5.set(Calendar.YEAR, year);
                myCalender5.set(Calendar.MONTH, monthOfYear);
                myCalender5.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender5.getTime());
                tglLahir5.setText(tanggal);


            }
        };
        date6 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender6.set(Calendar.YEAR, year);
                myCalender6.set(Calendar.MONTH, monthOfYear);
                myCalender6.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender6.getTime());
                tglLahir6.setText(tanggal);


            }
        };
        date7 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender7.set(Calendar.YEAR, year);
                myCalender7.set(Calendar.MONTH, monthOfYear);
                myCalender7.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender7.getTime());
                tglLahir7.setText(tanggal);


            }
        };
        date8 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender8.set(Calendar.YEAR, year);
                myCalender8.set(Calendar.MONTH, monthOfYear);
                myCalender8.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender8.getTime());
                tglLahir8.setText(tanggal);


            }
        };
        date9 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender9.set(Calendar.YEAR, year);
                myCalender9.set(Calendar.MONTH, monthOfYear);
                myCalender9.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender9.getTime());
                tglLahir9.setText(tanggal);


            }
        };
        date10 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalender10.set(Calendar.YEAR, year);
                myCalender10.set(Calendar.MONTH, monthOfYear);
                myCalender10.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String tanggal = sdf.format(myCalender10.getTime());
                tglLahir10.setText(tanggal);


            }
        };

    }

    //method spinner dan jenis kelamin dan nomor rumah

    private void layoutSpinner() {
        getallNoRumah();

        getJenisKelamin();

    }

    private void getJenisKelamin() {

        final List<String> jenisKelaminList;
        jenisKelaminList = new ArrayList<String>();
        jenisKelaminList.add("Laki-Laki");
        jenisKelaminList.add("Perempuan");

        // Spinner Drop down elements
        adapterJenisKelamin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisKelaminList);
        // Drop down layout style - list view with radio button
        adapterJenisKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner

        spinnerjenisKelamin1.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin2.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin3.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin4.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin5.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin6.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin7.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin8.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin9.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin10.setAdapter(adapterJenisKelamin);

        spinnerjenisKelamin1.setOnItemSelectedListener(this);
        spinnerjenisKelamin2.setOnItemSelectedListener(this);
        spinnerjenisKelamin3.setOnItemSelectedListener(this);
        spinnerjenisKelamin4.setOnItemSelectedListener(this);
        spinnerjenisKelamin5.setOnItemSelectedListener(this);
        spinnerjenisKelamin6.setOnItemSelectedListener(this);
        spinnerjenisKelamin7.setOnItemSelectedListener(this);
        spinnerjenisKelamin8.setOnItemSelectedListener(this);
        spinnerjenisKelamin9.setOnItemSelectedListener(this);
        spinnerjenisKelamin10.setOnItemSelectedListener(this);

//        ArrayList<String> list = new ArrayList<String>() {{
//            add("item1");
//            add("item2");
//            add("item3");
//        }};
//
//        ArrayList<String> list1 = new ArrayList<String>();
//        list1.add("item1");
//        list1.add("item2");
//        list1.add("item3");
    }

    private void getallNoRumah() {
        userList = new ArrayList<>();
        userList.clear();


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

                for(int i=0;i<n;i++)
                {
                    final String noRUmah = userList.get(i).getNoRumah();

                    noRumahList.add(noRUmah);

                }
                Log.w("Hasil", ""+noRumahList);

                layoutNoRumah(noRumahList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void layoutNoRumah(List<String> listNoRumah) {
        adapterNoRumah = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listNoRumah);
        adapterNoRumah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNoRumah.setAdapter(adapterNoRumah);
        Log.w("Hasil", ""+noRumahUser);
        spinnerNoRumah.setSelection(selectedSpinnerNoRumah(listNoRumah, noRumahUser));
        spinnerNoRumah.setEnabled(false);
        spinnerNoRumah.setFocusable(false);

        spinnerNoRumah.setOnItemSelectedListener(this);

    }

    private int selectedSpinnerNoRumah(List<String> listNoRumah, String string) {
        int selectedSpinner = 0;
        for (int i=0; i<listNoRumah.size(); i++){
            if (listNoRumah.get(i).equals(string)) {
//                Log.w(TAG, "y : "+ listJenisKelamin.get(i));
                selectedSpinner = i+1;
            }
        }
        return selectedSpinner;
//        Log.w(TAG, "x : "+selectedSpinner);
    }

    private void getallakun() {
//        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//        final String getEmailUser = fUser.getEmail();

        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");

        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                userList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    user.setUserid(snapshot.getKey());
                    userList.add(user);
                }

                int n = userList.size();
                for (int i=0; i<n; i++) {

                    String nRumah = userList.get(i).getNoRumah();
                    String setNama, setNoKK, setNoRumah;

                    if (nRumah.equals(noRumahSelect)) {
                        setIdUserSelect = userList.get(i).getUserid();
                        setImageUserSelect = userList.get(i).getImg();
                        setNama = userList.get(i).getNamaKepalaKeluarga();
                        setNoKK = userList.get(i).getNoKK();
                        setNoRumah = userList.get(i).getNoRumah();
//                        coba.setText(dataUsers.get(i).getUserid());

                        namaKK.setText(setNama);
                        noKK.setText(setNoKK);
                        noRumah = setNoRumah;

                        disableEditText(namaKK);
                        disableEditText(noKK);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        item = adapterView.getItemAtPosition(position).toString();


        switch (adapterView.getId()) {
            case R.id.spinner_norumah_warga:
                noRumahSelect = item;
                if (!noRumahSelect.equals("No Rumah")) {
                    getallakun();
                } else {
                    noRumah = item;
                }
                break;
            case R.id.spinner_jeniskelamin_1_warga:
                jenisKelamin1 = item;
                break;
            case R.id.spinner_jeniskelamin_2_warga:
                jeniskelamin2 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota1_warga:
                jenisKelamin3 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota2_warga:
                jenisKelamin4 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota3_warga:
                jenisKelamin5 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota4_warga:
                jenisKelamin6 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota5_warga:
                jenisKelamin7 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota6_warga:
                jenisKelamin8 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota7_warga:
                jenisKelamin9 = item;
                break;
            case R.id.spinner_jeniskelamin_anggota8_warga:
                jenisKelamin10 = item;
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        switch (adapterView.getId()) {
//            case R.id.spinner_norumah:
//                spinnerNoRumah.setError("Pilih No Rumah");
//                spinnerNoRumah.requestFocus();
//                break;
//            case R.id.spinner_jeniskelamin_1:
//                spinnerjenisKelamin1.setError("Pilih Jenis Kelamin");
//                spinnerjenisKelamin1.requestFocus();
//                break;
//            case R.id.spinner_jeniskelamin_2:
//                jeniskelamin2 = item;
//                spinnerjenisKelamin2.setError("Pilih Jenis Kelamin");
//                spinnerjenisKelamin2.requestFocus();
//                break;
//        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setTextColor(getResources().getColor(R.color.textGrey));
    }


    private void getDataIntent() {

        Intent intent = getIntent();
        noKKuser = intent.getStringExtra("NO_KK");
        noRumahUser = intent.getStringExtra("NO_RUMAH_USER");
        namaKKuser = intent.getStringExtra("NAMA_KK_USER");
        Log.w("NORUMAH", ""+noRumahUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_simpan_warga:
                //tambah data user kemudian di registerkan gmail nya.
                Log.w("Nilai", "KLIK");
                addKeluarga();
                break;

            case R.id.btn_batal_warga:
                onSupportNavigateUp();
                break;

            case R.id.et_tgl_lahir_1_warga:
                new DatePickerDialog(context, date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.et_tgl_lahir_2_warga:
                new DatePickerDialog(context, date2,
                        myCalender2.get(Calendar.YEAR),
                        myCalender2.get(Calendar.MONTH),
                        myCalender2.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota1_warga:
                new DatePickerDialog(context, date3,
                        myCalender3.get(Calendar.YEAR),
                        myCalender3.get(Calendar.MONTH),
                        myCalender3.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota2_warga:
                new DatePickerDialog(context, date4,
                        myCalender4.get(Calendar.YEAR),
                        myCalender4.get(Calendar.MONTH),
                        myCalender4.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota3_warga:
                new DatePickerDialog(context, date5,
                        myCalender5.get(Calendar.YEAR),
                        myCalender5.get(Calendar.MONTH),
                        myCalender5.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota4_warga:
                new DatePickerDialog(context, date6,
                        myCalender6.get(Calendar.YEAR),
                        myCalender6.get(Calendar.MONTH),
                        myCalender6.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota5_warga:
                new DatePickerDialog(context, date7,
                        myCalender7.get(Calendar.YEAR),
                        myCalender7.get(Calendar.MONTH),
                        myCalender7.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota6_warga:
                new DatePickerDialog(context, date8,
                        myCalender8.get(Calendar.YEAR),
                        myCalender8.get(Calendar.MONTH),
                        myCalender8.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota7_warga:
                new DatePickerDialog(context, date9,
                        myCalender9.get(Calendar.YEAR),
                        myCalender9.get(Calendar.MONTH),
                        myCalender9.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.et_tgl_lahir_anggota8_warga:
                new DatePickerDialog(context, date10,
                        myCalender10.get(Calendar.YEAR),
                        myCalender10.get(Calendar.MONTH),
                        myCalender10.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.arrowBtn1:

                if (expendableView1.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView1.setVisibility(View.VISIBLE);
                    arrowBtn1.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView1.setVisibility(View.GONE);
                    arrowBtn1.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn2:

                if (expendableView2.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView2.setVisibility(View.VISIBLE);
                    arrowBtn2.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView2.setVisibility(View.GONE);
                    arrowBtn2.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn3:

                if (expendableView3.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView3.setVisibility(View.VISIBLE);
                    arrowBtn3.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView3.setVisibility(View.GONE);
                    arrowBtn3.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn4:

                if (expendableView4.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView4.setVisibility(View.VISIBLE);
                    arrowBtn4.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView4.setVisibility(View.GONE);
                    arrowBtn4.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn5:

                if (expendableView5.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView5.setVisibility(View.VISIBLE);
                    arrowBtn5.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView5.setVisibility(View.GONE);
                    arrowBtn5.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn6:

                if (expendableView6.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView6.setVisibility(View.VISIBLE);
                    arrowBtn6.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView6.setVisibility(View.GONE);
                    arrowBtn6.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn7:

                if (expendableView7.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView7.setVisibility(View.VISIBLE);
                    arrowBtn7.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView7.setVisibility(View.GONE);
                    arrowBtn7.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;

            case R.id.arrowBtn8:

                if (expendableView8.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView8.setVisibility(View.VISIBLE);
                    arrowBtn8.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expendableView8.setVisibility(View.GONE);
                    arrowBtn8.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                break;
        }
    }

    private void addKeluarga() {
        Log.w("Nilai", "KLIK1");
        String getImageKK;
        final String getNoRumah = noRumahUser;
        final String getNoKK = noKK.getText().toString().trim();
        final String getNamaKK = namaKK.getText().toString().trim();

        final String getNik1 = nik1.getText().toString().trim();
        final String getJenisKelamin1 = jenisKelamin1;
        final String getTempatLahir1 = tempatLahir1.getText().toString().trim();
        final String getTglLahir1 = tglLahir1.getText().toString();
        final String getAgama1 = agama1.getText().toString().trim();

        final String getNamaIstri = namaIstri.getText().toString().trim();
        final String getNik2 = nik2.getText().toString().trim();
        final String getStatusHubungan2 = statusHubungan2.getText().toString().trim();
        final String getJenisKelamin2 = jeniskelamin2;
        final String getTempatLahir2 = tempatLahir2.getText().toString().trim();
        final String getTglLahir2 = tglLahir2.getText().toString();
        final String getAgama2 = agama2.getText().toString().trim();

        final String getNama3 = nama3.getText().toString().trim();
        final String getNik3 = nik3.getText().toString().trim();
        final String getStatusHubungan3 = statusHubungan3.getText().toString().trim();
        String getJenisKelamin3 = jenisKelamin3;
        final String getTempatLahir3 = tempatLahir3.getText().toString().trim();
        String getTglLahir3 = tglLahir3.getText().toString();
        final String getAgama3 = agama3.getText().toString().trim();

        final String getNama4 = nama4.getText().toString().trim();
        final String getNik4 = nik4.getText().toString().trim();
        final String getStatusHubungan4 = statusHubungan4.getText().toString().trim();
        String getJenisKelamin4 = jenisKelamin4;
        final String getTempatLahir4 = tempatLahir4.getText().toString().trim();
        String getTglLahir4 = tglLahir4.getText().toString();
        final String getAgama4 = agama4.getText().toString().trim();

        final String getNama5 = nama5.getText().toString().trim();
        final String getNik5 = nik5.getText().toString().trim();
        final String getStatusHubungan5 = statusHubungan5.getText().toString().trim();
        String getJenisKelamin5 = jenisKelamin5;
        final String getTempatLahir5 = tempatLahir5.getText().toString().trim();
        String getTglLahir5 = tglLahir5.getText().toString();
        final String getAgama5 = agama5.getText().toString().trim();

        final String getNama6 = nama6.getText().toString().trim();
        final String getNik6 = nik6.getText().toString().trim();
        final String getStatusHubungan6 = statusHubungan6.getText().toString().trim();
        String getJenisKelamin6 = jenisKelamin6;
        final String getTempatLahir6 = tempatLahir6.getText().toString().trim();
        String getTglLahir6 = tglLahir6.getText().toString();
        final String getAgama6 = agama6.getText().toString().trim();

        final String getNama7 = nama7.getText().toString().trim();
        final String getNik7 = nik7.getText().toString().trim();
        final String getStatusHubungan7 = statusHubungan7.getText().toString().trim();
        String getJenisKelamin7 = jenisKelamin7;
        final String getTempatLahir7 = tempatLahir7.getText().toString().trim();
        String getTglLahir7 = tglLahir7.getText().toString();
        final String getAgama7 = agama7.getText().toString().trim();

        final String getNama8 = nama8.getText().toString().trim();
        final String getNik8 = nik8.getText().toString().trim();
        final String getStatusHubungan8 = statusHubungan8.getText().toString().trim();
        String getJenisKelamin8 = jenisKelamin8;
        final String getTempatLahir8 = tempatLahir8.getText().toString().trim();
        String getTglLahir8 = tglLahir8.getText().toString();
        final String getAgama8 = agama8.getText().toString().trim();
        Log.w("Nilai", "KLIK2");
        final String getNama9 = nama9.getText().toString().trim();
        final String getNik9 = nik9.getText().toString().trim();
        final String getStatusHubungan9 = statusHubungan9.getText().toString().trim();
        String getJenisKelamin9 = jenisKelamin9;
        final String getTempatLahir9 = tempatLahir9.getText().toString().trim();
        String getTglLahir9 = tglLahir9.getText().toString();
        final String getAgama9 = agama9.getText().toString().trim();
        Log.w("Nilai", "KLIK3");
        final String getNama10 = nama10.getText().toString().trim();
        final String getNik10 = nik10.getText().toString().trim();
        final String getStatusHubungan10 = statusHubungan10.getText().toString().trim();
        String getJenisKelamin10 = jenisKelamin10;
        final String getTempatLahir10 = tempatLahir10.getText().toString().trim();
        String getTglLahir10 = tglLahir10.getText().toString();
        final String getAgama10 = agama10.getText().toString().trim();
        Log.w("Nilai", "KLIK4");

        if (image_uri == null) {
            //post without image
            getImageKK = "noImage";
        }
        else {
            getImageKK = String.valueOf(image_uri);
            //post with image
        }

        if (TextUtils.isEmpty(getNoRumah) || getNoRumah.equals("No Rumah")) {
            ((TextView)spinnerNoRumah.getSelectedView()).setError("Error message");
            spinnerNoRumah.setError("Pilih No Rumah!");
            spinnerNoRumah.requestFocus();
            Toast.makeText(this, "Pilih No Rumah!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getNoKK)) {
            noKK.setError("No KK tidak boleh kosong!");
            noKK.requestFocus();
            Toast.makeText(this, "No KK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

//Kepala Keluarga
        if (TextUtils.isEmpty(getNamaKK)) {
            namaKK.setError("Nama Kepala Keluarga tidak boleh kosong!");
            namaKK.requestFocus();
            Toast.makeText(this, "Nama Kepala Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getNik1)) {
            nik1.setError("NIK tidak boleh kosong!");
            nik1.requestFocus();
            Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;

        }
        if (getNik1.length() != 16) {
            nik1.setError("NIK Harus 16 digit");
            nik1.requestFocus();
            Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getJenisKelamin1) || getJenisKelamin1.equals("Jenis Kelamin")) {
            ((TextView)spinnerjenisKelamin1.getSelectedView()).setError("Error message");
            spinnerjenisKelamin1.setError("Pilih jenis kelamin!");
            spinnerjenisKelamin1.requestFocus();
            Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getTempatLahir1)) {
            tempatLahir1.setError("Tempat lahir tidak boleh kosong!");
            tempatLahir1.requestFocus();
            Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getTglLahir1)) {
            tglLahir1.requestFocus();
            Toast.makeText(this, "Pilih Tanggal Lahir!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getAgama1)) {
            agama1.setError("Agama tidak boleh kosong!");
            agama1.requestFocus();
            Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
//----------------------------------------------------------------------

//Istri
        if (TextUtils.isEmpty(getNamaIstri)) {
            namaIstri.setError("Nama Istri tidak boleh kosong!");
            namaIstri.requestFocus();
            Toast.makeText(this, "Nama Istri tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getNik2)) {
            nik2.setError("NIK tidak boleh kosong!");
            nik2.requestFocus();
            Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getNik2.length() != 16) {
            nik2.setError("NIK Harus 16 digit");
            nik2.requestFocus();
            Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getStatusHubungan2)) {
            statusHubungan2.setError("Status Hubungan Keluarga tidak boleh kosong!");
            statusHubungan2.requestFocus();
            Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getJenisKelamin2) || getJenisKelamin2.equals("Jenis Kelamin")) {
            ((TextView)spinnerjenisKelamin2.getSelectedView()).setError("Error message");
            spinnerjenisKelamin2.setError("Pilih jenis kelamin!");
            spinnerjenisKelamin2.requestFocus();
            Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getTempatLahir2)) {
            tempatLahir2.setError("Tempat lahir tidak boleh kosong!");
            tempatLahir2.requestFocus();
            Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getTglLahir2)) {
            tglLahir2.requestFocus();
            Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(getAgama2)) {
            agama2.setError("Agama tidak boleh kosong!");
            agama2.requestFocus();
            Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
//Keluarga 3
        if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

//            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(getNama3)) {
                nama3.setError("Nama tidak boleh kosong!");
                nama3.requestFocus();
                Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getNik3)) {
                nik3.setError("NIK tidak boleh kosong!");
                nik3.requestFocus();
                Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (getNik3.length() != 16) {
                nik3.setError("NIK Harus 16 digit");
                nik3.requestFocus();
                Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getStatusHubungan3)) {
                statusHubungan3.setError("Status Hubungan Keluarga tidak boleh kosong!");
                statusHubungan3.requestFocus();
                Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getJenisKelamin3) || getJenisKelamin3.equals("Jenis Kelamin")) {
                ((TextView)spinnerjenisKelamin3.getSelectedView()).setError("Error message");
                spinnerjenisKelamin3.setError("Pilih jenis kelamin!");
                spinnerjenisKelamin3.requestFocus();
                Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getTempatLahir3)) {
                tempatLahir3.setError("Tempat lahir tidak boleh kosong!");
                tempatLahir3.requestFocus();
                Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getTglLahir3)) {
                tglLahir3.requestFocus();
                Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getAgama3)) {
                agama3.setError("Agama tidak boleh kosong!");
                agama3.requestFocus();
                Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

//keluarga4
        if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {
                if (TextUtils.isEmpty(getNama4)) {
                    nama4.setError("Nama tidak boleh kosong!");
                    nama4.requestFocus();
                    Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getNik4)) {
                    nik4.setError("NIK tidak boleh kosong!");
                    nik4.requestFocus();
                    Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getNik4.length() != 16) {
                    nik4.setError("NIK Harus 16 digit");
                    nik4.requestFocus();
                    Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getStatusHubungan4)) {
                    statusHubungan4.setError("Status Hubungan Keluarga tidak boleh kosong!");
                    statusHubungan4.requestFocus();
                    Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getJenisKelamin4) || getJenisKelamin4.equals("Jenis Kelamin")) {
                    ((TextView)spinnerjenisKelamin4.getSelectedView()).setError("Error message");
                    spinnerjenisKelamin4.setError("Pilih jenis kelamin!");
                    spinnerjenisKelamin4.requestFocus();
                    Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getTempatLahir4)) {
                    tempatLahir4.setError("Tempat lahir tidak boleh kosong!");
                    tempatLahir4.requestFocus();
                    Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getTglLahir4)) {
                    tglLahir4.requestFocus();
                    Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getAgama4)) {
                    agama4.setError("Agama tidak boleh kosong!");
                    agama4.requestFocus();
                    Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else {

                nama4.setText("");
                nik4.setText("");
                statusHubungan4.setText("");
                tempatLahir4.setText("");
                agama4.setText("");
                tglLahir4.setText("");
                getJenisKelamin4 = "";
                getTglLahir4 = "";
                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }

        }

//keluarga5
        if (!TextUtils.isEmpty(getNama5) || !TextUtils.isEmpty(getNik5) || !TextUtils.isEmpty(getStatusHubungan5) ||
                !TextUtils.isEmpty(getTempatLahir5)  || !TextUtils.isEmpty(getAgama5)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

                if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                        !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

                    if (TextUtils.isEmpty(getNama5)) {
                        nama5.setError("Nama tidak boleh kosong!");
                        nama5.requestFocus();
                        Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(getNik5)) {
                        nik5.setError("NIK tidak boleh kosong!");
                        nik5.requestFocus();
                        Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (getNik5.length() != 16) {
                        nik5.setError("NIK Harus 16 digit");
                        nik5.requestFocus();
                        Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(getStatusHubungan5)) {
                        statusHubungan5.setError("Status Hubungan Keluarga tidak boleh kosong!");
                        statusHubungan5.requestFocus();
                        Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(getJenisKelamin5) || getJenisKelamin5.equals("Jenis Kelamin")) {
                        ((TextView)spinnerjenisKelamin5.getSelectedView()).setError("Error message");
                        spinnerjenisKelamin5.setError("Pilih jenis kelamin!");
                        spinnerjenisKelamin5.requestFocus();
                        Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(getTempatLahir5)) {
                        tempatLahir5.setError("Tempat lahir tidak boleh kosong!");
                        tempatLahir5.requestFocus();
                        Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(getTglLahir5)) {
                        tglLahir5.requestFocus();
                        Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(getAgama5)) {
                        agama5.setError("Agama tidak boleh kosong!");
                        agama5.requestFocus();
                        Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else {

                    nama5.setText("");
                    nik5.setText("");
                    statusHubungan5.setText("");
                    tempatLahir5.setText("");
                    agama5.setText("");
                    tglLahir5.setText("");
                    getJenisKelamin5 = "";
                    getTglLahir5 = "";
                    Toast.makeText(this, "Isi Anggota Keluarga 2 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                nama5.setText("");
                nik5.setText("");
                statusHubungan5.setText("");
                tempatLahir5.setText("");
                agama5.setText("");
                tglLahir5.setText("");
                getJenisKelamin5 = "";
                getTglLahir5 = "";
                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }

        }

//keluarga6
        if (!TextUtils.isEmpty(getNama6) || !TextUtils.isEmpty(getNik6) || !TextUtils.isEmpty(getStatusHubungan6) ||
                !TextUtils.isEmpty(getTempatLahir6)  || !TextUtils.isEmpty(getAgama6)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

                if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                        !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

                    if (!TextUtils.isEmpty(getNama5) || !TextUtils.isEmpty(getNik5) || !TextUtils.isEmpty(getStatusHubungan5) ||
                            !TextUtils.isEmpty(getTempatLahir5)  || !TextUtils.isEmpty(getAgama5)) {

                        if (TextUtils.isEmpty(getNama6)) {
                            nama6.setError("Nama tidak boleh kosong!");
                            nama6.requestFocus();
                            Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(getNik6)) {
                            nik6.setError("NIK tidak boleh kosong!");
                            nik6.requestFocus();
                            Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (getNik6.length() != 16) {
                            nik6.setError("NIK Harus 16 digit");
                            nik6.requestFocus();
                            Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(getStatusHubungan6)) {
                            statusHubungan6.setError("Status Hubungan Keluarga tidak boleh kosong!");
                            statusHubungan6.requestFocus();
                            Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(getJenisKelamin6) || getJenisKelamin6.equals("Jenis Kelamin")) {
                            ((TextView)spinnerjenisKelamin6.getSelectedView()).setError("Error message");
                            spinnerjenisKelamin6.setError("Pilih jenis kelamin!");
                            spinnerjenisKelamin6.requestFocus();
                            Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(getTempatLahir6)) {
                            tempatLahir6.setError("Tempat lahir tidak boleh kosong!");
                            tempatLahir6.requestFocus();
                            Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(getTglLahir6)) {
                            tglLahir6.requestFocus();
                            Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(getAgama6)) {
                            agama6.setError("Agama tidak boleh kosong!");
                            agama6.requestFocus();
                            Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else {
                        nama6.setText("");
                        nik6.setText("");
                        statusHubungan6.setText("");
                        tempatLahir6.setText("");
                        agama6.setText("");
                        tglLahir6.setText("");
                        getJenisKelamin6 = "";
                        getTglLahir6 = "";
                        Toast.makeText(this, "Isi Anggota Keluarga 3 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    nama6.setText("");
                    nik6.setText("");
                    statusHubungan6.setText("");
                    tempatLahir6.setText("");
                    agama6.setText("");
                    tglLahir6.setText("");
                    getJenisKelamin6 = "";
                    getTglLahir6 = "";
                    Toast.makeText(this, "Isi Anggota Keluarga 2 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                nama6.setText("");
                nik6.setText("");
                statusHubungan6.setText("");
                tempatLahir6.setText("");
                agama6.setText("");
                tglLahir6.setText("");
                getJenisKelamin6 = "";
                getTglLahir6 = "";
                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }



        }

//keluarga7
        if (!TextUtils.isEmpty(getNama7) || !TextUtils.isEmpty(getNik7) || !TextUtils.isEmpty(getStatusHubungan7) ||
                !TextUtils.isEmpty(getTempatLahir7)  || !TextUtils.isEmpty(getAgama7)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

                if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                        !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

                    if (!TextUtils.isEmpty(getNama5) || !TextUtils.isEmpty(getNik5) || !TextUtils.isEmpty(getStatusHubungan5) ||
                            !TextUtils.isEmpty(getTempatLahir5)  || !TextUtils.isEmpty(getAgama5)) {

                        if (!TextUtils.isEmpty(getNama6) || !TextUtils.isEmpty(getNik6) || !TextUtils.isEmpty(getStatusHubungan6) ||
                                !TextUtils.isEmpty(getTempatLahir6)  || !TextUtils.isEmpty(getAgama6)) {

                            if (TextUtils.isEmpty(getNama7)) {
                                nama7.setError("Nama tidak boleh kosong!");
                                nama7.requestFocus();
                                Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(getNik7)) {
                                nik7.setError("NIK tidak boleh kosong!");
                                nik7.requestFocus();
                                Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (getNik7.length() != 16) {
                                nik7.setError("NIK Harus 16 digit");
                                nik7.requestFocus();
                                Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(getStatusHubungan7)) {
                                statusHubungan7.setError("Status Hubungan Keluarga tidak boleh kosong!");
                                statusHubungan7.requestFocus();
                                Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(getJenisKelamin7) || getJenisKelamin7.equals("Jenis Kelamin")) {
                                ((TextView)spinnerjenisKelamin7.getSelectedView()).setError("Error message");
                                spinnerjenisKelamin7.setError("Pilih jenis kelamin!");
                                spinnerjenisKelamin7.requestFocus();
                                Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(getTempatLahir7)) {
                                tempatLahir7.setError("Tempat lahir tidak boleh kosong!");
                                tempatLahir7.requestFocus();
                                Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(getTglLahir7)) {
                                tglLahir7.requestFocus();
                                Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(getAgama7)) {
                                agama7.setError("Agama tidak boleh kosong!");
                                agama7.requestFocus();
                                Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        else {
                            nama7.setText("");
                            nik7.setText("");
                            statusHubungan7.setText("");
                            tempatLahir7.setText("");
                            agama7.setText("");
                            tglLahir7.setText("");
                            getJenisKelamin7 = "";
                            getTglLahir7 = "";
                            Toast.makeText(this, "Isi Anggota Keluarga 4 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        nama7.setText("");
                        nik7.setText("");
                        statusHubungan7.setText("");
                        tempatLahir7.setText("");
                        agama7.setText("");
                        tglLahir7.setText("");
                        getJenisKelamin7 = "";
                        getTglLahir7 = "";
                        Toast.makeText(this, "Isi Anggota Keluarga 3 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    nama7.setText("");
                    nik7.setText("");
                    statusHubungan7.setText("");
                    tempatLahir7.setText("");
                    agama7.setText("");
                    tglLahir7.setText("");
                    getJenisKelamin7 = "";
                    getTglLahir7 = "";
                    Toast.makeText(this, "Isi Anggota Keluarga 2 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                nama7.setText("");
                nik7.setText("");
                statusHubungan7.setText("");
                tempatLahir7.setText("");
                agama7.setText("");
                tglLahir7.setText("");
                getJenisKelamin7 = "";
                getTglLahir7 = "";
                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }

        }


//keluarga8
        if (!TextUtils.isEmpty(getNama8) || !TextUtils.isEmpty(getNik8) || !TextUtils.isEmpty(getStatusHubungan8) ||
                !TextUtils.isEmpty(getTempatLahir8)  || !TextUtils.isEmpty(getAgama8)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

                if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                        !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

                    if (!TextUtils.isEmpty(getNama5) || !TextUtils.isEmpty(getNik5) || !TextUtils.isEmpty(getStatusHubungan5) ||
                            !TextUtils.isEmpty(getTempatLahir5)  || !TextUtils.isEmpty(getAgama5)) {

                        if (!TextUtils.isEmpty(getNama6) || !TextUtils.isEmpty(getNik6) || !TextUtils.isEmpty(getStatusHubungan6) ||
                                !TextUtils.isEmpty(getTempatLahir6)  || !TextUtils.isEmpty(getAgama6)) {

                            if (!TextUtils.isEmpty(getNama7) || !TextUtils.isEmpty(getNik7) || !TextUtils.isEmpty(getStatusHubungan7) ||
                                    !TextUtils.isEmpty(getTempatLahir7)  || !TextUtils.isEmpty(getAgama7)) {

                                if (TextUtils.isEmpty(getNama8)) {
                                    nama8.setError("Nama tidak boleh kosong!");
                                    nama8.requestFocus();
                                    Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(getNik8)) {
                                    nik8.setError("NIK tidak boleh kosong!");
                                    nik8.requestFocus();
                                    Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (getNik8.length() != 16) {
                                    nik8.setError("NIK Harus 16 digit");
                                    nik8.requestFocus();
                                    Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(getStatusHubungan8)) {
                                    statusHubungan8.setError("Status Hubungan Keluarga tidak boleh kosong!");
                                    statusHubungan8.requestFocus();
                                    Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(getJenisKelamin8) || getJenisKelamin8.equals("Jenis Kelamin")) {
                                    ((TextView)spinnerjenisKelamin8.getSelectedView()).setError("Error message");
                                    spinnerjenisKelamin8.setError("Pilih jenis kelamin!");
                                    spinnerjenisKelamin8.requestFocus();
                                    Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(getTempatLahir8)) {
                                    tempatLahir8.setError("Tempat lahir tidak boleh kosong!");
                                    tempatLahir8.requestFocus();
                                    Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(getTglLahir8)) {
                                    tglLahir8.requestFocus();
                                    Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(getAgama8)) {
                                    agama8.setError("Agama tidak boleh kosong!");
                                    agama8.requestFocus();
                                    Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            else {
                                nama8.setText("");
                                nik8.setText("");
                                statusHubungan8.setText("");
                                tempatLahir8.setText("");
                                agama8.setText("");
                                tglLahir8.setText("");
                                getJenisKelamin8 = "";
                                getTglLahir8 = "";
                                Toast.makeText(this, "Isi Anggota Keluarga 5 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            nama8.setText("");
                            nik8.setText("");
                            statusHubungan8.setText("");
                            tempatLahir8.setText("");
                            agama8.setText("");
                            tglLahir8.setText("");
                            getJenisKelamin8 = "";
                            getTglLahir8 = "";
                            Toast.makeText(this, "Isi Anggota Keluarga 4 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        nama8.setText("");
                        nik8.setText("");
                        statusHubungan8.setText("");
                        tempatLahir8.setText("");
                        agama8.setText("");
                        tglLahir8.setText("");
                        getJenisKelamin8 = "";
                        getTglLahir8 = "";
                        Toast.makeText(this, "Isi Anggota Keluarga 3 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    nama8.setText("");
                    nik8.setText("");
                    statusHubungan8.setText("");
                    tempatLahir8.setText("");
                    agama8.setText("");
                    tglLahir8.setText("");
                    getJenisKelamin8 = "";
                    getTglLahir8 = "";
                    Toast.makeText(this, "Isi Anggota Keluarga 2 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                nama8.setText("");
                nik8.setText("");
                statusHubungan8.setText("");
                tempatLahir8.setText("");
                agama8.setText("");
                tglLahir8.setText("");
                getJenisKelamin8 = "";
                getTglLahir8 = "";
                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }

        }

//keluarga9
        if (!TextUtils.isEmpty(getNama9) || !TextUtils.isEmpty(getNik9) || !TextUtils.isEmpty(getStatusHubungan9) ||
                !TextUtils.isEmpty(getTempatLahir9)  || !TextUtils.isEmpty(getAgama9)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

                if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                        !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

                    if (!TextUtils.isEmpty(getNama5) || !TextUtils.isEmpty(getNik5) || !TextUtils.isEmpty(getStatusHubungan5) ||
                            !TextUtils.isEmpty(getTempatLahir5)  || !TextUtils.isEmpty(getAgama5)) {

                        if (!TextUtils.isEmpty(getNama6) || !TextUtils.isEmpty(getNik6) || !TextUtils.isEmpty(getStatusHubungan6) ||
                                !TextUtils.isEmpty(getTempatLahir6)  || !TextUtils.isEmpty(getAgama6)) {

                            if (!TextUtils.isEmpty(getNama7) || !TextUtils.isEmpty(getNik7) || !TextUtils.isEmpty(getStatusHubungan7) ||
                                    !TextUtils.isEmpty(getTempatLahir7)  || !TextUtils.isEmpty(getAgama7)) {

                                if (!TextUtils.isEmpty(getNama8) || !TextUtils.isEmpty(getNik8) || !TextUtils.isEmpty(getStatusHubungan8) ||
                                        !TextUtils.isEmpty(getTempatLahir8)  || !TextUtils.isEmpty(getAgama8)) {

                                    if (TextUtils.isEmpty(getNama9)) {
                                        nama9.setError("Nama tidak boleh kosong!");
                                        nama9.requestFocus();
                                        Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (TextUtils.isEmpty(getNik9)) {
                                        nik9.setError("NIK tidak boleh kosong!");
                                        nik9.requestFocus();
                                        Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (getNik9.length() != 16) {
                                        nik9.setError("NIK Harus 16 digit");
                                        nik9.requestFocus();
                                        Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (TextUtils.isEmpty(getStatusHubungan9)) {
                                        statusHubungan9.setError("Status Hubungan Keluarga tidak boleh kosong!");
                                        statusHubungan9.requestFocus();
                                        Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (TextUtils.isEmpty(getJenisKelamin9) || getJenisKelamin9.equals("Jenis Kelamin")) {
                                        ((TextView)spinnerjenisKelamin9.getSelectedView()).setError("Error message");
                                        spinnerjenisKelamin9.setError("Pilih jenis kelamin!");
                                        spinnerjenisKelamin9.requestFocus();
                                        Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (TextUtils.isEmpty(getTempatLahir9)) {
                                        tempatLahir9.setError("Tempat lahir tidak boleh kosong!");
                                        tempatLahir9.requestFocus();
                                        Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (TextUtils.isEmpty(getTglLahir9)) {
                                        tglLahir9.requestFocus();
                                        Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (TextUtils.isEmpty(getAgama9)) {
                                        agama9.setError("Agama tidak boleh kosong!");
                                        agama9.requestFocus();
                                        Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                else {

                                    nama9.setText("");
                                    nik9.setText("");
                                    statusHubungan9.setText("");
                                    tempatLahir9.setText("");
                                    agama9.setText("");
                                    tglLahir9.setText("");
                                    getJenisKelamin9 = "";
                                    getTglLahir9 = "";
                                    Toast.makeText(this, "Isi Anggota Keluarga 6 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                nama9.setText("");
                                nik9.setText("");
                                statusHubungan9.setText("");
                                tempatLahir9.setText("");
                                agama9.setText("");
                                tglLahir9.setText("");
                                getJenisKelamin9 = "";
                                getTglLahir9 = "";
                                Toast.makeText(this, "Isi Anggota Keluarga 5 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            nama9.setText("");
                            nik9.setText("");
                            statusHubungan9.setText("");
                            tempatLahir9.setText("");
                            agama9.setText("");
                            tglLahir9.setText("");
                            getJenisKelamin9 = "";
                            getTglLahir9 = "";
                            Toast.makeText(this, "Isi Anggota Keluarga 4 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        nama9.setText("");
                        nik9.setText("");
                        statusHubungan9.setText("");
                        tempatLahir9.setText("");
                        agama9.setText("");
                        tglLahir9.setText("");
                        getJenisKelamin9 = "";
                        getTglLahir9 = "";
                        Toast.makeText(this, "Isi Anggota Keluarga 3 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    nama9.setText("");
                    nik9.setText("");
                    statusHubungan9.setText("");
                    tempatLahir9.setText("");
                    agama9.setText("");
                    tglLahir9.setText("");
                    getJenisKelamin9 = "";
                    getTglLahir9 = "";
                    Toast.makeText(this, "Isi Anggota Keluarga 2 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                nama9.setText("");
                nik9.setText("");
                statusHubungan9.setText("");
                tempatLahir9.setText("");
                agama9.setText("");
                tglLahir9.setText("");
                getJenisKelamin9 = "";
                getTglLahir9 = "";
                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }

        }

//keluarga10
        if (!TextUtils.isEmpty(getNama10) || !TextUtils.isEmpty(getNik10) || !TextUtils.isEmpty(getStatusHubungan10) ||
                !TextUtils.isEmpty(getTempatLahir10)  || !TextUtils.isEmpty(getAgama10)) {

            if (!TextUtils.isEmpty(getNama3) || !TextUtils.isEmpty(getNik3) || !TextUtils.isEmpty(getStatusHubungan3) ||
                    !TextUtils.isEmpty(getTempatLahir3)  || !TextUtils.isEmpty(getAgama3)) {

                if (!TextUtils.isEmpty(getNama4) || !TextUtils.isEmpty(getNik4) || !TextUtils.isEmpty(getStatusHubungan4) ||
                        !TextUtils.isEmpty(getTempatLahir4)  || !TextUtils.isEmpty(getAgama4)) {

                    if (!TextUtils.isEmpty(getNama5) || !TextUtils.isEmpty(getNik5) || !TextUtils.isEmpty(getStatusHubungan5) ||
                            !TextUtils.isEmpty(getTempatLahir5)  || !TextUtils.isEmpty(getAgama5)) {

                        if (!TextUtils.isEmpty(getNama6) || !TextUtils.isEmpty(getNik6) || !TextUtils.isEmpty(getStatusHubungan6) ||
                                !TextUtils.isEmpty(getTempatLahir6)  || !TextUtils.isEmpty(getAgama6)) {

                            if (!TextUtils.isEmpty(getNama7) || !TextUtils.isEmpty(getNik7) || !TextUtils.isEmpty(getStatusHubungan7) ||
                                    !TextUtils.isEmpty(getTempatLahir7)  || !TextUtils.isEmpty(getAgama7)) {

                                if (!TextUtils.isEmpty(getNama8) || !TextUtils.isEmpty(getNik8) || !TextUtils.isEmpty(getStatusHubungan8) ||
                                        !TextUtils.isEmpty(getTempatLahir8)  || !TextUtils.isEmpty(getAgama8)) {

                                    if (!TextUtils.isEmpty(getNama9) || !TextUtils.isEmpty(getNik9) || !TextUtils.isEmpty(getStatusHubungan9) ||
                                            !TextUtils.isEmpty(getTempatLahir9)  || !TextUtils.isEmpty(getAgama9)) {
                                        Log.w("Nilai", "M18");
                                        if (TextUtils.isEmpty(getNama10)) {
                                            nama10.setError("Nama tidak boleh kosong!");
                                            nama10.requestFocus();
                                            Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(getNik10)) {
                                            nik10.setError("NIK tidak boleh kosong!");
                                            nik10.requestFocus();
                                            Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (getNik10.length() != 16) {
                                            nik10.setError("NIK Harus 16 digit");
                                            nik10.requestFocus();
                                            Toast.makeText(this, "NIK Harus 16 digit", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(getStatusHubungan10)) {
                                            statusHubungan10.setError("Status Hubungan Keluarga tidak boleh kosong!");
                                            statusHubungan10.requestFocus();
                                            Toast.makeText(this, "Status Hubungan Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(getJenisKelamin10) || getJenisKelamin10.equals("Jenis Kelamin")) {
                                            ((TextView)spinnerjenisKelamin10.getSelectedView()).setError("Error message");
                                            spinnerjenisKelamin10.setError("Pilih jenis kelamin!");
                                            spinnerjenisKelamin10.requestFocus();
                                            Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(getTempatLahir10)) {
                                            tempatLahir10.setError("Tempat lahir tidak boleh kosong!");
                                            tempatLahir10.requestFocus();
                                            Toast.makeText(this, "Tempat lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(getTglLahir10)) {
                                            tglLahir10.requestFocus();
                                            Toast.makeText(this, "Tanggal lahir tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(getAgama10)) {
                                            agama10.setError("Agama tidak boleh kosong!");
                                            agama10.requestFocus();
                                            Toast.makeText(this, "Agama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else {
                                            Log.w("Nilai", "SUKSES");
                                            pd.setMessage("Menambahkan data keluarga...");
                                            pd.show();

                                            jenisK1 = getJenisKelamin1;
                                            tglL1 = getTglLahir1;
                                            jenisK2 = getJenisKelamin2;
                                            tglL2 = getTglLahir2;
                                            jenisK3 = getJenisKelamin3;
                                            tglL3 = getTglLahir3;
                                            jenisK4 = getJenisKelamin4;
                                            tglL4 = getTglLahir4;
                                            jenisK5 = getJenisKelamin5;
                                            tglL5 = getTglLahir5;
                                            Log.w("Nilai", "KLIK6");
                                            jenisK6 = getJenisKelamin6;
                                            tglL6 = getTglLahir6;
                                            jenisK7 = getJenisKelamin7;
                                            tglL7 = getTglLahir7;
                                            jenisK8 = getJenisKelamin8;
                                            tglL8 = getTglLahir8;
                                            jenisK9 = getJenisKelamin9;
                                            tglL9 = getTglLahir9;
                                            jenisK10 = getJenisKelamin10;
                                            tglL10 = getTglLahir10;
                                            Log.w("Nilai", "KLIK7");
                                            if (getNama3.equals("")) {
                                                jenisK3 = "";
                                                tglL3 = "";
                                            }
                                            if (getNama4.equals("")) {
                                                jenisK4 = "";
                                                tglL4 = "";
                                            }
                                            if (getNama5.equals("")) {
                                                jenisK5 = "";
                                                tglL5 = "";
                                            }
                                            if (getNama6.equals("")) {
                                                jenisK6 = "";
                                                tglL6 = "";
                                            }
                                            if (getNama7.equals("")) {
                                                jenisK7 = "";
                                                tglL7 = "";
                                            }
                                            if (getNama8.equals("")) {
                                                jenisK8 = "";
                                                tglL8 = "";
                                            }
                                            if (getNama9.equals("")) {
                                                jenisK9 = "";
                                                tglL9 = "";
                                            }
                                            if (getNama10.equals("")) {
                                                jenisK10 = "";
                                                tglL10 = "";
                                            }


                                            if (!getImageKK.equals("noImage")) {
                                                Log.w("Nilai", "KLIK8");
                                                // Delete foto sebelumnya
                                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                                StorageReference profileref = storageRef.child("Kartukeluarga/kk_img_" + setIdUserSelect);
                                                profileref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // File deleted successfully
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Uh-oh, an error occurred!
                                                    }
                                                });
                                                //post with image
//                final String timeStamp = String.valueOf(System.currentTimeMillis());
                                                String filePathAndName = "Kartukeluarga/" + "kk_img_" + setIdUserSelect;
                                                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                                                ref.putFile(Uri.parse(getImageKK))
                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                //image is uploaded to firebase storage, now get it's uri
                                                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                                                while (!uriTask.isSuccessful());
                                                                final String downloadUri = uriTask.getResult().toString();

                                                                if (uriTask.isSuccessful()) {
                                                                    Log.w("Nilai", "KLIK9");
                                                                    //uri is received upload post to firebase database
                                                                    //path to store post data
                                                                    //referen untuk tabel Keluarga
                                                                    databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");

                                                                    String id = databaseKeluarga.push().getKey();

                                                                    //we will store the additional fields in firebase database
                                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                                    hashMap.put("keluargaId", id);
                                                                    hashMap.put("userId", setIdUserSelect);
                                                                    hashMap.put("imageKK", downloadUri) ;
                                                                    hashMap.put("noRumah", getNoRumah);
                                                                    hashMap.put("noKK", getNoKK);

                                                                    hashMap.put("namaKK", getNamaKK);
                                                                    hashMap.put("nik1", getNik1);
                                                                    hashMap.put("jenisKelamin1", jenisK1);
                                                                    hashMap.put("tempatLahir1", getTempatLahir1);
                                                                    hashMap.put("tglLahir1", tglL1);
                                                                    hashMap.put("agama1", getAgama1);

                                                                    hashMap.put("namaIstri", getNamaIstri);
                                                                    hashMap.put("nik2", getNik2);
                                                                    hashMap.put("statusHubungan2", getStatusHubungan2);
                                                                    hashMap.put("jenisKelamin2", jenisK2);
                                                                    hashMap.put("tempatLahir2", getTempatLahir2);
                                                                    hashMap.put("tglLahir2", tglL2);
                                                                    hashMap.put("agama2", getAgama2);

                                                                    hashMap.put("nama3", getNama3);
                                                                    hashMap.put("nik3", getNik3);
                                                                    hashMap.put("statusHubungan3", getStatusHubungan3);
                                                                    hashMap.put("jenisKelamin3", jenisK3);
                                                                    hashMap.put("tempatLahir3", getTempatLahir3);
                                                                    hashMap.put("tglLahir3", tglL3);
                                                                    hashMap.put("agama3", getAgama3);

                                                                    hashMap.put("nama4", getNama4);
                                                                    hashMap.put("nik4", getNik4);
                                                                    hashMap.put("statusHubungan4", getStatusHubungan4);
                                                                    hashMap.put("jenisKelamin4", jenisK4);
                                                                    hashMap.put("tempatLahir4", getTempatLahir4);
                                                                    hashMap.put("tglLahir4", tglL4);
                                                                    hashMap.put("agama4", getAgama4);

                                                                    hashMap.put("nama5", getNama5);
                                                                    hashMap.put("nik5", getNik5);
                                                                    hashMap.put("statusHubungan5", getStatusHubungan5);
                                                                    hashMap.put("jenisKelamin5", jenisK5);
                                                                    hashMap.put("tempatLahir5", getTempatLahir5);
                                                                    hashMap.put("tglLahir5", tglL5);
                                                                    hashMap.put("agama5", getAgama5);

                                                                    hashMap.put("nama6", getNama6);
                                                                    hashMap.put("nik6", getNik6);
                                                                    hashMap.put("statusHubungan6", getStatusHubungan6);
                                                                    hashMap.put("jenisKelamin6", jenisK6);
                                                                    hashMap.put("tempatLahir6", getTempatLahir6);
                                                                    hashMap.put("tglLahir6", tglL6);
                                                                    hashMap.put("agama6", getAgama6);

                                                                    hashMap.put("nama7", getNama7);
                                                                    hashMap.put("nik7", getNik7);
                                                                    hashMap.put("statusHubungan7", getStatusHubungan7);
                                                                    hashMap.put("jenisKelamin7", jenisK7);
                                                                    hashMap.put("tempatLahir7", getTempatLahir7);
                                                                    hashMap.put("tglLahir7", tglL7);
                                                                    hashMap.put("agama7", getAgama7);

                                                                    hashMap.put("nama8", getNama8);
                                                                    hashMap.put("nik8", getNik8);
                                                                    hashMap.put("statusHubungan8", getStatusHubungan8);
                                                                    hashMap.put("jenisKelamin8", jenisK8);
                                                                    hashMap.put("tempatLahir8", getTempatLahir8);
                                                                    hashMap.put("tglLahir8", tglL8);
                                                                    hashMap.put("agama8", getAgama8);

                                                                    hashMap.put("nama9", getNama9);
                                                                    hashMap.put("nik9", getNik9);
                                                                    hashMap.put("statusHubungan9", getStatusHubungan9);
                                                                    hashMap.put("jenisKelamin9", jenisK9);
                                                                    hashMap.put("tempatLahir9", getTempatLahir9);
                                                                    hashMap.put("tglLahir9", tglL9);
                                                                    hashMap.put("agama9", getAgama9);

                                                                    Log.w("Nilai", "KLIK10");

                                                                    hashMap.put("nama10", getNama10);
                                                                    hashMap.put("nik10", getNik10);
                                                                    hashMap.put("statusHubungan10", getStatusHubungan10);
                                                                    hashMap.put("jenisKelamin10", jenisK10);
                                                                    hashMap.put("tempatLahir10", getTempatLahir10);
                                                                    hashMap.put("tglLahir10", tglL10);
                                                                    hashMap.put("agama10", getAgama10);
                                                                    Log.w("Nilai", "KLIK11");
                                                                    uploadData(id, hashMap);
                                                                    Log.w("Nilai", "3 "+getNama3+" + "+getNik3+" + "+getStatusHubungan3+" + "+jenisK3+" + "+getTempatLahir3+" + "+tglL3+" + "+getAgama3);
                                                                    Log.w("Nilai", "4 "+getNama4+" + "+getNik4+" + "+getStatusHubungan4+" + "+jenisK4+" + "+getTempatLahir4+" + "+tglL4+" + "+getAgama4);
                                                                    Log.w("Nilai", "5 "+getNama5+" + "+getNik5+" + "+getStatusHubungan5+" + "+jenisK5+" + "+getTempatLahir5+" + "+tglL5+" + "+getAgama5);
                                                                    Log.w("Nilai", "6 "+getNama6+" + "+getNik6+" + "+getStatusHubungan6+" + "+jenisK6+" + "+getTempatLahir6+" + "+tglL6+" + "+getAgama6);
                                                                    Log.w("Nilai", "7 "+getNama7+" + "+getNik7+" + "+getStatusHubungan7+" + "+jenisK7+" + "+getTempatLahir7+" + "+tglL7+" + "+getAgama7);
                                                                    Log.w("Nilai", "8 "+getNama8+" + "+getNik8+" + "+getStatusHubungan8+" + "+jenisK8+" + "+getTempatLahir8+" + "+tglL8+" + "+getAgama8);
                                                                    Log.w("Nilai", "9 "+getNama9+" + "+getNik9+" + "+getStatusHubungan9+" + "+jenisK9+" + "+getTempatLahir9+" + "+tglL9+" + "+getAgama9);
                                                                    Log.w("Nilai", "10 "+getNama10+" + "+getNik10+" + "+getStatusHubungan10+" + "+jenisK10+" + "+getTempatLahir10+" + "+tglL10+" + "+getAgama10);

                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                //failed upload image
                                                                pd.dismiss();
                                                                Toast.makeText(context,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }
                                            else {
                                                pd.dismiss();
                                                Toast.makeText(context, "Masukkan Gambar", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    }
                                    else {
                                        nama10.setText("");
                                        nik10.setText("");
                                        statusHubungan10.setText("");
                                        tempatLahir10.setText("");
                                        agama10.setText("");
                                        tglLahir10.setText("");

                                        Toast.makeText(this, "Isi Anggota Keluarga 7 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    nama10.setText("");
                                    nik10.setText("");
                                    statusHubungan10.setText("");
                                    tempatLahir10.setText("");
                                    agama10.setText("");
                                    tglLahir10.setText("");
                                    getTglLahir10 = "";
                                    getJenisKelamin10 = "";
                                    Toast.makeText(this, "Isi Anggota Keluarga 6 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                nama10.setText("");
                                nik10.setText("");
                                statusHubungan10.setText("");
                                tempatLahir10.setText("");
                                agama10.setText("");
                                tglLahir10.setText("");
                                getTglLahir10 = "";
                                getJenisKelamin10 = "";
                                Toast.makeText(this, "Isi Anggota Keluarga 5 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            nama10.setText("");
                            nik10.setText("");
                            statusHubungan10.setText("");
                            tempatLahir10.setText("");
                            agama10.setText("");
                            tglLahir10.setText("");
                            getTglLahir10 = "";
                            getJenisKelamin10 = "";
                            Toast.makeText(this, "Isi Anggota Keluarga 4 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        nama10.setText("");
                        nik10.setText("");
                        statusHubungan10.setText("");
                        tempatLahir10.setText("");
                        agama10.setText("");
                        tglLahir10.setText("");
                        getTglLahir10 = "";
                        getJenisKelamin10 = "";

                        Toast.makeText(this, "Isi Anggota Keluarga 3 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    nama10.setText("");
                    nik10.setText("");
                    statusHubungan10.setText("");
                    tempatLahir10.setText("");
                    agama10.setText("");
                    tglLahir10.setText("");
                    getTglLahir10 = "";
                    getJenisKelamin10 = "";

                    Toast.makeText(this, "Isi Anggota Keluarga 2 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                nama10.setText("");
                nik10.setText("");
                statusHubungan10.setText("");
                tempatLahir10.setText("");
                agama10.setText("");
                tglLahir10.setText("");
                getJenisKelamin10 = "";
                getTglLahir10 = "";

                Toast.makeText(this, "Isi Anggota Keluarga 1 Terlebih Dahulu", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            Log.w("Nilai", "KLIK5");
            pd.setMessage("Menambahkan data keluarga...");
            pd.show();

            jenisK1 = getJenisKelamin1;
            tglL1 = getTglLahir1;
            jenisK2 = getJenisKelamin2;
            tglL2 = getTglLahir2;
            jenisK3 = getJenisKelamin3;
            tglL3 = getTglLahir3;
            jenisK4 = getJenisKelamin4;
            tglL4 = getTglLahir4;
            jenisK5 = getJenisKelamin5;
            tglL5 = getTglLahir5;
            Log.w("Nilai", "KLIK6");
            jenisK6 = getJenisKelamin6;
            tglL6 = getTglLahir6;
            jenisK7 = getJenisKelamin7;
            tglL7 = getTglLahir7;
            jenisK8 = getJenisKelamin8;
            tglL8 = getTglLahir8;
            jenisK9 = getJenisKelamin9;
            tglL9 = getTglLahir9;
            jenisK10 = getJenisKelamin10;
            tglL10 = getTglLahir10;
            Log.w("Nilai", "KLIK7");
            if (getNama3.equals("")) {
                jenisK3 = "";
                tglL3 = "";
            }
            if (getNama4.equals("")) {
                jenisK4 = "";
                tglL4 = "";
            }
            if (getNama5.equals("")) {
                jenisK5 = "";
                tglL5 = "";
            }
            if (getNama6.equals("")) {
                jenisK6 = "";
                tglL6 = "";
            }
            if (getNama7.equals("")) {
                jenisK7 = "";
                tglL7 = "";
            }
            if (getNama8.equals("")) {
                jenisK8 = "";
                tglL8 = "";
            }
            if (getNama9.equals("")) {
                jenisK9 = "";
                tglL9 = "";
            }
            if (getNama10.equals("")) {
                jenisK10 = "";
                tglL10 = "";
            }


            if (!getImageKK.equals("noImage")) {
                Log.w("Nilai", "KLIK8");
                // Delete foto sebelumnya
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference profileref = storageRef.child("Kartukeluarga/kk_img_" + setIdUserSelect);
                profileref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });
                //post with image
//                final String timeStamp = String.valueOf(System.currentTimeMillis());
                String filePathAndName = "Kartukeluarga/" + "kk_img_" + setIdUserSelect;
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(getImageKK))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //image is uploaded to firebase storage, now get it's uri
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful());
                                final String downloadUri = uriTask.getResult().toString();

                                if (uriTask.isSuccessful()) {
                                    Log.w("Nilai", "KLIK9");
                                    //uri is received upload post to firebase database
                                    //path to store post data
                                    //referen untuk tabel Keluarga
                                    databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");

                                    String id = databaseKeluarga.push().getKey();

                                    //we will store the additional fields in firebase database
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("keluargaId", id);
                                    hashMap.put("userId", setIdUserSelect);
                                    hashMap.put("imageKK", downloadUri) ;
                                    Log.w("URI",""+downloadUri);
                                    hashMap.put("noRumah", getNoRumah);
                                    hashMap.put("noKK", getNoKK);

                                    hashMap.put("namaKK", getNamaKK);
                                    hashMap.put("nik1", getNik1);
                                    hashMap.put("jenisKelamin1", jenisK1);
                                    hashMap.put("tempatLahir1", getTempatLahir1);
                                    hashMap.put("tglLahir1", tglL1);
                                    hashMap.put("agama1", getAgama1);

                                    hashMap.put("namaIstri", getNamaIstri);
                                    hashMap.put("nik2", getNik2);
                                    hashMap.put("statusHubungan2", getStatusHubungan2);
                                    hashMap.put("jenisKelamin2", jenisK2);
                                    hashMap.put("tempatLahir2", getTempatLahir2);
                                    hashMap.put("tglLahir2", tglL2);
                                    hashMap.put("agama2", getAgama2);

                                    hashMap.put("nama3", getNama3);
                                    hashMap.put("nik3", getNik3);
                                    hashMap.put("statusHubungan3", getStatusHubungan3);
                                    hashMap.put("jenisKelamin3", jenisK3);
                                    hashMap.put("tempatLahir3", getTempatLahir3);
                                    hashMap.put("tglLahir3", tglL3);
                                    hashMap.put("agama3", getAgama3);

                                    hashMap.put("nama4", getNama4);
                                    hashMap.put("nik4", getNik4);
                                    hashMap.put("statusHubungan4", getStatusHubungan4);
                                    hashMap.put("jenisKelamin4", jenisK4);
                                    hashMap.put("tempatLahir4", getTempatLahir4);
                                    hashMap.put("tglLahir4", tglL4);
                                    hashMap.put("agama4", getAgama4);

                                    hashMap.put("nama5", getNama5);
                                    hashMap.put("nik5", getNik5);
                                    hashMap.put("statusHubungan5", getStatusHubungan5);
                                    hashMap.put("jenisKelamin5", jenisK5);
                                    hashMap.put("tempatLahir5", getTempatLahir5);
                                    hashMap.put("tglLahir5", tglL5);
                                    hashMap.put("agama5", getAgama5);

                                    hashMap.put("nama6", getNama6);
                                    hashMap.put("nik6", getNik6);
                                    hashMap.put("statusHubungan6", getStatusHubungan6);
                                    hashMap.put("jenisKelamin6", jenisK6);
                                    hashMap.put("tempatLahir6", getTempatLahir6);
                                    hashMap.put("tglLahir6", tglL6);
                                    hashMap.put("agama6", getAgama6);

                                    hashMap.put("nama7", getNama7);
                                    hashMap.put("nik7", getNik7);
                                    hashMap.put("statusHubungan7", getStatusHubungan7);
                                    hashMap.put("jenisKelamin7", jenisK7);
                                    hashMap.put("tempatLahir7", getTempatLahir7);
                                    hashMap.put("tglLahir7", tglL7);
                                    hashMap.put("agama7", getAgama7);

                                    hashMap.put("nama8", getNama8);
                                    hashMap.put("nik8", getNik8);
                                    hashMap.put("statusHubungan8", getStatusHubungan8);
                                    hashMap.put("jenisKelamin8", jenisK8);
                                    hashMap.put("tempatLahir8", getTempatLahir8);
                                    hashMap.put("tglLahir8", tglL8);
                                    hashMap.put("agama8", getAgama8);

                                    hashMap.put("nama9", getNama9);
                                    hashMap.put("nik9", getNik9);
                                    hashMap.put("statusHubungan9", getStatusHubungan9);
                                    hashMap.put("jenisKelamin9", jenisK9);
                                    hashMap.put("tempatLahir9", getTempatLahir9);
                                    hashMap.put("tglLahir9", tglL9);
                                    hashMap.put("agama9", getAgama9);

//                                    Log.w("Nilai", "KLIK10");

                                    hashMap.put("nama10", getNama10);
                                    hashMap.put("nik10", getNik10);
                                    hashMap.put("statusHubungan10", getStatusHubungan10);
                                    hashMap.put("jenisKelamin10", jenisK10);
                                    hashMap.put("tempatLahir10", getTempatLahir10);
                                    hashMap.put("tglLahir10", tglL10);
                                    hashMap.put("agama10", getAgama10);
//                                    Log.w("Nilai", "KLIK11");
                                    uploadData(id, hashMap);
//                                    Log.w("Nilai", "3 "+getNama3+" + "+getNik3+" + "+getStatusHubungan3+" + "+jenisK3+" + "+getTempatLahir3+" + "+tglL3+" + "+getAgama3);
//                                    Log.w("Nilai", "4 "+getNama4+" + "+getNik4+" + "+getStatusHubungan4+" + "+jenisK4+" + "+getTempatLahir4+" + "+tglL4+" + "+getAgama4);
//                                    Log.w("Nilai", "5 "+getNama5+" + "+getNik5+" + "+getStatusHubungan5+" + "+jenisK5+" + "+getTempatLahir5+" + "+tglL5+" + "+getAgama5);
//                                    Log.w("Nilai", "6 "+getNama6+" + "+getNik6+" + "+getStatusHubungan6+" + "+jenisK6+" + "+getTempatLahir6+" + "+tglL6+" + "+getAgama6);
//                                    Log.w("Nilai", "7 "+getNama7+" + "+getNik7+" + "+getStatusHubungan7+" + "+jenisK7+" + "+getTempatLahir7+" + "+tglL7+" + "+getAgama7);
//                                    Log.w("Nilai", "8 "+getNama8+" + "+getNik8+" + "+getStatusHubungan8+" + "+jenisK8+" + "+getTempatLahir8+" + "+tglL8+" + "+getAgama8);
//                                    Log.w("Nilai", "9 "+getNama9+" + "+getNik9+" + "+getStatusHubungan9+" + "+jenisK9+" + "+getTempatLahir9+" + "+tglL9+" + "+getAgama9);
//                                    Log.w("Nilai", "10 "+getNama10+" + "+getNik10+" + "+getStatusHubungan10+" + "+jenisK10+" + "+getTempatLahir10+" + "+tglL10+" + "+getAgama10);

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //failed upload image
                                pd.dismiss();
                                Toast.makeText(context,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
            else {
                pd.dismiss();
                Toast.makeText(context, "Masukkan Gambar", Toast.LENGTH_SHORT).show();
                return;
            }
        }

//        progressBar.setVisibility(View.VISIBLE);
//        //referen untuk tabel Keluarga
//        databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");
//
//        final String id = databaseKeluarga.push().getKey();
//
//        we will store the additional fields in firebase database
//        ModelKeluarga user = new ModelKeluarga(
//                getNoRumah,
//                getNoKK,
//                getNamaKepalaKeluarga,
//                getNikNamaKK,
//                getNamaIstri,
//                getNikIstri,
//                getNamaAnak,
//                getNikAnak
//        );
//
//        databaseKeluarga.child(id).setValue(user)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        if (task.isSuccessful()){
//                            noRumah.setText("");
//                            noKK.setText("");
//                            namaKepalaKeluarga.setText("");
//                            nikNamaKK.setText("");
//                            namaIstri.setText("");
//                            nikIstri.setText("");
//                            namaAnak.setText("");
//                            nikAnak.setText("");
//
//                            Toast.makeText(TambahDataKeluargaWargaActivity.this, "Berhasil menambah Keluarga",Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(TambahDataKeluargaWargaActivity.this, ListDataKeluargaWargaActivity.class));
//                            finish();
//                        } else {
//                            //display failure message
//                            Toast.makeText(TambahDataKeluargaWargaActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
    }

    private void uploadData(String id, HashMap<String, Object> hashMap) {
        Log.w("Nilai", "KLIK12");

        databaseKeluarga.child(id).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
//                Log.w("Nilai", "KLIK13");
                Toast.makeText(context, "Berhasil menambah data Keluarga", Toast.LENGTH_SHORT).show();

                //reset views

                imageKK.setImageDrawable(getResources().getDrawable(R.drawable.icon_kamera));
                imageKK.setImageURI(null);
                image_uri = null;

                onSupportNavigateUp();
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding post in databse
                        pd.dismiss();
                        Toast.makeText(context,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        onSupportNavigateUp();
                        finish();
                    }
                });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }
}
