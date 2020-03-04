package com.tafh.tugasakhir.keluarga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.tafh.tugasakhir.berita.AddPostActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

public class TambahDataKeluargaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;

    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constats
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //image picked will be samed in this uri
    private Uri image_uri = null;

    private ArrayList<ModelUser> userList;

    private List<String> noRumahList;
    private List<String> jenisKelaminList;

    private ArrayAdapter<String> adapterNoRumah;
    private ArrayAdapter<String> adapterJenisKelamin;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseKeluarga;

    private ActionBar actionBar;

    private MaterialSpinner spinnerNoRumah, spinnerjenisKelamin1, spinnerjenisKelamin2;
    private Calendar
            myCalender = Calendar.getInstance(),
            myCalender2 = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date, date2;

    private ImageView imageKK;

    private EditText noKK,
            namaKK, nik1, tempatLahir1, tglLahir1, agama1,
            namaIstri, nik2, statusHubungan2, tempatLahir2, tglLahir2, agama2;

    private String item, noRumahSelect, setIdUserSelect, setImageUserSelect,
            noRumah, jenisKelamin1, jeniskelamin2;

    private Button btn_simpan, btn_batal;


    //progress bar
    private ProgressDialog pd;

    String TAG = "Nilai";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tambah_data_keluarga);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Data Keluarga");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        spinnerNoRumah = findViewById(R.id.spinner_norumah);
        noKK = findViewById(R.id.et_no_kk);
        imageKK = findViewById(R.id.iv_image_tambahkk);
        namaKK = findViewById(R.id.et_nama_kk);
        nik1 = findViewById(R.id.et_nik_1);
        spinnerjenisKelamin1 = findViewById(R.id.spinner_jeniskelamin_1);
        tempatLahir1 = findViewById(R.id.et_tempat_lahir_1);
        tglLahir1 = findViewById(R.id.et_tgl_lahir_1);
        agama1 = findViewById(R.id.et_agama_1);

        namaIstri = findViewById(R.id.et_nama_istri);
        nik2 = findViewById(R.id.et_nik_2);
        statusHubungan2 = findViewById(R.id.et_status_hubungan_2);
        spinnerjenisKelamin2 = findViewById(R.id.spinner_jeniskelamin_2);
        tempatLahir2 = findViewById(R.id.et_tempat_lahir_2);
        tglLahir2 = findViewById(R.id.et_tgl_lahir_2);
        agama2 = findViewById(R.id.et_agama_2);

        btn_simpan = findViewById(R.id.btn_simpan);
        btn_batal = findViewById(R.id.btn_batal);

        layoutSpinner();
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

        btn_simpan.setOnClickListener(this);
        btn_batal.setOnClickListener(this);


    }

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

    }

    private void layoutSpinner() {
        getallNoRumah();
        getJenisKelamin();

        // Spinner Drop down elements
        adapterNoRumah = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noRumahList);
        adapterJenisKelamin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisKelaminList);
        // Drop down layout style - list view with radio button
        adapterNoRumah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterJenisKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner

        spinnerNoRumah.setAdapter(adapterNoRumah);
        spinnerjenisKelamin1.setAdapter(adapterJenisKelamin);
        spinnerjenisKelamin2.setAdapter(adapterJenisKelamin);

        spinnerNoRumah.setOnItemSelectedListener(this);
        spinnerjenisKelamin1.setOnItemSelectedListener(this);
        spinnerjenisKelamin2.setOnItemSelectedListener(this);


    }

    private void getJenisKelamin() {
        jenisKelaminList = new ArrayList<String>();
        jenisKelaminList.add("Laki-Laki");
        jenisKelaminList.add("Perempuan");

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        item = adapterView.getItemAtPosition(position).toString();


        switch (adapterView.getId()) {
            case R.id.spinner_norumah:
                noRumahSelect = item;
                if (!noRumahSelect.equals("No Rumah")) {
                    getallakun();
                } else {
                    noRumah = item;
                }
                break;
            case R.id.spinner_jeniskelamin_1:
                jenisKelamin1 = item;
                break;
            case R.id.spinner_jeniskelamin_2:
                jeniskelamin2 = item;
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
                Toast.makeText(TambahDataKeluargaActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_simpan:
                //tambah data user kemudian di registerkan gmail nya.
                addKeluarga();
                break;

            case R.id.btn_batal:
                onSupportNavigateUp();
                break;

            case R.id.et_tgl_lahir_1:
                new DatePickerDialog(TambahDataKeluargaActivity.this, date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.et_tgl_lahir_2:
                new DatePickerDialog(TambahDataKeluargaActivity.this, date2,
                        myCalender2.get(Calendar.YEAR),
                        myCalender2.get(Calendar.MONTH),
                        myCalender2.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void addKeluarga() {
        final String getImageKK;
        final String getNoRumah = noRumah.trim();
        final String getNoKK = noKK.getText().toString().trim();

        final String getNamaKK = namaKK.getText().toString().trim();
        final String getNik1 = nik1.getText().toString().trim();
        final String getJenisKelamin1 = jenisKelamin1.trim();
        final String getTempatLahir1 = tempatLahir1.getText().toString().trim();
        final String getTglLahir1 = tglLahir1.getText().toString().trim();
        final String getAgama1 = agama1.getText().toString().trim();

        final String getNamaIstri = namaIstri.getText().toString().trim();
        final String getNik2 = nik2.getText().toString().trim();
        final String getStatusHubungan2 = statusHubungan2.getText().toString().trim();
        final String getJenisKelamin2 = jeniskelamin2.trim();
        final String getTempatLahir2 = tempatLahir2.getText().toString().trim();
        final String getTglLahir2 = tglLahir2.getText().toString().trim();
        final String getAgama2 = agama2.getText().toString().trim();

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
        else {
            pd.setMessage("Menambahkan data keluarga...");
            pd.show();

            if (!getImageKK.equals("noImage")) {
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
                                    hashMap.put("jenisKelamin1", getJenisKelamin1);
                                    hashMap.put("tempatLahir1", getTempatLahir1);
                                    hashMap.put("tglLahir1", getTglLahir1);
                                    hashMap.put("agama1", getAgama1);

                                    hashMap.put("namaIstri", getNamaIstri);
                                    hashMap.put("nik2", getNik2);
                                    hashMap.put("statusHubungan2", getStatusHubungan2);
                                    hashMap.put("jenisKelamin2", getJenisKelamin2);
                                    hashMap.put("tempatLahir2", getTempatLahir2);
                                    hashMap.put("tglLahir2", getTglLahir2);
                                    hashMap.put("agama2", getAgama2);

                                    uploadData(id, hashMap);

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //failed upload image
                                pd.dismiss();
                                Toast.makeText(TambahDataKeluargaActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
            else {
                pd.dismiss();
                Toast.makeText(TambahDataKeluargaActivity.this, "Masukkan Gambar", Toast.LENGTH_SHORT).show();
                return;
            }
//            ModelKeluarga user = new ModelKeluarga(
//                    getNoRumah,
//                    getNoKK,
//                    getNamaKepalaKeluarga,
//                    getNikNamaKK,
//                    getNamaIstri,
//                    getNikIstri,
//                    getNamaAnak,
//                    getNikAnak
//            );
        }
    }

    private void uploadData(String id, HashMap<String, Object> hashMap) {


        databaseKeluarga.child(id).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(TambahDataKeluargaActivity.this, "Berhasil menambah data Keluarga", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(TambahDataKeluargaActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
