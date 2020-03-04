package com.tafh.tugasakhir.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.PersonalChatAdapter;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelChat;
import com.tafh.tugasakhir.model.ModelUser;
import com.tafh.tugasakhir.notifications.APIService;
import com.tafh.tugasakhir.notifications.Client;
import com.tafh.tugasakhir.notifications.Data;
import com.tafh.tugasakhir.notifications.Sender;
import com.tafh.tugasakhir.notifications.Token;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalChatActivity extends AppCompatActivity {

    //views from xml
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageView profileIv, mImageIv;
    private TextView nameTv, userStatusTv;
    private EditText messageEt;
    private ImageButton sendBtn;
    private String hisName, hisUserId, hisImage, myUserId, hisGmail,
            uIdSender, uIdPenerima;
    private Bitmap bmp;

    //for checking if user has seen message or not
    private ValueEventListener dilihatListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDbRef, userRefForDilihat;

    private List<ModelChat> chatList;
    private PersonalChatAdapter personalChatAdapter;

    private ActionBar actionBar;
    private Context context = PersonalChatActivity.this;


    APIService apiService;
    boolean notify = false;

    private static final String TAG = "RESPONSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        getDataIntent();
        //intent dari FirebaseMessaging.java
        Intent intent = getIntent();
        uIdSender= intent.getStringExtra("UID_SENDER");
        uIdPenerima = intent.getStringExtra("UID_PENERIMA");

        if (uIdPenerima != null && uIdSender != null) {
            myUserId = uIdPenerima;
            hisUserId = uIdSender;
        }

        actionBar = getSupportActionBar();

        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mImageIv = findViewById(R.id.iv_image_PC);
        //init vies
        recyclerView = findViewById(R.id.rv_personal_chat);
//        profileIv = findViewById(R.id.profile_iv);
//        nameTv = findViewById(R.id.tv_nama_personal_chat);
//        userStatusTv = findViewById(R.id.tv_user_status);
        messageEt = findViewById(R.id.et_message);
        sendBtn = findViewById(R.id.btn_send);

        //layout (linearlayout) for Recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //recyclerview properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //create apiservice
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

//        search user to get that user's info

        usersDbRef = firebaseDatabase.getReference("Users");
        final Query userQuery = usersDbRef.orderByChild("userId").equalTo(hisUserId);
        //get user pict and name and gmail
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required info is received
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    //get data
                    String name = ""+ ds.child("namaKepalaKeluarga").getValue();
//                    String image = ""+ds.child("img").getValue();
//                    String onlineStatus = ""+ ds.child("onlineStatus").getValue();
//                    if (onlineStatus.equals("online")) {
//                        actionBar.setSubtitle(onlineStatus);
//                    } else {
//                        actionBar.setSubtitle("offline");
////                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
////                        calendar.setTimeInMillis(Long.parseLong(onlineStatus));
////
////                        final String timeOnline = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
////
////                        actionBar.setSubtitle("Last seen at: "+timeOnline);
//                    }


                    //set data
                    actionBar.setTitle(name);
//                    try {
//                        Picasso.with(context).load(image).placeholder(R.drawable.ic_akun_black_24dp)
//                                .into(profileIv);
//
//                    } catch (Exception e) {
//                        Picasso.with(context).load(R.drawable.ic_akun_black_24dp).into(profileIv);
//
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        //click button to send message


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                //get text from edit text
                String message = messageEt.getText().toString().trim();
                //check if text empty or not
                if (TextUtils.isEmpty(message)) {
                    //text empty
                    Toast.makeText(context, "Tidak bisa mengirim pesan yang kosong...", Toast.LENGTH_SHORT).show();
                }
                else {
                    //text not empty
                    sendMessage(message);
                }

                //reset edittext after sending message
                messageEt.setText("");
            }
        });

        readMessages();
        seenMessages();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
//        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //get timestamp
//        String timestamp = String.valueOf(System.currentTimeMillis());
        //set offline with last seen time stamp
//        checkOnlineStatus("offline");
        userRefForDilihat.removeEventListener(dilihatListener);



    }


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null ){

        }
        else {
            startActivity(new Intent(this, LoginGmailActivity.class));
            finish();
        }
    }
//
//    @Override
//    protected void onResume() {
//        checkOnlineStatus("online");
//        super.onResume();
//    }
    //

    private void getDataIntent() {
        Intent intent = getIntent();
        hisName = intent.getStringExtra("HIS_NAME");
        hisUserId = intent.getStringExtra("HIS_UID");
        myUserId = intent.getStringExtra("MY_USERID");
        hisImage = intent.getStringExtra("HIS_IMAGE");
        hisGmail = intent.getStringExtra("HIS_GMAIL");



//        if (hisGmail != null) {
//            Log.w("uidPC", hisGmail);
//        } else {
//            Log.w("uidPC", "gmail null");
//        }

//        Bundle extras = getIntent().getExtras();
//        bmp = extras.getParcelable("IMAGEBITMAP");
//
//
//        mImageIv.setImageBitmap(bmp);

    }

    private void checkOnlineStatus(String status) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(myUserId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        //update value of olineStatus of current user
        dbRef.updateChildren(hashMap);
    }

    private void seenMessages() {
        userRefForDilihat = FirebaseDatabase.getInstance().getReference("Chats");
        dilihatListener = userRefForDilihat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUserId) && chat.getSender().equals(hisUserId)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("dilihat", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);

                    if (chat.getReceiver().equals(myUserId) && chat.getSender().equals(hisUserId) ||
                            chat.getReceiver().equals(hisUserId) && chat.getSender().equals(myUserId)) {

                        chatList.add(chat);
                    }

                    //adapter
                    personalChatAdapter = new PersonalChatAdapter(PersonalChatActivity.this, chatList, hisImage, myUserId);
                    personalChatAdapter.notifyDataSetChanged();
                    //set adapter to recyclerview
                    recyclerView.setAdapter(personalChatAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(final String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUserId);
        hashMap.put("receiver", hisUserId);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("dilihat", false);

        databaseReference.child("Chats").push().setValue(hashMap);


        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUserId);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ModelUser user = dataSnapshot.getValue(ModelUser.class);
                //notify = true
                if (notify) {

                    sendNotification(hisUserId, user.getNamaKepalaKeluarga(), message);

                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(final String hisUserId, final String namaKepalaKeluarga, final String message) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUserId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String noKK = "null";
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUserId, namaKepalaKeluarga+": "+message, "Pesan baru", hisUserId, noKK, R.drawable.ic_notif);
                    Sender sender = new Sender(data, token.getToken());
//                    final String Mtoken = token.getToken();
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.d(TAG,"Successfully notification send by using retrofit.");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d(TAG,"failure.");
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        //hide searchvier, we dont need it
//        menu.findItem(R.id.action_sortnomor).setVisible(false);
//        menu.findItem(R.id.action_sortnama).setVisible(false);
//        menu.findItem(R.id.action_logout);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//        if (id == R.id.action_logout) {
//            //logoout
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity
        return super.onSupportNavigateUp();
    }



}
