package com.tafh.tugasakhir.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.ProgressDialogHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.WelcomeActivity;
import com.tafh.tugasakhir.akun.AkunDetailActivity;
import com.tafh.tugasakhir.akun.ListDataAkunActivity;
import com.tafh.tugasakhir.berita.AddPostActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.isEmpty;

public class SettingProfileActivity extends AppCompatActivity {

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;

    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constats
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;


    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //image picked will be samed in this uri
    private Uri image_uri = null;

    private static final int PICK_IMAGE = 1;
    private CircleImageView mImageBtn;
    private Uri imageUri;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;

    private EditText mNoHpUserBaru, mPasswordUserBaru;
    private TextView  mEmailUserBaru, mNamaUserBaru, mNoKKuserBaru, mNoRumahUserBaru, mUserType;
    private FirebaseAuth auth;
    private Button btnSimpan, btnBatal;

    private String gmailLama, passwordLama, cekNama, cekNoRUmah, cekNoKK, cekNoHp, cekGmail, cekPassword,cekUserType, cekCover, cekSearch;

    private ProgressDialog pd;

    private String myUserId;
    private String getImage, getNoRumah, getUserType, getNama, getEmail, getPassword, getNoHp, getNoKK, getUserId;
    private String setNama, setEmail,setPassword,setNoHp,setNoKK,setNoRumah,setUserType;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        pd = new ProgressDialog(this);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle("Ubah Profile");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();


//        imageUri = null;
//        mStorage = FirebaseStorage.getInstance().getReference().child("images");
//        mFirestore = FirebaseFirestore.getInstance();

        mImageBtn = findViewById(R.id.btn_img_profile);
        //ambil gambar
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
//                Intent imageIntent = new Intent();
//                imageIntent.setType("image/*");
//                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), PICK_IMAGE);
            }
        });

        mNoRumahUserBaru = findViewById(R.id.tv_norumah);
        mNamaUserBaru = findViewById(R.id.tv_nama_user);
        mEmailUserBaru = findViewById(R.id.tv_gmail_user);
        mPasswordUserBaru = findViewById(R.id.et_password_user);

        mNoHpUserBaru = findViewById(R.id.et_no_hp_user);
        mNoKKuserBaru = findViewById(R.id.tv_no_kk_user);
        mUserType = findViewById(R.id.tv_user_type);

        setView();

        btnSimpan = findViewById(R.id.btn_simpan_akun);
        btnBatal = findViewById(R.id.btn_batal_akun);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
                finish();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String gImage;
                final String imageStatus;

                cekNama = mNamaUserBaru.getText().toString();
                cekNoRUmah = mNoRumahUserBaru.getText().toString();
                cekNoKK = mNoKKuserBaru.getText().toString();

                cekNoHp = mNoHpUserBaru.getText().toString();
                gmailLama = auth.getCurrentUser().getEmail();
                cekGmail = mEmailUserBaru.getText().toString();
                cekPassword = mPasswordUserBaru.getText().toString();
                cekUserType = mUserType.getText().toString();
                cekSearch = "noSearch";
                cekCover = "noCover";

                //Mengecek agar tidak ada data yang kosong, saat proses update

                if (TextUtils.isEmpty(cekGmail)) {
                    mEmailUserBaru.setError("Email tidak boleh kosong!");
                    mEmailUserBaru.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(cekGmail).matches()) {
                    mEmailUserBaru.setError("Masukan email yang sesuai!");
                    mEmailUserBaru.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(cekPassword)) {
                    mPasswordUserBaru.setError("Password tidak boleh kosong!");
                    mPasswordUserBaru.requestFocus();
                    return;
                }

                if (cekPassword.length() < 6) {
                    mPasswordUserBaru.setError(getString(R.string.minimum_password));
                    mPasswordUserBaru.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(cekNoHp)) {
                    mNoHpUserBaru.setError("No Hp tidak boleh kosong!");
                    mNoHpUserBaru.requestFocus();
                    return;
                }
                if (cekNoHp.length() <= 10) {
                    mNoHpUserBaru.setError("Masukkan Nomor Hp yang sesuai!");
                    mNoHpUserBaru.requestFocus();
                    return;
                }

                if (image_uri == null) {
                    //tidak memilih gambar, gunakan gambar sebelumnya
                    imageStatus = "noImage";
                    if (getImage != null) {
                        gImage = getImage;
                    }
                    else {
                        gImage = "noImage";
                    }
                }
                else {
                    imageStatus = "withImage";
                    gImage = String.valueOf(image_uri);
                    //post with image
                }

                Log.w("Nilai", ""+gImage);
                Log.w("Nilai", ""+imageStatus);

                if (!imageStatus.equals("noImage")) {
                    //post without image

                    Log.w("NILAI_PASSWORD","dengan gambar "+cekPassword+" + "+cekNoHp);
                    Log.w("NILAI_PASSWORD", ""+gImage);
                    ubahData(cekNama, cekNoRUmah, cekNoKK, cekNoHp, cekGmail, cekPassword, cekUserType, imageStatus, gImage, cekSearch, cekCover);
                }
                else {

                    //post with image
                    Log.w("NILAI_PASSWORD","tanpa gambar : "+cekPassword+" + "+cekNoHp);
                    Log.w("NILAI_PASSWORD", ""+gImage);
                    ubahData(cekNama, cekNoRUmah, cekNoKK, cekNoHp, cekGmail, cekPassword, cekUserType, imageStatus, gImage, cekSearch, cekCover);
                }
