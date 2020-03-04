package com.tafh.tugasakhir.akun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.AkunAdapter;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListDataAkunActivity extends AppCompatActivity{
    private MenuItem item;
    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
//    private RecyclerView.Adapter adapter;
    private AkunAdapter akunAdapter;
//    private RecyclerView.LayoutManager layoutManager;
    private Button btnTambah;
    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private FirebaseAuth auth;
    private DatabaseReference databaseUsers;

    private ArrayList<ModelUser> dataAkun;

    private ProgressBar progressBar;
    private TextView txtDataKosong;

    private ActionBar actionBar;

    private String myUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_akun);
        getDataIntent();
//        Log.w("myuserid", myUserId);
        auth = FirebaseAuth.getInstance();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Data User");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDataAkunActivity.this, TambahDataAkunActivity.class);
                startActivity(intent);
            }
        });

        txtDataKosong = findViewById(R.id.txt_data_kosong_akun);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.rv_list_data_akun);
        //recycler view and its properties
        layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

//        btnTambah = findViewById(R.id.btn_tambah);
//        btnTambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ListDataAkunActivity.this, TambahDataAkunActivity.class);
//                startActivity(intent);
//            }
//        });
        //Inisialisasi ArrayList

        dataAkun = new ArrayList<>();


//
//        ModelUser m = new ModelUser();
//        m.setImg(R.drawable.ic_akun_black_24dp);

        getAllUsers();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null ){

        }
        else {
            startActivity(new Intent(this, LoginGmailActivity.class));
            finish();
        }
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        myUserId = intent.getStringExtra("MY_USERID");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }

    private void getAllUsers() {

        //show newest post first, for this load from last
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
        //set layout to recyclerview

        txtDataKosong.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
//        Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        databaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataAkun.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek akun
                            ModelUser akun = snapshot.getValue(ModelUser.class);

                            if (databaseUsers == null) {
                                txtDataKosong.setVisibility(View.VISIBLE);
                            } else {
                                txtDataKosong.setVisibility(View.INVISIBLE);
                            }

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            akun.setUserid(snapshot.getKey());
                            dataAkun.add(akun);
                        }

                        //Inisialisasi Adapter dan data akun dalam bentuk Array
                        akunAdapter = new AkunAdapter(ListDataAkunActivity.this, dataAkun, myUserId);
                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(akunAdapter);

                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        /*
                        Kode ini akan dijalankan ketika ada error dan
                        pengambilan data error tersebut lalu memprint error nya
                        ke LogCat
                        */
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("ListDataAkunActivity", databaseError.getDetails()+" "+databaseError.getMessage());

                    }
                });
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
//    private void MyRecyclerView() {
//        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
//        layoutManager = new LinearLayoutManager(this);
//
//        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//
//        //Membuat Underline pada Setiap Item Didalam List
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext()
//            , DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat
//            .getDrawable(getApplicationContext(), R.drawable.line));
//        recyclerView.addItemDecoration(itemDecoration);
//    }
//
//    private void searchUsers(final String query) {
//        //get current user
//        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//        final String getEmailUser = fUser.getEmail();
//        //get path of database named "Users" containing user info
//        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
//        //get all data from path
//        drUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                dataUsers2.clear();
////                dataUsers1.clear();
//
//
////                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
////                    ModelUser user = snapshot.getValue(ModelUser.class);
//////                    user.setUserid(snapshot.getKey());
////                    dataUsers1.add(user);
////                }
////                int n = dataUsers1.size();
////                for (int i=0; i<n; i++) {
////                    String gmail = dataUsers1.get(i).getGmail();
////                    if (gmail.equals(getEmailUser)) {
////                        getUserChatId = dataUsers1.get(i).getUserid();
//////                        coba.setText(dataUsers.get(i).getUserid());
////                    }
////                }
//                dataAkun.clear();
//
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    ModelUser chats = snapshot.getValue(ModelUser.class);
//                    chats.setUserid(snapshot.getKey());
//
//
//                    if (!chats.getUserid().equals(myUserId)) {
//                        if (chats.getNamaKepalaKeluarga().toLowerCase().contains(query.toLowerCase()) ||
//                                chats.getGmail().toLowerCase().contains(query.toLowerCase())) {
//
//                            dataAkun.add(chats);
//
//                            akunAdapter = new AkunAdapter(ListDataAkunActivity.this, dataAkun, myUserId);
//                            //set adapter to recycler view
//                            recyclerView.setAdapter(akunAdapter);
//
//                        }
//
//                    }
//
//                }
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ListDataAkunActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//        //inflating menu
//        menu.clear();
//        inflater.inflate(R.menu.menu_main, menu);
//        //searchView
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = new SearchView(this.getSupportActionBar().getThemedContext());
//        MenuItemCompat.setActionView(item, searchView);
//        //search listener
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                //called when user press search button from keyboard
//                //if search query is not empty then searc
//                if (!TextUtils.isEmpty(s.trim())) {
//                    //search text contains text, search it
////                    Toast.makeText(getContext(), "Joss"+s,Toast.LENGTH_SHORT).show();
//                    searchUsers(s);
//                }
//                else {
//                    GetData();
//                }
//
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                //called wheneever user press any single letter
//                //if search query is not empty then searc
//                if (!TextUtils.isEmpty(s.trim())) {
//                    //search text contains text, search it
//                    searchUsers(s);
//                }
//                else {
//                    GetData();
//                }
//                return false;
//            }
//        });
//
//
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        //inflating menu
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_sortnama);
        menu.findItem(R.id.action_sortnomor);
        //searchview
        item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((this)).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, searchView);
        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button from keyboard
                //if search query is not empty then searc
                if (!TextUtils.isEmpty(s.trim())) {
                    //search text contains text, search it
//                    Toast.makeText(getContext(), "Joss"+s,Toast.LENGTH_SHORT).show();
                    searchUsers(s);
                }
                else {
                    getAllUsers();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called wheneever user press any single letter
                //if search query is not empty then searc
                if (!TextUtils.isEmpty(s.trim())) {
                    //search text contains text, search it
                    searchUsers(s);
                }
                else {
                    getAllUsers();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth auth;
//        auth = FirebaseAuth.getInstance();
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_sortnama) {
//            layoutManager.setStackFromEnd(false);
//            layoutManager.setReverseLayout(false);

            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            final String getEmailUser = fUser.getEmail();
            //get path of database named "Users" containing user info
            final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
//        get all data from path
            drUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    dataAkun.clear();

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        ModelUser modelUser = snapshot.getValue(ModelUser.class);
                        modelUser.setUserid(snapshot.getKey());
                        dataAkun.add(modelUser);
                        Collections.sort(dataAkun, new Comparator<ModelUser>() {
                            @Override
                            public int compare(ModelUser modelUser, ModelUser t1) {
                                return modelUser.getNamaKepalaKeluarga().compareTo(t1.getNamaKepalaKeluarga());


                            }

                        });
                        akunAdapter = new AkunAdapter(ListDataAkunActivity.this, dataAkun, myUserId);
                        //set adapter to recycler view

                        recyclerView.setAdapter(akunAdapter);


                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListDataAkunActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(ListDataAkunActivity.this, "Urut sesuai Nama", Toast.LENGTH_SHORT).show();
//            checkUserStatus();
        }
        if (id == R.id.action_sortnomor) {
//            layoutManager.setStackFromEnd(false);
//            layoutManager.setReverseLayout(false);

            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            final String getEmailUser = fUser.getEmail();
            //get path of database named "Users" containing user info
            final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
//        get all data from path
            drUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    dataAkun.clear();

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        ModelUser modelUser = snapshot.getValue(ModelUser.class);
                        modelUser.setUserid(snapshot.getKey());
                        dataAkun.add(modelUser);
                        Collections.sort(dataAkun, new Comparator<ModelUser>() {
                            @Override
                            public int compare(ModelUser modelUser, ModelUser t1) {
                                return new BigDecimal(modelUser.getNoRumah()).compareTo(new BigDecimal(t1.getNoRumah()));
                            }

                        });
                        akunAdapter = new AkunAdapter(ListDataAkunActivity.this, dataAkun, myUserId);
                        //set adapter to recycler view

                        recyclerView.setAdapter(akunAdapter);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListDataAkunActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(ListDataAkunActivity.this, "Urut sesuai No Rumah", Toast.LENGTH_SHORT).show();
//            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchUsers(final String query) {
        Log.w("Nilai", ""+query);
        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        final String getEmailUser = fUser.getEmail();
        //get path of database named "Users" containing user info
        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
//        get all data from path
        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataUsers2.clear();
//                dataUsers1.clear();


//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    ModelUser user = snapshot.getValue(ModelUser.class);
////                    user.setUserid(snapshot.getKey());
//                    dataUsers1.add(user);
//                }
//                int n = dataUsers1.size();
//                for (int i=0; i<n; i++) {
//                    String gmail = dataUsers1.get(i).getGmail();
//                    if (gmail.equals(getEmailUser)) {
//                        getUserChatId = dataUsers1.get(i).getUserid();
////                        coba.setText(dataUsers.get(i).getUserid());
//                    }
//                }
                dataAkun.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ModelUser modelUser = snapshot.getValue(ModelUser.class);
                    modelUser.setUserid(snapshot.getKey());

                    if (modelUser.getNamaKepalaKeluarga().toLowerCase().contains(query.toLowerCase()) ||
                            modelUser.getNoRumah().toLowerCase().contains(query.toLowerCase()) ||
                            modelUser.getNoKK().toLowerCase().contains(query.toLowerCase())) {

                        dataAkun.add(modelUser);

                        akunAdapter = new AkunAdapter(ListDataAkunActivity.this, dataAkun, myUserId);
                        //set adapter to recycler view
                        recyclerView.setAdapter(akunAdapter);

                    }
//
//                    if (!modelUser.getUserid().equals(myUserId)) {
//
//
//                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListDataAkunActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }



}
