package com.tafh.tugasakhir.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.chats.PersonalChatActivity;
import com.tafh.tugasakhir.model.ModelUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyHolder>{

    private Context context;
    private List<ModelUser> userList;
    private String myUserId;

    //constructor
    public ChatsAdapter(Context context, List<ModelUser> userList, String myUserId) {
        this.context = context;
        this.userList = userList;
        this.myUserId = myUserId;
    }

    //view holder class
    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mAvatarIv;
        private TextView mNameTv, mEmailTv, userImage;
        private ItemClickListener itemClickListener;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init vies
            mAvatarIv = itemView.findViewById(R.id.iv_avatar_users);
            mNameTv = itemView.findViewById(R.id.tv_nama_users);
            mEmailTv = itemView.findViewById(R.id.tv_email_users);

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
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout(row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup, false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {


        //get data
        final String hisUID = userList.get(i).getUserid();
        final String userImage = userList.get(i).getImg();
        final String userName = userList.get(i).getNamaKepalaKeluarga();
        final String userEmail = userList.get(i).getGmail();

        //set data
        myHolder.mNameTv.setText(userName);
        myHolder.mEmailTv.setText(userEmail);
        try {
            Picasso.with(context)
                    .load(userImage)
                    .placeholder(R.drawable.ic_akun_black_24dp)
                    .into(myHolder.mAvatarIv);

        } catch (Exception e) {

        }

        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

//                Toast.makeText(context, ""+userEmail, Toast.LENGTH_SHORT).show();
                //click user from user list to start chatting/messaging
                //start activity by putting UID of receiver
                //we will user that UID to identify the user we are gonna chatt

                Intent intent = new Intent(context, PersonalChatActivity.class);

                intent.putExtra("HIS_UID", hisUID);
                intent.putExtra("HIS_NAME", userName);
                intent.putExtra("MY_USERID", myUserId);
                intent.putExtra("HIS_IMAGE", userImage);
                intent.putExtra("HIS_GMAIL", userEmail);

//                myHolder.mAvatarIv.buildDrawingCache();
//                Bitmap image = myHolder.mAvatarIv.getDrawingCache();
//
//                Bundle extras = new Bundle();
//                extras.putParcelable("IMAGEBITMAP", image);
//                intent.putExtras(extras);

                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return userList.size();
    }


}
