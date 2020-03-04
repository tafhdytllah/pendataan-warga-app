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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.DetailIuranBulananAdapter;
import com.tafh.tugasakhir.adapter.IuranBulananAdapter;
import com.tafh.tugasakhir.model.ModelIuran;
import com.tafh.tugasakhir.model.ModelKeluarga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DetailIuranBulananActivity extends AppCompatActivity{

    private MenuItem item;

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private FirebaseAuth auth;
    private DatabaseReference databaseIuran;

    private ArrayList<ModelIuran> dataIuran;

    private TextView textKosong;
    private ProgressBar progressBar;
    private String mKeluargaId, mNorumah, mNokk, mNamaKK;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_iuran_bulanan);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Iuran Bulanan");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        //show newest post first, for this load from last


        Intent getIntent = getIntent();
        mNamaKK = getIntent.getStringExtra("NAMAKK");
        mKeluargaId = getIntent.getStringExtra("iKeluargaId");
        mNorumah = getIntent.getStringExtra("iNoRumah");
        mNokk = getIntent.getStringExtra("iNoKK");

        //FAB untuk pindah ke tambah data iuran
        FloatingActionButton fab = findViewById(R.id.fab_iuran);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailIuranBulananActivity.this, TambahIuranActivity.class);
                intent.putExtra("NAMAKK", mNamaKK);
                intent.putExtra("KELUARGAID", mKeluargaId);
                intent.putExtra("MNORUMAH", mNorumah);
                intent.putExtra("MNOKK", mNokk);
                startActivity(intent);
            }
        });
        textKosong = findViewById(R.id.text_kosong);
        progressBar = findViewById(R.id.progress_bar_detail_iuran);
        recyclerView = findViewById(R.id.rv_list_detail_iuran_bulanan);

        MyRecyclerView();
        GetData();


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }


    private void GetData() {
        progressBar.setVisibility(View.VISIBLE);


        databaseIuran = FirebaseDatabase.getInstance().getReference("Iuran");

        databaseIuran.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Inisialisasi ArrayList
                dataIuran = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child(mKeluargaId).getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek iuran
                    ModelIuran iuran = snapshot.getValue(ModelIuran.class);

                    if (databaseIuran == null) {
                        textKosong.setVisibility(View.VISIBLE);
                    } else {
                        textKosong.setVisibility(View.INVISIBLE);
                    }

                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    iuran.setIuranId(snapshot.getKey());

                    dataIuran.add(iuran);
                }

                //Inisialisasi Adapter dan data akun dalam bentuk Array
                adapter = new DetailIuranBulananAdapter(DetailIuranBulananActivity.this, dataIuran, mKeluargaId);
                //Memasang Adapter pada RecyclerView
                recyclerView.setAdapter(adapter);

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                        /*
                        Kode ini akan dijalankan ketika ada error dan
                        pengambilan data error tersebut lalu memprint error nya
                        ke LogCat
                        */
                Toast.makeText(DetailIuranBulananActivity.this,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                Log.e("KeluargaAktifFragment", databaseError.getDetails()+" "+databaseError.getMessage());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //searchview
        item = menu.findItem(R.id.action_search);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_sortnama).setTitle("Urutkan sesuai Tanggal");
        menu.findItem(R.id.action_sortnomor).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);



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

            dataIuran.clear();
            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            final String getEmailUser = fUser.getEmail();
            //get path of database named "Users" containing user info
            final DatabaseReference drIuran = FirebaseDatabase.getInstance().getReference("Iuran");
//        get all data from path
            drIuran.child(mKeluargaId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        ModelIuran modelIuran = snapshot.getValue(ModelIuran.class);
                        modelIuran.setKeluargaId(snapshot.getKey());
                        dataIuran.add(modelIuran);

                        Collections.sort(dataIuran, new Comparator<ModelIuran>() {
                            @Override
                            public int compare(ModelIuran model, ModelIuran t1) {
                                return model.getTanggalIuran().compareTo(t1.getTanggalIuran());
                            }
                        });
                        adapter = new DetailIuranBulananAdapter(DetailIuranBulananActivity.this, dataIuran, mKeluargaId);
                        //set adapter to recycler view

                        recyclerView.setAdapter(adapter);


                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetailIuranBulananActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(DetailIuranBulananActivity.this, "Urut sesuai Tanggal", Toast.LENGTH_SHORT).show();
//            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }



    private void MyRecyclerView() {
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext()
                , DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }



}
