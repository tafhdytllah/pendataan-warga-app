package com.tafh.tugasakhir.iuran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.AkunAdapter;
import com.tafh.tugasakhir.adapter.IuranBulananAdapter;
import com.tafh.tugasakhir.adapter.KeluargaAdapter;
import com.tafh.tugasakhir.akun.ListDataAkunActivity;
import com.tafh.tugasakhir.keluarga.TambahDataKeluargaActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListIuranBulananActivity extends AppCompatActivity {
    private MenuItem item;

    //Deklarasi Variable untuk RecyclerView
    private IuranBulananAdapter iuranBulananAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private FirebaseAuth auth;
    private DatabaseReference databaseKeluarga;

    private ArrayList<ModelKeluarga> dataKeluarga;

    private ProgressBar progressBar;
    private TextView txtDataKosong;

    private String myUserId;

    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_iuran_bulanan);
        getDataIntent();
        Log.w("myuserid", myUserId);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Iuran Bulanan");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        dataKeluarga = new ArrayList<>();
        txtDataKosong = findViewById(R.id.txt_data_kosong_iuran);
        progressBar = findViewById(R.id.progress_bar_list_iuran);
        recyclerView = findViewById(R.id.rv_list_iuran_bulanan);

        //recycler view and its properties
        layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from last
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

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
        dataKeluarga.clear();
        progressBar.setVisibility(View.VISIBLE);
        databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");

        databaseKeluarga.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Inisialisasi ArrayList

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelKeluarga keluarga = snapshot.getValue(ModelKeluarga.class);

                    if (databaseKeluarga == null) {
                        txtDataKosong.setVisibility(View.VISIBLE);
                    } else {
                        txtDataKosong.setVisibility(View.INVISIBLE);
                    }
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    keluarga.setKeluargaId(snapshot.getKey());
                    dataKeluarga.add(keluarga);
                }

                //Inisialisasi Adapter dan data akun dalam bentuk Array
                iuranBulananAdapter = new IuranBulananAdapter(ListIuranBulananActivity.this, dataKeluarga);
                //Memasang Adapter pada RecyclerView
                recyclerView.setAdapter(iuranBulananAdapter);

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                        /*
                        Kode ini akan dijalankan ketika ada error dan
                        pengambilan data error tersebut lalu memprint error nya
                        ke LogCat
                        */
                Toast.makeText(ListIuranBulananActivity.this,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                Log.e("KeluargaAktifFragment", databaseError.getDetails()+" "+databaseError.getMessage());

            }
        });
    }

//    private void MyRecyclerView() {
//
//        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//
//        //Membuat Underline pada Setiap Item Didalam List
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext()
//                , DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat
//                .getDrawable(getApplicationContext(), R.drawable.line));
//        recyclerView.addItemDecoration(itemDecoration);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //searchview
        item = menu.findItem(R.id.action_search);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_sortnama);
        menu.findItem(R.id.action_sortnomor);
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
        auth = FirebaseAuth.getInstance();
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_sortnama) {
//            layoutManager.setStackFromEnd(false);
//            layoutManager.setReverseLayout(false);

            dataKeluarga.clear();
            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            final String getEmailUser = fUser.getEmail();
            //get path of database named "Users" containing user info
            final DatabaseReference drKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");
//        get all data from path
            drKeluarga.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        ModelKeluarga modelKeluarga = snapshot.getValue(ModelKeluarga.class);
                        modelKeluarga.setKeluargaId(snapshot.getKey());
                        dataKeluarga.add(modelKeluarga);

                        Collections.sort(dataKeluarga, new Comparator<ModelKeluarga>() {
                            @Override
                            public int compare(ModelKeluarga modelKeluarga, ModelKeluarga t1) {
                                return modelKeluarga.getNamaKK().compareTo(t1.getNamaKK());
                            }
                        });
                        iuranBulananAdapter = new IuranBulananAdapter(ListIuranBulananActivity.this, dataKeluarga);
                        //set adapter to recycler view

                        recyclerView.setAdapter(iuranBulananAdapter);


                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListIuranBulananActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(ListIuranBulananActivity.this, "Urut sesuai Nama", Toast.LENGTH_SHORT).show();
//            checkUserStatus();
        }
        if (id == R.id.action_sortnomor) {
//            layoutManager.setStackFromEnd(false);
//            layoutManager.setReverseLayout(false);

            dataKeluarga.clear();
            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            final String getEmailUser = fUser.getEmail();
            //get path of database named "Users" containing user info
            final DatabaseReference drKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");
//        get all data from path
            drKeluarga.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        ModelKeluarga modelKeluarga = snapshot.getValue(ModelKeluarga.class);
                        modelKeluarga.setKeluargaId(snapshot.getKey());
                        dataKeluarga.add(modelKeluarga);

                        Collections.sort(dataKeluarga, new Comparator<ModelKeluarga>() {
                            @Override
                            public int compare(ModelKeluarga modelKeluarga, ModelKeluarga t1) {
                                return new BigDecimal(modelKeluarga.getNoRumah()).compareTo(new BigDecimal(t1.getNoRumah()));

                            }
                        });
                        iuranBulananAdapter = new IuranBulananAdapter(ListIuranBulananActivity.this, dataKeluarga);
                        //set adapter to recycler view

                        recyclerView.setAdapter(iuranBulananAdapter);


                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListIuranBulananActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(ListIuranBulananActivity.this, "Urut sesuai No Rumah", Toast.LENGTH_SHORT).show();
//            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchUsers(final String query) {

        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        final String getEmailUser = fUser.getEmail();
        //get path of database named "Users" containing user info
        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Keluarga");
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
                dataKeluarga.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ModelKeluarga modelKeluarga = snapshot.getValue(ModelKeluarga.class);
                    modelKeluarga.setKeluargaId(snapshot.getKey());

                    if (modelKeluarga.getNamaKK().toLowerCase().contains(query.toLowerCase()) ||
                            modelKeluarga.getNoRumah().toLowerCase().contains(query.toLowerCase()) ||
                            modelKeluarga.getNoKK().toLowerCase().contains(query.toLowerCase())) {

                        dataKeluarga.add(modelKeluarga);

                        iuranBulananAdapter = new IuranBulananAdapter(ListIuranBulananActivity.this, dataKeluarga);
                        //set adapter to recycler view
                        recyclerView.setAdapter(iuranBulananAdapter);

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
                Toast.makeText(ListIuranBulananActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
