package com.tafh.tugasakhir.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.iuran.ListIuranBulananActivity;
import com.tafh.tugasakhir.model.ModelIuran;

import java.util.ArrayList;

public class DetailIuranBulananWargaAdapter extends RecyclerView.Adapter<DetailIuranBulananWargaAdapter.ViewHolder> {
    //Deklarasi Variable

    private DatabaseReference databaseIuran;
    private ArrayList<ModelIuran> models;
    private Context context;
    private int nilai;

    public DetailIuranBulananWargaAdapter(Context context, ArrayList<ModelIuran> models) {
        this.models = models;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tglBayarIuran, judulIuran, jumlahIuran, pembayaranIuran;
        private ImageView imageView;
        private LinearLayout listIuranBulanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            listIuranBulanan = itemView.findViewById(R.id.row_detail_iuran_bulanan);

            imageView = itemView.findViewById(R.id.iv_ImageView_detail_iuran_bulanan);
            tglBayarIuran = itemView.findViewById(R.id.tv_tgl_bayar_iuran);
            judulIuran = itemView.findViewById(R.id.tv_judul_iuran);
            jumlahIuran = itemView.findViewById(R.id.tv_jumlah_bayar_iuran);
            pembayaranIuran = itemView.findViewById(R.id.tv_pembayaran_iuran);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_detail_iuran_bulanan, parent, false);

        return new DetailIuranBulananWargaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailIuranBulananWargaAdapter.ViewHolder holder, final int position) {

        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String setTglBayarIuran = models.get(position).getTanggalIuran();
        final String setJudulIuran = models.get(position).getJudulIuran();
        final String setJumlahIuran = models.get(position).getJumlahIuran();
        final String setPembayaranIuran = models.get(position).getPembayaranIuran();
        //Memasukan Nilai/Value kedalam View (TextView)
        holder.tglBayarIuran.setText(setTglBayarIuran);
        holder.judulIuran.setText(setJudulIuran);
        holder.jumlahIuran.setText(setJumlahIuran);
        holder.pembayaranIuran.setText(setPembayaranIuran);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
