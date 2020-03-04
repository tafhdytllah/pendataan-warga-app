package com.tafh.tugasakhir.berita;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
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
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDbRef;
    private ActionBar actionBar;
    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;

    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constats
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //views
    private EditText titleEt, descroptionEt;
    private ImageView imageIv;
    private Button uploadBtn, batalBtn;

    //image picked will be samed in this uri
    private Uri image_uri = null;

    //user info
    private String userName, userEmail, userId, userImage;

    //progress bar
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();
        getDataIntent();
//        getDataUser();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Berita Baru");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle(userEmail);


        //init permissions arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);
        //init vies
        titleEt = findViewById(R.id.et_post_title);
        descroptionEt = findViewById(R.id.et_post_deskripsi);
        imageIv = findViewById(R.id.iv_post_image);
        uploadBtn = findViewById(R.id.btn_post_upload);
        batalBtn = findViewById(R.id.btn_batal_upload);

        //get image from camera/gallery on click
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show image pick dialog
                showImagePickDialog();
            }
        });

        batalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
                finish();
            }
        });
        //upload button click listener
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data(title, deskripsi) from EditText
                String title = titleEt.getText().toString().trim();
                String deskripsi = descroptionEt.getText().toString().trim();

                if (TextUtils.isEmpty(title)){
                    titleEt.setError("Masukkan Judul");
                    titleEt.requestFocus();
                    Toast.makeText(AddPostActivity.this, "Judul tidak boleh kosongl!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(deskripsi)) {
                    descroptionEt.setError("Masukkan Deskripsi");
                    descroptionEt.requestFocus();
                    Toast.makeText(AddPostActivity.this, "Deskripsi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (image_uri == null) {
                    //post without image
                    uploadData(title, deskripsi, "noImage");
                }
                else {
                    //post with image
                    uploadData(title, deskripsi, String.valueOf(image_uri));
                }
            }
        });

    }

    private void uploadData(final String title, final String deskripsi, String uri) {
        pd.setMessage("Mengirim berita...");
        pd.show();

        //for post-image name, post-id, post-publish-time
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;
        if (!uri.equals("noImage")) {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to firebase storage, now get it's uri
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());


                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                //uri is received upload post to firebase database
//path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in this ref
                                String id = ref.push().getKey();
                                HashMap<Object, String> hashMap = new HashMap<>();
                                //put post info
                                hashMap.put("postId", timeStamp);
                                hashMap.put("pTitle", title);
                                hashMap.put("pDescr", deskripsi);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);
                                hashMap.put("userId", userId);
                                hashMap.put("userName", userName);
                                hashMap.put("userEmail", userEmail);
                                hashMap.put("userImage", userImage);



                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, "Berita terkirim", Toast.LENGTH_SHORT).show();

                                                //reset views
                                                titleEt.setText("");
                                                descroptionEt.setText("");
                                                imageIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_kamera));
                                                imageIv.setImageURI(null);
                                                image_uri = null;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding post in databse
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed upload image
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else {
            pd.dismiss();
            Toast.makeText(AddPostActivity.this, "Masukkan Gambar", Toast.LENGTH_SHORT).show();
            return;
        }
//            //post withoud image
//            HashMap<Object, String> hashMap = new HashMap<>();
//            //put post info
//            hashMap.put("uid", uid);
//            hashMap.put("uName", nama);
//            hashMap.put("uEmail", gmail );
//            hashMap.put("uDp", dp);
//            hashMap.put("pId", timeStamp);
//            hashMap.put("pTitle", title);
//            hashMap.put("pDescr", deskripsi);
//            hashMap.put("pImage", "noImage");
//            hashMap.put("pTime", timeStamp);
//
//            //path to store post data
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
//            //put data in this ref
//            ref.child(timeStamp).setValue(hashMap)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            pd.dismiss();
//                            Toast.makeText(AddPostActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
//                            //reset views
//                            titleEt.setText("");
//                            descroptionEt.setText("");
//                            imageIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_kamera));
//                            imageIv.setImageURI(null);
//                            image_uri = null;
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            //failed adding post in databse
//                            pd.dismiss();
//                            Toast.makeText(AddPostActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }

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

    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        this method is called when user ressed Allow  or deny from permission request dialog
