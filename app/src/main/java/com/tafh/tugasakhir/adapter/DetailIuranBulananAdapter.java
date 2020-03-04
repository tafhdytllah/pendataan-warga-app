package com.tafh.tugasakhir.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.tafh.tugasakhir.iuran.DetailIuranBulananActivity;
import com.tafh.tugasakhir.iuran.ListIuranBulananActivity;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaWargaActivity;
import com.tafh.tugasakhir.model.ModelItem;
import com.tafh.tugasakhir.model.ModelIuran;
import com.tafh.tugasakhir.model.ModelKeluarga;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DetailIuranBulananAdapter extends RecyclerView.Adapter<DetailIuranBulananAdapter.ViewHolder> {
    //Deklarasi Variable

    private DatabaseReference databaseIuran;
    private ArrayList<ModelIuran> models;
    private Context context;
    private int nilai;
    private String keluargaId;

    public DetailIuranBulananAdapter(Context context, ArrayList<ModelIuran> models, String mKeluargaId) {
        this.models = models;
        this.context = context;
        this.keluargaId = mKeluargaId;
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

        return new DetailIuranBulananAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailIuranBulananAdapter.ViewHolder holder, final int position) {

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

        holder.listIuranBulanan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Hapus"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
//                                Toast.makeText(context, "Data : "+ models.get(position), Toast.LENGTH_SHORT).show();
                                onDeleteData(models.get(position).getIuranId(), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });


    }

    private void onDeleteData(String data, int position) {
//
//            Toast.makeText(context, "Data : "+ data, Toast.LENGTH_SHORT).show();

            databaseIuran = FirebaseDatabase.getInstance().getReference("Iuran");
            databaseIuran.child(keluargaId)
                    .child(data)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent intent = new Intent(context, ListIuranBulananActivity.class);
                            context.startActivity(intent);

                            Toast.makeText(context, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show();

                            ((Activity)context).finish();

                        }
                    });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
