package com.tafh.tugasakhir.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.MainWargaActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.profile.SettingProfileActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AkunWargaFragment extends Fragment {
    private FirebaseFirestore mFirestore;
    private CircleImageView mProfileImage;
    private Button btnSetting;
    private TextView mNamaUser, mEmailUser, mNoHpUser, mNoKkUser,mNoRumahUser;
    private ArrayList<ModelUser> dataUser;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private Button myButton;
    private String myUserId;

    private String setUserId,setUserType,setNoRumah,setNamaUser,setEmail,setPassword,setNoHp,setNoKK,setImage;
    private ProgressDialog pd;

    public AkunWargaFragment(String myUserId) {
        // Required empty public constructor
        this.myUserId = myUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_akun_warga, container, false);
        pd = new ProgressDialog(this.getContext());

        auth = FirebaseAuth.getInstance();

        mProfileImage = view.findViewById(R.id.iv_img_profile);

        mNoRumahUser = view.findViewById(R.id.tv_no_rumah_user);
        mNamaUser = view.findViewById(R.id.tv_nama_user);
        mEmailUser = view.findViewById(R.id.tv_gmail_user);
        mNoHpUser = view.findViewById(R.id.tv_no_hp_user);
        mNoKkUser = view.findViewById(R.id.tv_no_kk_user);

        myButton = view.findViewById(R.id.btn_signout);
        btnSetting = view.findViewById(R.id.btn_settings);

        getDataAkunYangLogin();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

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
                                Picasso.with(getContext()).load(setImage).into(mProfileImage);
                            }
                            catch (Exception e) {

                            }
                        }

                    }
                }

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
        ((MainWargaActivity) getActivity())
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
