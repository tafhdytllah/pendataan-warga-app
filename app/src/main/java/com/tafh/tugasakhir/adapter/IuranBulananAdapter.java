package com.tafh.tugasakhir.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.iuran.DetailIuranBulananActivity;
import com.tafh.tugasakhir.model.ModelKeluarga;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class IuranBulananAdapter extends RecyclerView.Adapter<IuranBulananAdapter.ViewHolder> {
    //Deklarasi Variable
    private ArrayList<ModelKeluarga> models;
    private Context context;

    public IuranBulananAdapter(Context context, ArrayList<ModelKeluarga> models) {
        this.models = models;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView noRumah, namaKepalaKeluarga, noKK;
        private ImageView imageView;
        private ItemClickListener itemClickListener;
        private LinearLayout listIuranBulanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listIuranBulanan = itemView.findViewById(R.id.row_list_iuran_bulanan);

            imageView = itemView.findViewById(R.id.iv_ImageView_iuran_bulanan);
            noRumah = itemView.findViewById(R.id.tv_no_rumah_iuran);
            namaKepalaKeluarga = itemView.findViewById(R.id.tv_nama_kk_iuran);
            noKK = itemView.findViewById(R.id.tv_no_kk_iuran);

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
                .inflate(R.layout.row_list_iuran_bulanan, parent, false);

        return new IuranBulananAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IuranBulananAdapter.ViewHolder holder, int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String NoRumah = models.get(position).getNoRumah();
        final String NamaKepalaKeluarga = models.get(position).getNamaKK();
        final String NoKK = models.get(position).getNoKK();
//        final int ImageV = models.get(position).getImg();
        ModelKeluarga m = new ModelKeluarga();
//        final int ImageV = m.setImg(R.drawable.list_iuran);
        //Memasukan Nilai/Value kedalam View (TextView)
        holder.noRumah.setText(NoRumah);
        holder.namaKepalaKeluarga.setText(NamaKepalaKeluarga);
        holder.noKK.setText(NoKK);
//        holder.imageView.setImageResource(ImageV);

//        friends this method is than you can use when you want to use one activity
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String gKeluargaId = models.get(position).getKeluargaId();

                String gNoRumah = models.get(position).getNoRumah();
                String gNoKK = models.get(position).getNoKK();
                String gNamaKK = models.get(position).getNamaKK();
//                String gNamaKK = models.get(position).getNamaKepalaKeluarga();
//                String gNikNamaKK = models.get(position).getNikNamaKK();
//
//                String gNamaIstri = models.get(position).getNamaIstri();
//                String gNikIstri = models.get(position).getNikIstri();
//                String gNamaAnak = models.get(position).getNamaAnak();
//                String gNikAnak = models.get(position).getNikAnak();
//
//
//
//                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.imageView.getDrawable();//get image from drawabele
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();//image will get stream and byte
//
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] bytes = stream.toByteArray();



                //get our data with intent
                Intent intent = new Intent(context, DetailIuranBulananActivity.class);

                intent.putExtra("NAMAKK", gNamaKK);
                intent.putExtra("iKeluargaId", gKeluargaId);

                intent.putExtra("iNoRumah", gNoRumah);
                intent.putExtra("iNoKK", gNoKK);
//                intent.putExtra("iNamaKK", gNamaKK);
//                intent.putExtra("iNikNamaKK", gNikNamaKK);
//
//                intent.putExtra("iNamaIstri", gNamaIstri);
//                intent.putExtra("iNikIstri", gNikIstri);
//                intent.putExtra("iNamaAnak", gNamaAnak);
//                intent.putExtra("iNikAnak", gNikAnak);
//
//                intent.putExtra("iImage", bytes);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
