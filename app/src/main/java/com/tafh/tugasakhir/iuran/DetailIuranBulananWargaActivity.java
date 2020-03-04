package com.tafh.tugasakhir.iuran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.DetailIuranBulananAdapter;
import com.tafh.tugasakhir.adapter.DetailIuranBulananWargaAdapter;
import com.tafh.tugasakhir.model.ModelIuran;
import com.tafh.tugasakhir.model.ModelKeluarga;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DetailIuranBulananWargaActivity extends AppCompatActivity {

    private MenuItem item;
    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private FirebaseAuth auth;
    private DatabaseReference databaseIuran, databaseUsers, databaseKeluarga;

    private ArrayList<ModelIuran> dataIuran;
    private ArrayList<ModelUser> dataUsers;
    private ArrayList<ModelKeluarga> dataKeluarga;

    private TextView textKosong;
    private String  getNomorKKuser, noKK, setKeluargaId;

    ActionBar actionBar;
//    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_iuran_bulanan_warga);

//        pd = new ProgressDialog(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Pembayaran Iuran");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        textKosong = findViewById(R.id.text_kosong_iuran_warga);
        recyclerView = findViewById(R.id.rv_list_detail_iuran_bulanan_warga);

        getAllUser();


        MyRecyclerView();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }

    private void GetData(final String keluargaId) {
//        pd.setMessage("Memuat data...");
//        pd.show();
        //ambil email user yg login.

        if (keluargaId != null) {

            databaseIuran = FirebaseDatabase.getInstance().getReference("Iuran");
            databaseIuran.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    dataIuran = new ArrayList<>();
                    dataIuran.clear();
                    for (DataSnapshot snapshot : dataSnapshot.child(keluargaId).getChildren()) {
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
                    adapter = new DetailIuranBulananWargaAdapter(DetailIuranBulananWargaActivity.this, dataIuran);
                    //Memasang Adapter pada RecyclerView
//                    pd.dismiss();
                    recyclerView.setAdapter(adapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    pd.dismiss();
                        /*
                        Kode ini akan dijalankan ketika ada error dan
                        pengambilan data error tersebut lalu memprint error nya
                        ke LogCat
                        */
                    Toast.makeText(DetailIuranBulananWargaActivity.this,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                    Log.e("KeluargaAktifFragment", databaseError.getDetails()+" "+databaseError.getMessage());

                }
            });

        }
        else {
//            pd.dismiss();
            textKosong.setVisibility(View.VISIBLE);
        }
    }


    private void getDataKeluarga(final String nokk) {

        databaseKeluarga = FirebaseDatabase.getInstance().getReference("Keluarga");
        databaseKeluarga.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataKeluarga = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelKeluarga keluarga = snapshot.getValue(ModelKeluarga.class);
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    keluarga.setKeluargaId(snapshot.getKey());
                    dataKeluarga.add(keluarga);
                }

                int n = dataKeluarga.size();
                for (int i = 0; i < n; i++) {

                    noKK = dataKeluarga.get(i).getNoKK();
                    //ambil keluarga ID
                    if (noKK.equals(nokk)) {
                        setKeluargaId = dataKeluarga.get(i).getKeluargaId();
                        i=n;
                    }
                }
                GetData(setKeluargaId);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                pd.dismiss();
                Toast.makeText(DetailIuranBulananWargaActivity.this, ""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getAllUser() {

        final String getEmailUser = auth.getCurrentUser().getEmail();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek akun
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    user.setUserid(snapshot.getKey());
                    dataUsers.add(user);
                }
                int n = dataUsers.size();
                for (int i = 0; i < n; i++) {

                    String gmail = dataUsers.get(i).getGmail();

                    if (gmail.equals(getEmailUser)) {
                        //ambil nomor kk user
                        getNomorKKuser = dataUsers.get(i).getNoKK();
                        i=n;
                    }

                }
                getDataKeluarga(getNomorKKuser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                pd.dismiss();
                Toast.makeText(DetailIuranBulananWargaActivity.this, ""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
            drIuran.child(setKeluargaId).addValueEventListener(new ValueEventListener() {
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
                        adapter = new DetailIuranBulananWargaAdapter(DetailIuranBulananWargaActivity.this, dataIuran);
                        //set adapter to recycler view

                        recyclerView.setAdapter(adapter);


                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetailIuranBulananWargaActivity.this, "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(DetailIuranBulananWargaActivity.this, "Urut sesuai Tanggal", Toast.LENGTH_SHORT).show();
//            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    private void MyRecyclerView() {
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext()
                , DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

}