//                if (imageUri != null) {

//                        final StorageReference userProfileRef = mStorage.child(userId + ".jpg");
//
//                        userProfileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                userProfileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        Uri downloadUrl = uri;
//                                        //upload sukses
//                                        //taruh data di firestore
//                                        Map<String, Object> file = new HashMap<>();
//                                        file.put("image", downloadUrl.toString());
//
//                                        mFirestore.collection("Users").document(userId).set(file).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                //sukses
//                                                final ModelUser Akun = new ModelUser();
//                                                Akun.setNoRumah(cekNoRUmah);
//                                                Akun.setNoKK(cekNoKK);
//                                                Akun.setNamaKepalaKeluarga(cekNama);
//                                                Akun.setNoHp(cekNoHp);
//                                                Akun.setGmail(cekGmail);
//                                                Akun.setPassword(cekPassword);
//                                                Akun.setUserType(cekUserType);
//
//                                                //ubah database firebase nya
//                                                updateAkun(Akun, userId);
//
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//
//                                                Toast.makeText(SettingProfileActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        });
//
//                                    }
//                                });
//                            }
//                        });
            }

        });
    }



    private void ubahData(final String gNama, final String gNoRUmah, final String gNoKK,
                          final String gNoHp, final String gGmail, final String gPassword,
                          final String gUserType, String imageStatus, final String gUri, final String gSearch, final String gCover ){
        pd.setMessage("Mengubah profile...");
        pd.show();
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        final FirebaseUser user = auth.getCurrentUser();
        Log.w("NILAI_PASSWORD","USER "+user);
        final String password = getPassword;
        final String email = getEmail;

        Log.w("NILAI_PASSWORD",""+getPassword);

        if (!imageStatus.equals("noImage")) {
            //dengan gambar baru
            if (gPassword.equals(getPassword)) {
                //upload tanpa ganti password
                pd.setMessage("Mengupload foto...");
                pd.show();

                Log.w("NILAI_PASSWORD","2 "+gPassword);

                Log.w("NILAI_PASSWORD","3 "+getPassword);
                // Delete foto sebelumnya
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference profileref = storageRef.child("Users/user_profile_" + myUserId);
                profileref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });

                //post with image

                //for post-image name, post-id, post-publish-time
                String filePathAndName = "Users/" + "user_profile_" + myUserId;
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(gUri))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to firebase storage, now get it's uri
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());

                        final String downloadUri = uriTask.getResult().toString();

                        if (uriTask.isSuccessful()) {
                            //tanpa ganti password tapi dengan gambar
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("userId", myUserId);
                            hashMap.put("userType", gUserType);
                            hashMap.put("noRumah", gNoRUmah);
                            hashMap.put("noKK", gNoKK);
                            hashMap.put("namaKepalaKeluarga", gNama);
                            hashMap.put("noHp", gNoHp);
                            hashMap.put("gmail", gGmail);

                            hashMap.put("password", gPassword);
                            hashMap.put("img", downloadUri);
//                                    hashMap.put("search", gSearch);
//                                    hashMap.put("cover", gCover);
                            hashMap.put("onlineStatus", "online");

                            updateAkun(hashMap, gPassword);

                            pd.dismiss();

                            onSupportNavigateUp();

                        } else {
                            //Error
                            pd.dismiss();
                            Log.w("Error", "Gagal upload image");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed upload image
                        pd.dismiss();
                        Toast.makeText(SettingProfileActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                pd.dismiss();
                //upload dengan ganti password
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Jika mengubah password, anda harus login kembali");
                //set layout linier layout
                LinearLayout linearLayout = new LinearLayout(this);
                //views to set in dialog
                final EditText etPasswordKonfirmasi = new EditText(this);
                etPasswordKonfirmasi.setHint("Masukkan password lama anda");
                etPasswordKonfirmasi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etPasswordKonfirmasi.setFocusable(true);
                etPasswordKonfirmasi.setClickable(true);
                etPasswordKonfirmasi.setCursorVisible(true);
                etPasswordKonfirmasi.setEnabled(true);

                linearLayout.addView(etPasswordKonfirmasi);
                linearLayout.setPadding(32, 10, 10, 10);
                builder.setView(linearLayout);

                //buttons recover
                builder.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pd.setMessage("Mengupload foto...");
                        pd.show();

                        //Yes button clicked

                        Log.w("NILAI_PASSWORD","2 "+gPassword);
                        Log.w("NILAI_PASSWORD","3 "+getPassword);
                        // Delete foto sebelumnya
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference profileref = storageRef.child("Users/user_profile_" + myUserId);
                        profileref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //nocmmand
                            }
                        });

                        //for post-image name, post-id, post-publish-time
                        String filePathAndName = "Users/" + "user_profile_" + myUserId;

                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);

                        ref.putFile(Uri.parse(gUri))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //image is uploaded to firebase storage, now get it's uri
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful());

                                        final String downloadUri = uriTask.getResult().toString();

                                        if (uriTask.isSuccessful()) {
                                            pd.setMessage("Merubah password...");
                                            pd.show();
                                            String password = etPasswordKonfirmasi.getText().toString().trim();
                                            final AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                                            //ganti password
                                            // Prompt the user to re-provide their sign-in credentials
                                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        Log.w("NILAI_PASSWORD","3 "+gPassword);
                                                        user.updatePassword(gPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    //password suudah diganti
                                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                                    hashMap.put("userId", myUserId);
                                                                    hashMap.put("userType", gUserType);
                                                                    hashMap.put("noRumah", gNoRUmah);
                                                                    hashMap.put("noKK", gNoKK);
                                                                    hashMap.put("namaKepalaKeluarga", gNama);
                                                                    hashMap.put("noHp", gNoHp);
                                                                    hashMap.put("gmail", gGmail);
                                                                    hashMap.put("password", gPassword);
                                                                    hashMap.put("img", downloadUri);
                                                                    //                                    hashMap.put("search", gSearch);
                                                                    //                                    hashMap.put("cover", gCover);
                                                                    hashMap.put("onlineStatus", "online");
                                                                    updateAkun(hashMap, gPassword);

                                                                }
                                                                else {
                                                                    pd.dismiss();
                                                                    Log.w("Error", "Error password not updated");
                                                                }

                                                            }
                                                        });
                                                    }
                                                    else {
                                                        pd.dismiss();
                                                        Log.w("Error", "Password salah");

                                                        Toast.makeText(SettingProfileActivity.this,"Password lama anda salah!", Toast.LENGTH_SHORT).show();
                                                        onSupportNavigateUp();
                                                        finish();
                                                    }
                                                }
                                            });

                                        }
                                        else {
                                            //Error
                                            pd.dismiss();
                                            Log.w("Error", "Gagal upload image");
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed upload image
                                        pd.dismiss();
                                        Toast.makeText(SettingProfileActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                //buttons cancel
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss
                        pd.dismiss();

                        mPasswordUserBaru.setText(getPassword);
                    }
                });
                //show dialog
                builder.create().show();


