package com.tafh.tugasakhir.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tafh.tugasakhir.akun.ListDataAkunActivity;
import com.tafh.tugasakhir.iuran.ListIuranBulananActivity;
import com.tafh.tugasakhir.model.ModelItem;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaActivity;
import com.tafh.tugasakhir.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyHolder> {

    private Context context;
    private List<ModelItem> models;
    private String myUserId;

    public GridAdapter(Context context, String myUserId) {
        super();

        this.context = context;
        this.myUserId = myUserId;

        models = new ArrayList<>();

        ModelItem m = new ModelItem();
        m.setTitle("Data Keluarga");
        m.setImage(R.drawable.keluarga);
        models.add(m);

        m = new ModelItem();
        m.setTitle("Data User");
        m.setImage(R.drawable.akungambar);
        models.add(m);

        m = new ModelItem();
        m.setTitle("Iuran Bulanan");
        m.setImage(R.drawable.list_iuran);
        models.add(m);

    }

    //view holder class

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTitle;
        private ItemClickListener itemClickListener;

        public MyHolder(@NonNull View itemView) {
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
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        final ModelItem modelposition = models.get(position);
        final String userId = myUserId;

        holder.mTitle.setText(modelposition.getTitle());
        holder.mImageView.setImageResource(modelposition.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (models.get(position).getTitle().equals("Data Keluarga")) {
                    Intent intent = new Intent(view.getContext(), ListDataKeluargaActivity.class);
                    intent.putExtra("MY_USERID", userId);
                    view.getContext().startActivity(intent);
                }
                if (models.get(position).getTitle().equals("Data User")) {
                    Intent intent = new Intent(view.getContext(), ListDataAkunActivity.class);
                    intent.putExtra("MY_USERID", userId);
                    view.getContext().startActivity(intent);
                }
                if (models.get(position).getTitle().equals("Iuran Bulanan")) {
                    Intent intent = new Intent(view.getContext(), ListIuranBulananActivity.class);
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
