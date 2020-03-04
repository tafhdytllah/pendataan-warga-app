package com.tafh.tugasakhir.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.MainWargaActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.PostsAdapter;
import com.tafh.tugasakhir.adapter.PostsWargaAdapter;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;
import com.tafh.tugasakhir.model.ModelPost;

import java.util.ArrayList;
import java.util.List;

public class HalloWargaFragment extends Fragment {
    private MenuItem item;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String myUserId, userName, userEmail, userImage;
    private DatabaseReference userDbRef;
    private DatabaseReference databasePosts;
    private TextView txtDataKosong;

    private RecyclerView recyclerView;
    private List<ModelPost> postList;
    private PostsWargaAdapter postsAdapter;
    private ProgressDialog pd;

    public HalloWargaFragment(String myUserId) {
        // Required empty public constructor
        this.myUserId = myUserId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hallo_warga, container, false);

        //init
        auth = FirebaseAuth.getInstance();
        checkUserStatus();
        getDataUser();

        txtDataKosong = view.findViewById(R.id.txt_data_kosong_hallo_warga);
        pd = new ProgressDialog(getContext());

        //recycler view and its properties
        recyclerView = view.findViewById(R.id.rv_list_berita_warga);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);
        //init post list
        postList = new ArrayList<>();

        getAllPosts();

        return view;
    }

    private void getDataUser() {
        //get some info of current user to include in post
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("userId").equalTo(myUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    userName = ""+ ds.child("namaKepalaKeluarga").getValue();
                    userEmail = ""+ ds.child("gmail").getValue();
                    userImage = ""+ ds.child("img").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllPosts() {
//        txtDataKosong.setVisibility(View.INVISIBLE);
        pd.setMessage("Memuat data...");
        pd.show();
        //path of all posts
        databasePosts = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from this ref
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    pd.dismiss();
                    if (databasePosts == null) {
                        txtDataKosong.setVisibility(View.VISIBLE);
                    } else {
                        txtDataKosong.setVisibility(View.INVISIBLE);
                    }
                    modelPost.setPostId(dataSnapshot.getKey());

                    postList.add(modelPost);

                    //adapter
                    postsAdapter = new PostsWargaAdapter(getActivity(), postList, myUserId);
                    //set adapter to recyclerview
                    recyclerView.setAdapter(postsAdapter);

                }

                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error

                pd.dismiss();
                Toast.makeText(getActivity(), "error : "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            //mprofiletv.settext(user.getemail();
        } else {
            //user not signed in, goto main activity
            startActivity(new Intent(getContext(), LoginGmailActivity.class));
            getActivity().finish();
        }
    }


    @Override
    public void onStart() {

        checkUserStatus();
        super.onStart();
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainWargaActivity) getActivity())
                .setActionBarTitle("Berita");

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
        menu.findItem(R.id.action_search).setVisible(false);


        //searchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainWargaActivity) getContext()).getSupportActionBar().getThemedContext());
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
//                    searchUsers(s);
                }
                else {
//                    getAllUsers();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called wheneever user press any single letter
                //if search query is not empty then searc
                if (!TextUtils.isEmpty(s.trim())) {
                    //search text contains text, search it
//                    searchUsers(s);
                }
                else {
//                    getAllUsers();
                }
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    private void signOut() {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Toast.makeText(getContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        checkUserStatus();
    }

}