//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case DialogInterface.BUTTON_POSITIVE:
//                                //Yes button clicked
//
//                                Log.w("NILAI_PASSWORD","2 "+gPassword);
//                                Log.w("NILAI_PASSWORD","3 "+getPassword);
//                                // Delete foto sebelumnya
//                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                                StorageReference profileref = storageRef.child("Users/user_profile_" + myUserId);
//                                profileref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        //nocmmand
//                                    }
//                                });
//
//                                //for post-image name, post-id, post-publish-time
//                                String filePathAndName = "Users/" + "user_profile_" + myUserId;
//
//                                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
//                                ref.putFile(Uri.parse(gUri))
//                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                //image is uploaded to firebase storage, now get it's uri
//                                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                                while (!uriTask.isSuccessful());
//
//                                                final String downloadUri = uriTask.getResult().toString();
//
//                                                if (uriTask.isSuccessful()) {
//
//                                                    //ganti password
//                                                    // Prompt the user to re-provide their sign-in credentials
//                                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()){
//
//                                                                Log.w("NILAI_PASSWORD","3 "+gPassword);
//                                                                user.updatePassword(gPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                                        if (task.isSuccessful()) {
//
//                                                                            //password suudah diganti
//                                                                            HashMap<String, Object> hashMap = new HashMap<>();
//                                                                            hashMap.put("userId", myUserId);
//                                                                            hashMap.put("userType", gUserType);
//                                                                            hashMap.put("noRumah", gNoRUmah);
//                                                                            hashMap.put("noKK", gNoKK);
//                                                                            hashMap.put("namaKepalaKeluarga", gNama);
//                                                                            hashMap.put("noHp", gNoHp);
//                                                                            hashMap.put("gmail", gGmail);
//                                                                            hashMap.put("password", gPassword);
//                                                                            hashMap.put("img", downloadUri);
//                                                                            //                                    hashMap.put("search", gSearch);
//                                                                            //                                    hashMap.put("cover", gCover);
//                                                                            hashMap.put("onlineStatus", "online");
//                                                                            updateAkun(hashMap, gPassword);
//
//                                                                        }
//                                                                        else {
//                                                                            pd.dismiss();
//                                                                            Log.w("Error", "Error password not updated");
//                                                                        }
//
//                                                                    }
//                                                                });
//                                                            }
//                                                            else {
//                                                                pd.dismiss();
//                                                                Log.w("Error", "Error password not updated");
//                                                            }
//                                                        }
//                                                    });
//
//                                                }
//                                                else {
//                                                    //Error
//                                                    pd.dismiss();
//                                                    Log.w("Error", "Gagal upload image");
//                                                }
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                //failed upload image
//                                                pd.dismiss();
//                                                Toast.makeText(SettingProfileActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                pd.dismiss();
//                                //No button clicked
//                                break;
//                        }
//                    }
//                };
//
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingProfileActivity.this);
//                builder.setMessage("Masukkan kembali password lama anda!").setPositiveButton("Ya", dialogClickListener)
//                        .setNegativeButton("Tidak", dialogClickListener).show();

            }

        }
        //tidak pake gambar (tanpa ganti password, dengan ganti password)
        else {
            //tanpa gambar baru
            if (gPassword.equals(getPassword)) {
                //tanpa ganti password tanpa gambar
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", myUserId);
                hashMap.put("userType", gUserType);
                hashMap.put("noRumah", gNoRUmah);
                hashMap.put("noKK", gNoKK);
                hashMap.put("namaKepalaKeluarga", gNama);
                hashMap.put("noHp", gNoHp);
                hashMap.put("gmail", gGmail);
                Log.w("NILAI_PASSWORD",""+gPassword);
                hashMap.put("password", gPassword);
                hashMap.put("img", gUri);
//                                    hashMap.put("search", gSearch);
//                                    hashMap.put("cover", gCover);
                hashMap.put("onlineStatus", "online");
                updateAkun(hashMap, gPassword);

                pd.dismiss();

                onSupportNavigateUp();

            }

            else {
                //ganti password tanpa gambar

                pd.dismiss();
                //upload dengan ganti password
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Jika mengubah password, anda harus login kembali");
                //set layout linier layout
                LinearLayout linearLayout = new LinearLayout(this);
                //views to set in dialog
                final EditText etPasswordKonfirmasi = new EditText(this);
                etPasswordKonfirmasi.setHint("Masukkan password lama anda");
                etPasswordKonfirmasi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etPasswordKonfirmasi.setFocusable(true);
                etPasswordKonfirmasi.setClickable(true);
                etPasswordKonfirmasi.setCursorVisible(true);
                etPasswordKonfirmasi.setEnabled(true);

                linearLayout.addView(etPasswordKonfirmasi);
                linearLayout.setPadding(32, 10, 10, 10);
                builder.setView(linearLayout);


                Log.w("NILAI_PASSWORD","2 "+gPassword+" + "+gNoHp); //password baru + nohp
                Log.w("NILAI_PASSWORD","3 "+getPassword); //password lama

                //buttons recover
                builder.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pd.setMessage("Mengubah password....");
                        pd.show();

                        //Yes button clicked

                        Log.w("NILAI_PASSWORD","2 "+gPassword);
                        Log.w("NILAI_PASSWORD","3 "+getPassword);


                        String password = etPasswordKonfirmasi.getText().toString().trim();
                        final AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                        //ganti password
                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    Log.w("NILAI_PASSWORD","4 "+gPassword);
                                    user.updatePassword(gPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                //password suudah diganti
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("userId", myUserId);
                                                hashMap.put("userType", gUserType);
                                                hashMap.put("noRumah", gNoRUmah);
                                                hashMap.put("noKK", gNoKK);
                                                hashMap.put("namaKepalaKeluarga", gNama);
                                                hashMap.put("noHp", gNoHp);
                                                hashMap.put("gmail", gGmail);
                                                hashMap.put("password", gPassword);
                                                hashMap.put("img", gUri);
                                                hashMap.put("onlineStatus", "online");
                                                updateAkun(hashMap, gPassword);

                                            }
                                            else {
                                                pd.dismiss();
                                                Log.w("Error", "Error password not updated");
                                            }

                                        }
                                    });
                                }
                                else {
                                    pd.dismiss();
                                    Log.w("Error", "Password salah");

                                    Toast.makeText(SettingProfileActivity.this,"Password lama anda salah!", Toast.LENGTH_SHORT).show();
                                    onSupportNavigateUp();
                                    finish();
                                }
                            }
                        });
                    }
                });
                //buttons cancel
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss

                        pd.dismiss();
                        mPasswordUserBaru.setText(getPassword);

                    }
                });
                //show dialog
                builder.create().show();