//        here we will handle permission cases (allowed anddenid)

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        //both permission are granted
                        pickFromCamera();
                    } else {

                        //camera or gallery or both permissions were denied
                        Toast.makeText(this, "Camera & Storage both permissions are neccessary", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }

            } break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length>0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //storage permission are granted
                        pickFromGallery();
                    } else {

                        //camera or gallery or both permissions were denied
                        Toast.makeText(this, "Storage permissions neccessary", Toast.LENGTH_SHORT).show();
                    }
                }
            } break;
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking camera

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery,get uri of image
                image_uri = data.getData();

                //set to imageview
                imageIv.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                imageIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(this, LoginGmailActivity.class));
            finish();
        }
    }



    private void getDataUser() {
        //get some info of current user to include in post
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    userName = ""+ ds.child("namaKepalaKeluarga").getValue();
                    userEmail = ""+ ds.child("gmail").getValue();
                    userImage = ""+ ds.child("img").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void getDataAkunYangLogin() {
//
//
//        final DatabaseReference databaseUsers;
//        final String getEmailUser = auth.getCurrentUser().getEmail();
//        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
//
//        databaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataUser = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    //Mapping data pada DataSnapshot ke dalam objek akun
//                    ModelUser user = snapshot.getValue(ModelUser.class);
//                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
//                    user.setUserid(snapshot.getKey());
//                    dataUser.add(user);
//                }
//
//                int n = dataUser.size();
//                for (int i=0; i<n; i++) {
//
//                    String gmail = dataUser.get(i).getGmail();
//
//                    if (gmail.equals(getEmailUser)){
//                        setNoRumah = dataUser.get(i).getNoRumah();
//                        setUserType = dataUser.get(i).getUserType();
//                        setNamaUser = dataUser.get(i).getNamaKepalaKeluarga();
//                        setEmail = dataUser.get(i).getGmail();
//
//                        setPassword = dataUser.get(i).getPassword();
//                        setNoHp = dataUser.get(i).getNoHp();
//                        setNoKK = dataUser.get(i).getNoKK();
//                        setUserId = myUserId;
//                        setImage = dataUser.get(i).getImg();
//                        if (setImage.equals("noImage")) {
//                            //hide imageview
//                            RequestOptions placeholderOption = new RequestOptions();
//                            placeholderOption.placeholder(R.drawable.iconscamera);
//                        }
//                        else {
//                            try {
//                                Picasso.with(getContext()).load(setImage).into(mProfileImage);
//                            }
//                            catch (Exception e) {
//
//                            }
//                        }
//
//                    }
//                }
////                mFirestore.collection("Users").document(setUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                    @Override
////                    public void onSuccess(DocumentSnapshot documentSnapshot) {
////                        String user_image = documentSnapshot.getString("image");
////
////                        RequestOptions placeholderOption = new RequestOptions();
////                        placeholderOption.placeholder(R.drawable.iconscamera);
////
////                        Glide.with(getContext())
////                                .setDefaultRequestOptions(placeholderOption)
////                                .load(user_image)
////                                .into(mProfileImage);
////                    }
////                });
//
//                mNoRumahUser.setText(setNoRumah);
//                mNamaUser.setText(setNamaUser);
//                mEmailUser.setText(setEmail);
//                mNoHpUser.setText(setNoHp);
//                mNoKkUser.setText(setNoKK);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void getDataIntent() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("MY_USERID");
        userName = intent.getStringExtra("MY_USERNAME");
        userEmail = intent.getStringExtra("MY_USEREMAIL");
        userImage = intent.getStringExtra("MY_USERIMAGE");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }

}
