package com.tafh.tugasakhir.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tafh.tugasakhir.R;

public class MyHolderWarga extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageView;
    TextView mTitle;
    ItemClickListener itemClickListener;

    public MyHolderWarga(@NonNull View itemView) {
        super(itemView);

        mImageView = itemView.findViewById(R.id.iv_image);

        mTitle = itemView.findViewById(R.id.tv_title);

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