//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case DialogInterface.BUTTON_POSITIVE:
//                                //Yes button clicked
//                                final AuthCredential credential = EmailAuthProvider.getCredential(email,password);
//
//                                // Prompt the user to re-provide their sign-in credentials
//                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//
//                                                    Log.w("NILAI_PASSWORD","4 "+gPassword);
//                                                        user.updatePassword(gPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                                        if (task.isSuccessful()) {
//
//                                                                            //password suudah diganti
//                                                                            HashMap<String, Object> hashMap = new HashMap<>();
//                                                                            hashMap.put("userId", myUserId);
//                                                                            hashMap.put("userType", gUserType);
//                                                                            hashMap.put("noRumah", gNoRUmah);
//                                                                            hashMap.put("noKK", gNoKK);
//                                                                            hashMap.put("namaKepalaKeluarga", gNama);
//                                                                            hashMap.put("noHp", gNoHp);
//                                                                            hashMap.put("gmail", gGmail);
//                                                                            hashMap.put("password", gPassword);
//                                                                            hashMap.put("img", gUri);
//                                                                            hashMap.put("onlineStatus", "online");
//
//                                                                            //                                    hashMap.put("search", gSearch);
//                                                                            //                                    hashMap.put("cover", gCover);
//                                                                            Log.w("NILAI_PASSWORD","5 "+gPassword);
//                                                                            updateAkun(hashMap, gPassword);
//
//
//                                                                        }
//                                                                        else {
//                                                                            pd.dismiss();
//                                                                            Log.w("Error", "Error password not updated");
//                                                                        }
//
//                                                                    }
//                                                                });
//                                                }
//                                                else {
//                                                    pd.dismiss();
//                                                    Log.w("Error", "Error password not updated");
//                                                }
//                                            }
//                                        });
//
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                pd.dismiss();
//                                //No button clicked
//                                break;
//                        }
//                    }
//                };
//
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingProfileActivity.this);
//                builder.setMessage("Jika password di ganti anda harus login kembali!").setPositiveButton("Ya", dialogClickListener)
//                        .setNegativeButton("Tidak", dialogClickListener).show();
            }

