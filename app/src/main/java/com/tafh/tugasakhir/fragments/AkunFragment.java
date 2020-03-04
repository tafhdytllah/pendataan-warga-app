package com.tafh.tugasakhir.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.WelcomeActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.profile.ImageDialog;
import com.tafh.tugasakhir.profile.SettingProfileActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AkunFragment extends Fragment {
    private FirebaseFirestore mFirestore;
    private CircleImageView  mProfileImage;
    private Button btnSetting;
    private TextView mNamaUser, mEmailUser, mNoHpUser, mNoKkUser,mNoRumahUser;
    private ArrayList<ModelUser> dataUser;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private Button myButton,btnQrCodeView;
    private ImageView ivQRCode;
    private RelativeLayout rlQRCODE;
    private String setUserId,setUserType,setNoRumah,setNamaUser,setEmail,setPassword,setNoHp,setNoKK,setImage, myUserId;
    private String CIPHERTEXT
            , PLAINTEXT = "RTLIMABELASRWKOSONGENAMKEMBANGANUTARA"
            , KEY = "SYAFWAN";
    private Bitmap btmap;
    private ProgressDialog pd;

    public AkunFragment(String myUserId) {
        this.myUserId = myUserId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akun, container, false );

        pd = new ProgressDialog(this.getContext());
        auth = FirebaseAuth.getInstance();
//        mFirestore = FirebaseFirestore.getInstance();

        mProfileImage = view.findViewById(R.id.iv_img_profile);

        ivQRCode = view.findViewById(R.id.iv_qrcode);
        qrCodeGenerate();
        mNoRumahUser = view.findViewById(R.id.tv_no_rumah_user);
        mNamaUser = view.findViewById(R.id.tv_nama_user);
        mEmailUser = view.findViewById(R.id.tv_gmail_user);
        mNoHpUser = view.findViewById(R.id.tv_no_hp_user);
        mNoKkUser = view.findViewById(R.id.tv_no_kk_user);
        rlQRCODE = view.findViewById(R.id.RL_QRCODE);

        getDataAkunYangLogin();

        btnQrCodeView = view.findViewById(R.id.btn_qrcode_view);
        btnQrCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ImageDialog.class);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();//image will get stream and byte
                btmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();

                intent.putExtra("QRCODE", bytes);
                startActivity(intent);

            }
        });

        myButton = view.findViewById(R.id.btn_signout);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btnSetting = view.findViewById(R.id.btn_settings);
        btnSetting.setBackgroundColor(Color.TRANSPARENT);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), SettingProfileActivity.class);
                intent.putExtra("I_USERTYPE", setUserType);
                intent.putExtra("I_NORUMAH", setNoRumah);
                intent.putExtra("I_NAMA", setNamaUser);
                intent.putExtra("I_EMAIL", setEmail);
                intent.putExtra("I_PASWORD", setPassword);

                intent.putExtra("I_NOHP", setNoHp);
                intent.putExtra("I_NOKK", setNoKK);
                intent.putExtra("I_USERID", setUserId);
                intent.putExtra("I_IMAGE", setImage);



                mProfileImage.buildDrawingCache();
                Bitmap image = mProfileImage.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("IMAGEBITMAP", image);
                intent.putExtras(extras);


                startActivity(intent);
            }
        });



        return view;

    }

    private void qrCodeGenerate() {

        //kunci dan text untuk di enkrip
        final String text = PLAINTEXT;
        final String key = KEY;

        //VIGENERE CIPHER
        encrypt(text, key);

        if (CIPHERTEXT != null && !CIPHERTEXT.isEmpty()) {
            try {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(CIPHERTEXT, BarcodeFormat.QR_CODE, 500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                btmap = barcodeEncoder.createBitmap(bitMatrix);

                ivQRCode.setImageBitmap(btmap);

            } catch (WriterException e) {
                e.printStackTrace();

            }
        }

    }

    private void encrypt(String text, String key) {
        String res = "";
        text = text.toUpperCase();
        key = key.toUpperCase();
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                res = new StringBuilder(String.valueOf(res)).append((char) (((key.charAt(j) + c) % 26) + 65)).toString();
                j = (j + 1) % key.length();
            }
        }
        CIPHERTEXT = res;
    }

    private void getDataAkunYangLogin() {


        final DatabaseReference databaseUsers;
        final String getEmailUser = auth.getCurrentUser().getEmail();
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
                for (int i=0; i<n; i++) {

                    String gmail = dataUser.get(i).getGmail();

                    if (gmail.equals(getEmailUser)){
                        setNoRumah = dataUser.get(i).getNoRumah();
                        setUserType = dataUser.get(i).getUserType();
                        setNamaUser = dataUser.get(i).getNamaKepalaKeluarga();
                        setEmail = dataUser.get(i).getGmail();

                        setPassword = dataUser.get(i).getPassword();
                        setNoHp = dataUser.get(i).getNoHp();
                        setNoKK = dataUser.get(i).getNoKK();
                        setUserId = myUserId;
                        setImage = dataUser.get(i).getImg();

                        if (setImage.equals("noImage")) {
                            //hide imageview
                            RequestOptions placeholderOption = new RequestOptions();
                            placeholderOption.placeholder(R.drawable.iconscamera);
                        }
                        else {
                            try {
                                Picasso.with(getContext())
                                        .load(setImage)
                                        .into(mProfileImage);
                            }
                            catch (Exception e) {

                            }
                        }

                    }
                }
//                mFirestore.collection("Users").document(setUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        String user_image = documentSnapshot.getString("image");
//
//                        RequestOptions placeholderOption = new RequestOptions();
//                        placeholderOption.placeholder(R.drawable.iconscamera);
//
//                        Glide.with(getContext())
//                                .setDefaultRequestOptions(placeholderOption)
//                                .load(user_image)
//                                .into(mProfileImage);
//                    }
//                });

                mNoRumahUser.setText(setNoRumah);
                mNamaUser.setText(setNamaUser);
                mEmailUser.setText(setEmail);
                mNoHpUser.setText(setNoHp);
                mNoKkUser.setText(setNoKK);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signOut() {
        auth.signOut();
        Toast.makeText(getContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginGmailActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onStart() {
        checkUserStatus();
        super.onStart();
    }
    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("Akun");

    }
    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            //mprofiletv.settext(user.getemail();

//            Toast.makeText(getContext(), "uid "+myUserId,Toast.LENGTH_SHORT).show();
        } else {
            //user not signed in, goto main activity
            startActivity(new Intent(getContext(), LoginGmailActivity.class));
            getActivity().finish();
        }
    }
}
