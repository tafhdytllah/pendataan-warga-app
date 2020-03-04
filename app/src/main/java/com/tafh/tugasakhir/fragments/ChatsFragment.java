package com.tafh.tugasakhir.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.ChatsAdapter;
import com.tafh.tugasakhir.keluarga.ListDataKeluargaActivity;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ChatsAdapter chatsAdapter;
    private ArrayList<ModelUser> dataUsers;

    private DatabaseReference dfUsers, dfChats;
    private String getName, getEmail, getSearch, getPhone, getImage, getCover, getChatsId;
    private TextView coba;
    private String myUserId;
    private ActionBar actionBar;

    public ChatsFragment(String myUserId) {
        // Required empty public constructor
        this.myUserId = myUserId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        auth = FirebaseAuth.getInstance();

        coba = view.findViewById(R.id.text_coba);
        //init recyclerview
        recyclerView = view.findViewById(R.id.rv_users_chats);
        //set it's properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init user list
        dataUsers = new ArrayList<>();
//        dataUsers1 = new ArrayList<>();
//        dataUsers2 = new ArrayList<>();

//        getDataUsers();
        //getAll users
        getAllUsers();

        return view;
    }

//    private void getDataUsers() {
//
//        dfUsers = FirebaseDatabase.getInstance().getReference("Users");
//        dfUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                dataUsers = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    //Mapping data pada DataSnapshot ke dalam objek akun
//                    ModelUser akun = snapshot.getValue(ModelUser.class);
//
//                    akun.setUserId(snapshot.getKey());
//                    dataUsers.add(akun);
//                }
//
//                int n = dataUsers.size();
//                for (int i=0; i<n; i++) {
//
//                    getName = dataUsers.get(i).getNamaKepalaKeluarga();
//                    getEmail = dataUsers.get(i).getGmail();
//                    getSearch = "null";
//                    getPhone = dataUsers.get(i).getNoHp();
//                    getImage = dataUsers.get(i).getImg();
//                    getCover = "null";
//                    getChatsId = dataUsers.get(i).getUserId();
//
//                    //we will store the additional fields in firebase database
//                    ModelChats userchats = new ModelChats(
//                            getName,
//                            getEmail,
//                            getSearch,
//                            getPhone,
//                            getImage,
//                            getCover,
//                            getChatsId
//                    );
//                    dfChats = FirebaseDatabase.getInstance().getReference("Chats");
//                    String id = dfChats.push().getKey();
//                    dfChats.child(id)
//                            .setValue(userchats)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                }
//                            });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    public void onStart() {

        checkUserStatus();
        super.onStart();
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Chats");

    }

    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null ){
//            Toast.makeText(getContext(), "userid : "+userId,Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(getContext(), LoginGmailActivity.class));
            getActivity().finish();
        }
    }

    private void getAllUsers() {

        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        final String getEmailUser = fUser.getEmail();
        //get path of database named "Users" containing user info
        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
        //get all data from path
        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataUsers.clear();
//                dataUsers2.clear();
//                dataUsers1.clear();


//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    ModelUser user = snapshot.getValue(ModelUser.class);
//                    user.setUserid(snapshot.getKey());
//                    dataUsers1.add(user);
//                }
//
//
//                int n = dataUsers1.size();
//                for (int i=0; i<n; i++) {
//                    String gmail = dataUsers1.get(i).getGmail();
//                    if (gmail.equals(getEmailUser)) {
//                        getUserChatId = dataUsers1.get(i).getUserid();
////                        coba.setText(dataUsers.get(i).getUserid());
//                    }
//                }

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ModelUser chats = snapshot.getValue(ModelUser.class);
                    chats.setUserid(snapshot.getKey());

                    //get all users except curently signed in user
                    if (!chats.getUserid().equals(myUserId)) {
                        //adapter
                        dataUsers.add(chats);
                    }
                    chatsAdapter = new ChatsAdapter(getContext(), dataUsers, myUserId);
                    //set adapter to recycler view
                    recyclerView.setAdapter(chatsAdapter);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUsers(final String query) {
        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        final String getEmailUser = fUser.getEmail();
        //get path of database named "Users" containing user info
        final DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("Users");
        //get all data from path
        drUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataUsers2.clear();
//                dataUsers1.clear();


//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    ModelUser user = snapshot.getValue(ModelUser.class);
////                    user.setUserid(snapshot.getKey());
//                    dataUsers1.add(user);
//                }
//                int n = dataUsers1.size();
//                for (int i=0; i<n; i++) {
//                    String gmail = dataUsers1.get(i).getGmail();
//                    if (gmail.equals(getEmailUser)) {
//                        getUserChatId = dataUsers1.get(i).getUserid();
////                        coba.setText(dataUsers.get(i).getUserid());
//                    }
//                }
                dataUsers.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ModelUser chats = snapshot.getValue(ModelUser.class);
                    chats.setUserid(snapshot.getKey());


                    if (!chats.getUserid().equals(myUserId)) {
                        if (chats.getNamaKepalaKeluarga().toLowerCase().contains(query.toLowerCase()) ||
                                chats.getGmail().toLowerCase().contains(query.toLowerCase())) {

                            dataUsers.add(chats);

                            chatsAdapter = new ChatsAdapter(getContext(), dataUsers, myUserId);
                            //set adapter to recycler view
                            recyclerView.setAdapter(chatsAdapter);

                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "error : "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);//to show menu option in fragment
        super.onCreate(savedInstanceState);
    }

    //inflate option menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //inflating menu
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_sortnomor).setVisible(false);
        menu.findItem(R.id.action_sortnama).setVisible(false);
        menu.findItem(R.id.action_logout);
        menu.findItem(R.id.action_search);

        //searchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, searchView);
        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button from keyboard
                //if search query is not empty then searc
                if (!TextUtils.isEmpty(s.trim())) {
                    //search text contains text, search it
//                    Toast.makeText(getContext(), "Joss"+s,Toast.LENGTH_SHORT).show();
                    searchUsers(s);
                }
                else {
                    getAllUsers();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called wheneever user press any single letter
                //if search query is not empty then searc
                if (!TextUtils.isEmpty(s.trim())) {
                    //search text contains text, search it
                    searchUsers(s);
                }
                else {
                    getAllUsers();
                }
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            auth.signOut();
            auth.signOut();
            Toast.makeText(getContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }
}