//            HashMap<Object, String> hashMap = new HashMap<>();
//            hashMap.put("userId", myUserId);
//            hashMap.put("userType", cekUserType);
//            hashMap.put("noRumah", cekNoRUmah);
//            hashMap.put("noKK", cekNoKK);
//            hashMap.put("namaKepalaKeluarga", cekNama);
//            hashMap.put("noHp", cekNoHp);
//            hashMap.put("gmail", cekGmail);
//            hashMap.put("password", cekPassword);
//            hashMap.put("img", "noImage");
//            hashMap.put("search", cekSearch);
//            hashMap.put("cover", cekCover);
//            hashMap.put("onlineStatus", "online");
//
//
//            updateAkun(hashMap);
//            final ModelUser Akun = new ModelUser();
//            Akun.setUserType(cekUserType);
//            Akun.setNoRumah(cekNoRUmah);
//            Akun.setNoKK(cekNoKK);
//            Akun.setNamaKepalaKeluarga(cekNama);
//            Akun.setNoHp(cekNoHp);
//            Akun.setGmail(cekGmail);
//            Akun.setPassword(cekPassword);
//            Akun.setImg("noImage");
//            Akun.setSearch(cekSearch);
//            Akun.setCover(cekCover);
        }

    }

    private void updateAkun(HashMap<String, Object> hashMap, final String gPassword) {
//        Toast.makeText(SettingProfileActivity.this, "nama " + userId,Toast.LENGTH_SHORT).show();

        DatabaseReference databaseUsers;
        if (!getPassword.equals(gPassword)) {
            auth.signOut();
            Log.w("NILAI_PASSWORD","Berhasil logout");

            databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
            Log.w("NILAI_PASSWORD","6 "+myUserId);
            databaseUsers.child(myUserId).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onSupportNavigateUp();
                            Log.w("NILAI_PASSWORD","Berhasil Update password");
                            Toast.makeText(SettingProfileActivity.this, "Data berhasil diubah, Silahkan Login kembali", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
//                            Log.w("NILAI_PASSWORD","SUKSES");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Log.w("NILAI_PASSWORD", "GAGAL"+e);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("NILAI_PASSWORD","GAGAL"+e);
                }
            });
            Log.w("NILAI_PASSWORD","....");
