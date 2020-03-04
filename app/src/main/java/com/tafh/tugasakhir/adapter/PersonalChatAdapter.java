package com.tafh.tugasakhir.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.model.ModelChat;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PersonalChatAdapter extends RecyclerView.Adapter<PersonalChatAdapter.MyHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<ModelChat> chatList;
    private String hisImage, myUserId;
    private FirebaseUser fUser;

    public PersonalChatAdapter(Context context, List<ModelChat> chatList, String hisImage, String myUserId) {
        this.context = context;
        this.chatList = chatList;
        this.hisImage = hisImage;
        this.myUserId = myUserId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout: row_chat_left.xml for receiver, row chat right for sender

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        }
        else {


            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        //get data
        final String message = chatList.get(i).getMessage();
        final String timeStamp = chatList.get(i).getTimestamp();
        final Boolean dilihat = chatList.get(i).isDilihat();
        //convert timestamp to dd/mm/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timeStamp));

        final String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);


//        if (dilihat) {
//            holder.dilihatTv.setText("udah");
//        } else {
//            holder.dilihatTv.setText("belom");
//        }
//
//        try {
//            Picasso.with(context).load(hisImage).placeholder(R.drawable.ic_akun_black_24dp).into(holder.profileIv);
//        }
//        catch (Exception e) {
//
//        }


        if (i==chatList.size()-1) {
            if (chatList.get(i).isDilihat()){
                holder.dilihatTv.setText("Seen");
            }
            else {
                holder.dilihatTv.setText("Delivered");
            }
        }
        else {
            holder.dilihatTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(myUserId)) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }

    //view holder class
    public class MyHolder extends RecyclerView.ViewHolder {

        //views
        private ImageView profileIv;
        private TextView messageTv, timeTv, dilihatTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init vies
            profileIv = itemView.findViewById(R.id.iv_profile_personal_chat);
            messageTv = itemView.findViewById(R.id.tv_message);
            timeTv = itemView.findViewById(R.id.tv_time);
            dilihatTv = itemView.findViewById(R.id.tv_dilihat);
        }
    }
}
