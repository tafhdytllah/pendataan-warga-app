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

import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.akun.AkunDetailActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.model.ModelUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AkunAdapter extends RecyclerView.Adapter<AkunAdapter.ViewHolder> {
    //Deklarasi Variable
    private ArrayList<ModelUser> models;
    private Context context;
    private String myUserId;

    //Membuat konstruktor, untuk menerima input dari database
    public AkunAdapter(Context context, ArrayList<ModelUser> listAkun, String myUserId) {
        this.context = context;
        this.models = listAkun;
        this.myUserId = myUserId;

    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView noRumah, namaKepalaKeluarga, noKK;
        private ImageView imageView;
        private ItemClickListener itemClickListener;
        private LinearLayout listAkun;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita

            imageView = itemView.findViewById(R.id.iv_imageview);

            noRumah = itemView.findViewById(R.id.tv_no_rumah);
            namaKepalaKeluarga = itemView.findViewById(R.id.tv_nama_kk);
            noKK = itemView.findViewById(R.id.tv_no_kk);

            listAkun = itemView.findViewById(R.id.row_list_akun);

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
                .inflate(R.layout.row_list_akun, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String NoRumah = models.get(position).getNoRumah();
        final String NamaKepalaKeluarga = models.get(position).getNamaKepalaKeluarga();
        final String NoKK = models.get(position).getNoKK();
        final String image = models.get(position).getImg();
//        final int ImageV = models.get(position).getImg();
//        ModelUser m = new ModelUser();
        //Memasukan Nilai/Value kedalam View (TextView)
        holder.noRumah.setText(NoRumah);
        holder.namaKepalaKeluarga.setText(NamaKepalaKeluarga);
        holder.noKK.setText(NoKK);
        try {
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.drawable.ic_akun_black_24dp)
                    .into(holder.imageView);

        } catch (Exception e) {

        }



//        friends this method is than you can use when you want to use one activity
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String gUserId = models.get(position).getUserid();
                String gNama = models.get(position).getNamaKepalaKeluarga();
                String gNoRumah = models.get(position).getNoRumah();
                String gNoKK = models.get(position).getNoKK();

                String gNoHP = models.get(position).getNoHp();
                String gGmail = models.get(position).getGmail();
                String gPassword = models.get(position).getPassword();
//                String gImage = models.get(position).getImg();
                String gUserType = models.get(position).getUserType();


//                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.imageView.getDrawable();//get image from drawabele
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();//image will get stream and byte
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] bytes = stream.toByteArray();

                //get our data with intent
                Intent intent = new Intent(context, AkunDetailActivity.class);

                intent.putExtra("iUserId", gUserId);
                intent.putExtra("iNama", gNama);
                intent.putExtra("iNoRumah", gNoRumah);
                intent.putExtra("iNoKK", gNoKK);
                intent.putExtra("iNoHP", gNoHP);

                intent.putExtra("iGmail", gGmail);
                intent.putExtra("iPassword", gPassword);
//                intent.putExtra("iImage", bytes);
                intent.putExtra("image", image);
                intent.putExtra("iUserType", gUserType);

                context.startActivity(intent);


            }
        });
//        Menampilkan Menu Update dan Delete saat user melakukan long klik pada salah satu item

    }

    @Override
    public int getItemCount() {
        return models.size();
    }


}
