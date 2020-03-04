package com.tafh.tugasakhir.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.model.ModelPost;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostsWargaAdapter extends RecyclerView.Adapter<PostsWargaAdapter.myHolder> {

    Context context;
    List<ModelPost> postList;
    private String myUserId;

    public PostsWargaAdapter(Context context, List<ModelPost> postList, String myUserId) {
        this.context = context;
        this.postList = postList;
        this.myUserId = myUserId;
    }


    //view holder class
    class myHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        private ImageView userImageIv, postImageIv;
        private TextView userNameTv, timeTv, titleTv, descrTv, likeTv;
        private ImageButton moreBtn;
        private Button likeBtn, commentBtn, shareBtn;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            userNameTv = itemView.findViewById(R.id.uNameTv);
            userImageIv = itemView.findViewById(R.id.uPictureIv);

            postImageIv = itemView.findViewById(R.id.pImageIv);
            timeTv = itemView.findViewById(R.id.pTimeTv);
            titleTv = itemView.findViewById(R.id.pTitleTv);
            descrTv = itemView.findViewById(R.id.pDescriptionTv);
//            pLikesTv = itemView.findViewById(R.id.pLikesTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            moreBtn.setVisibility(View.GONE);

            likeBtn = itemView.findViewById(R.id.likeBtn);
            likeBtn.setVisibility(View.GONE);

            commentBtn = itemView.findViewById(R.id.commentBtn);
            commentBtn.setVisibility(View.GONE);
//            shareBtn = itemView.findViewById(R.id.shareBtn);
        }
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup ViewGroup, int i) {
        //inflate layout row_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_berita, ViewGroup, false);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int i) {
        //get data
//        final String setUserId = postList.get(i).getUserId();
//        final String setEmail = postList.get(i).getUserEmail();
        final String setName = postList.get(i).getUserName();
        final String setUserImage = postList.get(i).getUserImage();

//        final String setPostId = postList.get(i).getpId();
        final String setTitle = postList.get(i).getpTitle();
        final String setDesc = postList.get(i).getpDescr();
        final String setPostImage = postList.get(i).getpImage();
        final String setTimeStamp = postList.get(i).getpTime();

        //convert timestamp to dd/mm/yyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(setTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        holder.userNameTv.setText(setName);
        holder.timeTv.setText(pTime);
        holder.titleTv.setText(setTitle);
        holder.descrTv.setText(setDesc);

        //set user image
        try {
            Picasso.with(context).load(setUserImage).placeholder(R.drawable.ic_akun_black_24dp).into(holder.userImageIv);
        }
        catch (Exception e) {

        }
        //set post image
        //if there is no image o.e. pImage.equals("noImage") then hide imageview
        if (setPostImage.equals("noImage")) {
            //hide imageview
            holder.postImageIv.setVisibility(View.GONE);
        }
        else {
            try {
                Picasso.with(context).load(setPostImage).into(holder.postImageIv);
            }
            catch (Exception e) {

            }
        }



        //handle button click
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will implement later
                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();

            }
        });
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will implement later
                Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();

            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will implement later
                Toast.makeText(context, "comment", Toast.LENGTH_SHORT).show();

            }
        });
//        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //will implement later
//                Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
