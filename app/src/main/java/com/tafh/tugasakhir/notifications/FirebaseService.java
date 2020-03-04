package com.tafh.tugasakhir.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class FirebaseService extends FirebaseMessagingService{


    private String gmail, currentUser;
    private ArrayList<ModelUser> dataUsers;
    private FirebaseUser fUser;

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG,"Refreshed token: " + token);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        gmail = fUser.getEmail();
        getDataUser();

        if (fUser != null) {

            updateToken(token);

        }
    }


    private void getDataUser() {
        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataUsers.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    user.setUserid(snapshot.getKey());
                    dataUsers.add(user);
                }

                int n = dataUsers.size();
                for (int i=0; i<n; i++) {
                    String userGmail = dataUsers.get(i).getGmail();
                    if (userGmail.equals(gmail)) {
                        currentUser = dataUsers.get(i).getUserid();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateToken(String tokenRefresh) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh);
        ref.child(currentUser).setValue(token);
    }
}
