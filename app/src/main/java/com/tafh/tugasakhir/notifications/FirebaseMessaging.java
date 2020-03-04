package com.tafh.tugasakhir.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.MainWargaActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.chats.PersonalChatActivity;
import com.tafh.tugasakhir.fragments.KeluargaKeluarFragment;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaActivity;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaWargaActivity;
import com.tafh.tugasakhir.model.ModelChat;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class FirebaseMessaging extends FirebaseMessagingService {

    private FirebaseUser fUser;
    private static final String TAG = "Nilai";
    private String userIdPenerima;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
//        Log.d("NEW_TOKEN : ", s);

        if (fUser != null) {

            updateToken(s);

        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getData().get("sent") +" + "+remoteMessage.getData().get("user"));
//        get current user from shared preferences

        SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
        userIdPenerima = sp.getString("myUserId", "None");
//        userIdPenerima = "-LyeI_2X9asBz2ssrdCZ";
//        Log.w(TAG,""+ userIdPenerima);
        //sent = useridpenerima
        //user = pengirim

        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");
        String noKK = remoteMessage.getData().get("noKK");
        String noData = "null";
//        Log.w("USERID_PENGIRIM", sent);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null && sent.equals(userIdPenerima) && noKK.equals(noData)) {
            if (!userIdPenerima.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOAndAvoceNotification(remoteMessage);
                }
                else {
                    sendNormalNotification(remoteMessage);
                }
            }
        } else  {
            if (!userIdPenerima.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOAndAvoceNotifKeluargaKeluar(remoteMessage);
                }
                else {
                    sendNormalNotifKeluargaKeluar(remoteMessage);
                }
            }
        }

    }


    private void updateToken(String tokenRefresh) {
//

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh);
//        Log.d(TAG, "From: " + token);
        ref.child(userIdPenerima).setValue(token);
    }

    private void sendNormalNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        //userId pengirim
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, PersonalChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("UID_SENDER", user);
        bundle.putString("UID_PENERIMA", userIdPenerima);
//        Log.w("uidsender",user);

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int j = 0;
        if (i>0) {
            j=i;
        }
        notificationManager.notify(j,builder.build());

    }

    private void  sendOAndAvoceNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]",""));
        Log.w("INTEGER", ""+i);
        Intent intent = new Intent(this, PersonalChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("UID_SENDER", user);
        bundle.putString("UID_PENERIMA", userIdPenerima);
//        Log.w("uidsender",user);

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
        Notification.Builder builder = notification1.getONotifications(title, body, pIntent, defSoundUri, icon);

        int j = 0;
        if (i>0) {
            j=i;
        }
        notification1.getManager().notify(j,builder.build());

    }

    private void sendNormalNotifKeluargaKeluar(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        //userId pengirim
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]",""));


        Bundle bundle = new Bundle();
        bundle.putString("UID_SENDER", user);
        bundle.putString("UID_PENERIMA", userIdPenerima);
//        Log.w("uidsender",user);


        Intent activityIntent = new Intent(this, ListDataKeluargaActivity.class);

        activityIntent.putExtras(bundle);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, i, activityIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent broadcastIntent = new Intent(this, MainWargaActivity.class);
        broadcastIntent.putExtra("toast message", body);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("No RUmah: 5 ")
                        .addLine("Nik: 582782782728928928 ")
                        .addLine("Nama: Topik")
                        .setBigContentTitle("Warga yang sudah pindah")
                        .setSummaryText("ABC"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Toast", actionIntent)
                .setSound(defSoundUri)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int j = 0;
        if (i>0) {
            j=i;
        }
        notificationManager.notify(j,builder.build());

    }

    private void sendOAndAvoceNotifKeluargaKeluar(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, ListDataKeluargaActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("UID_SENDER", user);
        bundle.putString("UID_PENERIMA", userIdPenerima);
//        Log.w("uidsender",user);

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
        Notification.Builder builder = notification1.getONotifications(title, body, pIntent, defSoundUri, icon);

        int j = 0;
        if (i>0) {
            j=i;
        }
        notification1.getManager().notify(j,builder.build());

    }
}
