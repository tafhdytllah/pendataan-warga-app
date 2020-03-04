package com.tafh.tugasakhir.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.ui.ProgressDialogHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.model.ModelPost;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.myHolder> {

    Context context;
    List<ModelPost> postList;
    private String myUserId, userImage;

    public PostsAdapter(Context context, List<ModelPost> postList, String myUserId, String userImage) {
        this.context = context;
        this.postList = postList;
        this.myUserId = myUserId;
        this.userImage = userImage;
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
    public void onBindViewHolder(@NonNull final myHolder holder, int i) {
        //get data
        final String setUserId = postList.get(i).getUserId();
        String setEmail = postList.get(i).getUserEmail();
        String setName = postList.get(i).getUserName();
        String setUserImage = postList.get(i).getUserImage();
        if (setUserImage != null) {
            Log.w("IMAGE_USER_POST", setUserImage);
        }

        //pId tidak ketarik datanya
        final String setPostId = postList.get(i).getPostId();
        String setTitle = postList.get(i).getpTitle();
        String setDesc = postList.get(i).getpDescr();
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
            //show imageview
            holder.postImageIv.setVisibility(View.VISIBLE);

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
                showMoreOptions(holder.moreBtn, setUserId, myUserId, setTimeStamp, setPostImage);

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

    private void showMoreOptions(ImageButton moreBtn, String setUserId, String myUserId, final String pId, final String setPostImage) {
        //creating popup menu currently having option delete, and more option later
        if (pId == null) {

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);

            //show delete option in only post(s) of currently signuser
            if (setUserId.equals(myUserId)) {
                //add items in menu
                popupMenu.getMenu().add(Menu.NONE, 0, 0, "Hapus");

            }

            //item click listener
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    int id = menuItem.getItemId();
                    if (id==0) {
                        //delete is clicked
                        beginDelete(pId, setPostImage);
                    }

                    return false;
                }
            });
            //show menu

            popupMenu.show();
        }



    }

    private void beginDelete(String pId, String pImage) {

        //post can be with or without image
        if (pImage.equals("noImage")) {
            //post is without image
            Toast.makeText(context, "Belum ada perintah Hapus tanpa gambar",Toast.LENGTH_SHORT).show();
//            deleteWithoutImage(setPostId);
        }
        else {
            //post is with image
//            Log.w("INFORMASI", "berhasil dengan image");
            deleteWithImage(pId, pImage);

        }
    }

    private void deleteWithImage(final String pId, String pImage) {
        //PROGRESSBAR
        if (pId == null) {
            Log.w("INFORMASI", "DATA KOSONG");
        }
        else {
            Log.w("INFORMASI", pId);
        }

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Menghapus berita...");

        // 1 delet image using url
        // 2 delete from database using post id

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image deleted, now delete database

                        Query fquery = FirebaseDatabase.getInstance().getReference("Posts")
                                .orderByChild("postId").equalTo(pId);
                        fquery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue(); //remove value from firebase where pid matches

                                }
                                //deleted
                                Toast.makeText(context, "Berita berhasil dihapus", Toast.LENGTH_SHORT).show();
                                postList.clear();
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed, can't go further
                        pd.dismiss();
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteWithoutImage(String setPostId) {
        //PROGRESSBAR
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");

        Query fquery = FirebaseDatabase.getInstance().getReference("Posts")
                .orderByChild("pId").equalTo(setPostId);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ds.getRef().removeValue(); //remove value from firebase where pid matches

                }
                //deleted
                Toast.makeText(context, "Berita berhasil dihapus", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