//
//
//                            Log.w("NILAI_PASSWORD","signout berhasil");
//                            auth.signInWithEmailAndPassword(getEmail, gPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                    Log.w("NILAI_PASSWORD","login kembali berhasil");
//
//                                    finish();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("LOG", ""+e);
//
//                                    Log.w("NILAI_PASSWORD","login tidak berhasil berhasil");
//                                }
//                            });
//
//                            auth.signOut();
//                            checkUserStatus();
        }
        else {
            databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
            Log.w("NILAI_PASSWORD","6 "+myUserId);
            databaseUsers.child(myUserId).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.w("NILAI_PASSWORD","SUKSES");

                            Toast.makeText(SettingProfileActivity.this, "Data Berhasil di Ubah", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
//


//                        pd.dismiss();

//                        mNoRumahUserBaru.setText(setNoRumah);
//                        mNamaUserBaru.setText(setNama);
//                        mEmailUserBaru.setText(setEmail);
//                        Log.w("NILAI_PASSWORD","7"+setPassword);
//                        mPasswordUserBaru.setText(setPassword);
//
//                        mNoHpUserBaru.setText(setNoHp);
//                        mNoKKuserBaru.setText(setNoKK);
//                        mImageBtn.setImageURI(imageUri);


//                        startActivity(new Intent(SettingProfileActivity.this, LoginGmailActivity.class));
//                        finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Log.w("NILAI_PASSWORD", ""+e);
                }
            });
        }
    }


    private void setView() {
        getDataIntent();

        mNoRumahUserBaru.setText(getNoRumah);
        mNamaUserBaru.setText(getNama);
        mNamaUserBaru.setEnabled(false);
        mNamaUserBaru.setClickable(false);
        mNamaUserBaru.setTextColor(getResources().getColor(R.color.textGrey));
        mEmailUserBaru.setText(getEmail);
        mPasswordUserBaru.setText(getPassword);

        mNoHpUserBaru.setText(getNoHp);
        mNoKKuserBaru.setText(getNoKK);
        mUserType.setText(getUserType);
        myUserId = getUserId;
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        getNoRumah = intent.getStringExtra("I_NORUMAH");
        getImage = intent.getStringExtra("I_IMAGE"); //gambar uri

        getUserType = intent.getStringExtra("I_USERTYPE");
        getNama = intent.getStringExtra("I_NAMA");
        getEmail = intent.getStringExtra("I_EMAIL");

        getPassword = intent.getStringExtra("I_PASWORD");
        getNoHp = intent.getStringExtra("I_NOHP");
        getNoKK = intent.getStringExtra("I_NOKK");
        getUserId = intent.getStringExtra("I_USERID");

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
//            userId = user.getUid();
            Log.w("STATUS_USER","LOGIN "+user);
        } else {

            Log.w("STATUS_USER","SIGNOUT "+user);
            startActivity(new Intent(this, LoginGmailActivity.class));
            finish();
        }
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
                mImageBtn.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                mImageBtn.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }
}
