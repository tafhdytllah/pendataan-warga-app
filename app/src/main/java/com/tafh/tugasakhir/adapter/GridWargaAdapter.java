package com.tafh.tugasakhir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.iuran.DetailIuranBulananWargaActivity;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaWargaActivity;
import com.tafh.tugasakhir.model.ModelItem;

import java.util.ArrayList;
import java.util.List;

public class GridWargaAdapter extends RecyclerView.Adapter<GridWargaAdapter.MyHolderWarga> {


    public static final String USER_ID = "userid";
    public static final String WARGA_ID = "wargaid";

    private List<ModelItem> models;
    private String myUserId;
    private Context context;

//    public GridAdapter(HomeFragment c, ArrayList<ModelItem> models) {
//        this.c = c;
//        this.models = models;
//    }
    public GridWargaAdapter(Context context, String myUserId) {
        super();
        this.context = context;
        this.myUserId = myUserId;

        models = new ArrayList<>();

        ModelItem m = new ModelItem();
        m.setTitle("Keluarga Saya");
        m.setImage(R.drawable.keluarga);
        models.add(m);

        m = new ModelItem();
        m.setTitle("Iuran Bulanan");
        m.setImage(R.drawable.list_iuran);
        models.add(m);

    }

    //view holder class

    public class MyHolderWarga extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTitle;
        private ItemClickListener itemClickListener;

        public MyHolderWarga(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_image);

            mTitle = itemView.findViewById(R.id.tv_title_home);

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
    public MyHolderWarga onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home, parent, false);


        return new MyHolderWarga(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolderWarga holder, final int position) {

        final ModelItem modelposition = models.get(position);
        final String userId = myUserId;
        holder.mTitle.setText(modelposition.getTitle());
        holder.mImageView.setImageResource(modelposition.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (models.get(position).getTitle().equals("Keluarga Saya")) {
                    Intent intent = new Intent(view.getContext(), ListDataKeluargaWargaActivity.class);
                    intent.putExtra("MY_USERID", userId);
                    view.getContext().startActivity(intent);
                }
                if (models.get(position).getTitle().equals("Iuran Bulanan")) {
                    Intent intent = new Intent(view.getContext(), DetailIuranBulananWargaActivity.class);
                    intent.putExtra("MY_USERID", userId);
                    view.getContext().startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
