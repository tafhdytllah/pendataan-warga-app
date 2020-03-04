package com.tafh.tugasakhir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.keluarga.KeluargaKeluarDetailActivity;
import com.tafh.tugasakhir.model.ModelKeluargaNonAktif;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class KeluargaKeluarAdapter extends RecyclerView.Adapter<KeluargaKeluarAdapter.ViewHolder> {
    //Deklarasi Variable
    private ArrayList<ModelKeluargaNonAktif> dataKeluarga;
    private ArrayList<ModelUser> listAkun;
    private Context context;
    private String myUserId;

    public KeluargaKeluarAdapter(Context context, ArrayList<ModelKeluargaNonAktif> models, String myUserId) {
        this.dataKeluarga = models;
        this.context = context;
        this.myUserId = myUserId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView noRumah, namaKepalaKeluarga, noKK;
        private ImageView imageView;
        private ItemClickListener itemClickListener;
        private LinearLayout listKeluargaKeluar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listKeluargaKeluar = itemView.findViewById(R.id.row_list_keluarga_keluar);

            imageView = itemView.findViewById(R.id.iv_ImageView_keluarga_keluar);
            noRumah = itemView.findViewById(R.id.tv_no_rumah_keluarga_keluar);
            namaKepalaKeluarga = itemView.findViewById(R.id.tv_nama_kk_keluarga_keluar);
            noKK = itemView.findViewById(R.id.tv_no_kk_keluarga_keluar);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClickListener(view, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_keluarga_keluar, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KeluargaKeluarAdapter.ViewHolder holder, int i) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String NoRumah = dataKeluarga.get(i).getNoRumah();
        final String NamaKepalaKeluarga = dataKeluarga.get(i).getNamaKK();
        final String NoKK = dataKeluarga.get(i).getNoKK();
//        final int ImageV = models.get(position).getImg();
//        ModelKeluargaNonAktif m = new ModelKeluargaNonAktif();
//        final int ImageV = m.setImg(R.drawable.ic_person_24dp);
        //Memasukan Nilai/Value kedalam View (TextView)
        holder.noRumah.setText(NoRumah);
        holder.namaKepalaKeluarga.setText(NamaKepalaKeluarga);
        holder.noKK.setText(NoKK);
        holder.imageView.setImageResource(R.drawable.ic_akun_24dp);

//        friends this method is than you can use when you want to use one activity
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int i) {
                String gKeluargaNonAktifId = dataKeluarga.get(i).getKeluargaNonAktifId();
                String gKeluargaId = dataKeluarga.get(i).getKeluargaId();
                String gUserId = dataKeluarga.get(i).getUserId();
                String gImageKK = dataKeluarga.get(i).getImageKK();
                String gNoRumah = dataKeluarga.get(i).getNoRumah();
                String gNoKK = dataKeluarga.get(i).getNoKK();

                String gNamaKK = dataKeluarga.get(i).getNamaKK();
                String gNik1 = dataKeluarga.get(i).getNik1();
                String gJenisKelamin1 = dataKeluarga.get(i).getJenisKelamin1();
                String gTempatLahir1 = dataKeluarga.get(i).getTempatLahir1();
                String gtglLahir1 = dataKeluarga.get(i).getTglLahir1();
                String gAgama1 = dataKeluarga.get(i).getAgama1();

                String gNamaIstri = dataKeluarga.get(i).getNamaIstri();
                String gStatusHubungan2 = dataKeluarga.get(i).getStatusHubungan2();
                String gNik2 = dataKeluarga.get(i).getNik2();
                String gJenisKelamin2 = dataKeluarga.get(i).getJenisKelamin2();
                String gTempatLahir2 = dataKeluarga.get(i).getTempatLahir2();
                String gtglLahir2 = dataKeluarga.get(i).getTglLahir2();
                String gAgama2 = dataKeluarga.get(i).getAgama2();

                String gNama3 = dataKeluarga.get(i).getNama3();
                String gNik3 = dataKeluarga.get(i).getNik3();
                String gStatusHubungan3 = dataKeluarga.get(i).getStatusHubungan3();
                String gJenisKelamin3 = dataKeluarga.get(i).getJenisKelamin3();
                String gTempatLahir3 = dataKeluarga.get(i).getTempatLahir3();
                String gtglLahir3 = dataKeluarga.get(i).getTglLahir3();
                String gAgama3 = dataKeluarga.get(i).getAgama3();

                String gNama4 = dataKeluarga.get(i).getNama4();
                String gNik4 = dataKeluarga.get(i).getNik4();
                String gStatusHubungan4 = dataKeluarga.get(i).getStatusHubungan4();
                String gJenisKelamin4 = dataKeluarga.get(i).getJenisKelamin4();
                String gTempatLahir4 = dataKeluarga.get(i).getTempatLahir4();
                String gtglLahir4 = dataKeluarga.get(i).getTglLahir4();
                String gAgama4 = dataKeluarga.get(i).getAgama4();

                String gNama5 = dataKeluarga.get(i).getNama5();
                String gNik5 = dataKeluarga.get(i).getNik5();
                String gStatusHubungan5 = dataKeluarga.get(i).getStatusHubungan5();
                String gJenisKelamin5 = dataKeluarga.get(i).getJenisKelamin5();
                String gTempatLahir5 = dataKeluarga.get(i).getTempatLahir5();
                String gtglLahir5 = dataKeluarga.get(i).getTglLahir5();
                String gAgama5 = dataKeluarga.get(i).getAgama5();

                String gNama6 = dataKeluarga.get(i).getNama6();
                String gNik6 = dataKeluarga.get(i).getNik6();
                String gStatusHubungan6 = dataKeluarga.get(i).getStatusHubungan6();
                String gJenisKelamin6 = dataKeluarga.get(i).getJenisKelamin6();
                String gTempatLahir6 = dataKeluarga.get(i).getTempatLahir6();
                String gtglLahir6 = dataKeluarga.get(i).getTglLahir6();
                String gAgama6 = dataKeluarga.get(i).getAgama6();

                String gNama7 = dataKeluarga.get(i).getNama7();
                String gNik7 = dataKeluarga.get(i).getNik7();
                String gStatusHubungan7 = dataKeluarga.get(i).getStatusHubungan7();
                String gJenisKelamin7 = dataKeluarga.get(i).getJenisKelamin7();
                String gTempatLahir7 = dataKeluarga.get(i).getTempatLahir7();
                String gtglLahir7 = dataKeluarga.get(i).getTglLahir7();
                String gAgama7 = dataKeluarga.get(i).getAgama7();

                String gNama8 = dataKeluarga.get(i).getNama8();
                String gNik8 = dataKeluarga.get(i).getNik8();
                String gStatusHubungan8 = dataKeluarga.get(i).getStatusHubungan8();
                String gJenisKelamin8 = dataKeluarga.get(i).getJenisKelamin8();
                String gTempatLahir8 = dataKeluarga.get(i).getTempatLahir8();
                String gtglLahir8 = dataKeluarga.get(i).getTglLahir8();
                String gAgama8 = dataKeluarga.get(i).getAgama8();

                String gNama9 = dataKeluarga.get(i).getNama9();
                String gNik9 = dataKeluarga.get(i).getNik9();
                String gStatusHubungan9 = dataKeluarga.get(i).getStatusHubungan9();
                String gJenisKelamin9 = dataKeluarga.get(i).getJenisKelamin9();
                String gTempatLahir9 = dataKeluarga.get(i).getTempatLahir9();
                String gtglLahir9 = dataKeluarga.get(i).getTglLahir9();
                String gAgama9 = dataKeluarga.get(i).getAgama9();

                String gNama10 = dataKeluarga.get(i).getNama10();
                String gNik10 = dataKeluarga.get(i).getNik10();
                String gStatusHubungan10 = dataKeluarga.get(i).getStatusHubungan10();
                String gJenisKelamin10 = dataKeluarga.get(i).getJenisKelamin10();
                String gTempatLahir10 = dataKeluarga.get(i).getTempatLahir10();
                String gtglLahir10 = dataKeluarga.get(i).getTglLahir10();
                String gAgama10 = dataKeluarga.get(i).getAgama10();

                //get our data with intent
                Intent intent = new Intent(context, KeluargaKeluarDetailActivity.class);
//
                intent.putExtra("gKeluargaNonAktifId", gKeluargaNonAktifId);
                intent.putExtra("gKeluargaId", gKeluargaId);
                intent.putExtra("gUserId", gUserId);
                intent.putExtra("gImageKK", gImageKK);
                intent.putExtra("gNoRumah", gNoRumah);
                intent.putExtra("gNoKK", gNoKK);

                intent.putExtra("gNamaKK", gNamaKK);
                intent.putExtra("gNik1", gNik1);
                intent.putExtra("gJenisKelamin1", gJenisKelamin1);
                intent.putExtra("gTempatLahir1", gTempatLahir1);
                intent.putExtra("gtglLahir1", gtglLahir1);
                intent.putExtra("gAgama1", gAgama1);

                intent.putExtra("gNamaIstri", gNamaIstri);
                intent.putExtra("gStatusHubungan2", gStatusHubungan2);
                intent.putExtra("gNik2", gNik2);
                intent.putExtra("gJenisKelamin2", gJenisKelamin2);
                intent.putExtra("gTempatLahir2", gTempatLahir2);
                intent.putExtra("gtglLahir2", gtglLahir2);
                intent.putExtra("gAgama2", gAgama2);


                intent.putExtra("gNama3", gNama3);
                intent.putExtra("gStatusHubungan3", gStatusHubungan3);
                intent.putExtra("gNik3", gNik3);
                intent.putExtra("gJenisKelamin3", gJenisKelamin3);
                intent.putExtra("gTempatLahir3", gTempatLahir3);
                intent.putExtra("gtglLahir3", gtglLahir3);
                intent.putExtra("gAgama3", gAgama3);

                intent.putExtra("gNama4", gNama4);
                intent.putExtra("gStatusHubungan4", gStatusHubungan4);
                intent.putExtra("gNik4", gNik4);
                intent.putExtra("gJenisKelamin4", gJenisKelamin4);
                intent.putExtra("gTempatLahir4", gTempatLahir4);
                intent.putExtra("gtglLahir4", gtglLahir4);
                intent.putExtra("gAgama4", gAgama4);

                intent.putExtra("gNama5", gNama5);
                intent.putExtra("gStatusHubungan5", gStatusHubungan5);
                intent.putExtra("gNik5", gNik5);
                intent.putExtra("gJenisKelamin5", gJenisKelamin5);
                intent.putExtra("gTempatLahir5", gTempatLahir5);
                intent.putExtra("gtglLahir5", gtglLahir5);
                intent.putExtra("gAgama5", gAgama5);

                intent.putExtra("gNama6", gNama6);
                intent.putExtra("gStatusHubungan6", gStatusHubungan6);
                intent.putExtra("gNik6", gNik6);
                intent.putExtra("gJenisKelamin6", gJenisKelamin6);
                intent.putExtra("gTempatLahir6", gTempatLahir6);
                intent.putExtra("gtglLahir6", gtglLahir6);
                intent.putExtra("gAgama6", gAgama6);

                intent.putExtra("gNama7", gNama7);
                intent.putExtra("gStatusHubungan7", gStatusHubungan7);
                intent.putExtra("gNik7", gNik7);
                intent.putExtra("gJenisKelamin7", gJenisKelamin7);
                intent.putExtra("gTempatLahir7", gTempatLahir7);
                intent.putExtra("gtglLahir7", gtglLahir7);
                intent.putExtra("gAgama7", gAgama7);

                intent.putExtra("gNama8", gNama8);
                intent.putExtra("gStatusHubungan8", gStatusHubungan8);
                intent.putExtra("gNik8", gNik8);
                intent.putExtra("gJenisKelamin8", gJenisKelamin8);
                intent.putExtra("gTempatLahir8", gTempatLahir8);
                intent.putExtra("gtglLahir8", gtglLahir8);
                intent.putExtra("gAgama8", gAgama8);

                intent.putExtra("gNama9", gNama9);
                intent.putExtra("gStatusHubungan9", gStatusHubungan9);
                intent.putExtra("gNik9", gNik9);
                intent.putExtra("gJenisKelamin9", gJenisKelamin9);
                intent.putExtra("gTempatLahir9", gTempatLahir9);
                intent.putExtra("gtglLahir9", gtglLahir9);
                intent.putExtra("gAgama9", gAgama9);


                intent.putExtra("gNama10", gNama10);
                intent.putExtra("gStatusHubungan10", gStatusHubungan10);
                intent.putExtra("gNik10", gNik10);
                intent.putExtra("gJenisKelamin10", gJenisKelamin10);
                intent.putExtra("gTempatLahir10", gTempatLahir10);
                intent.putExtra("gtglLahir10", gtglLahir10);
                intent.putExtra("gAgama10", gAgama10);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataKeluarga.size();
    }
}

