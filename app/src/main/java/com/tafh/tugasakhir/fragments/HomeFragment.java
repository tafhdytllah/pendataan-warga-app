package com.tafh.tugasakhir.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.GridAdapter;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;

public class HomeFragment extends Fragment {
    private FirebaseAuth auth;
    private GridAdapter myAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String myUserId;

    public HomeFragment(String myUserId) {
        this.myUserId = myUserId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false );
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mRecyclerView = view.findViewById(R.id.rv_home);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        if (myUserId == null) {
//
//            Log.w("INFORMASI", "null");
//
//        } else {
//
//            Log.w("INFORMASI", myUserId);
//
//        }
        myAdapter = new GridAdapter(getActivity(), myUserId);
        mRecyclerView.setAdapter(myAdapter);

//        final FragmentActivity c = getActivity();
//        final RecyclerView recyclerView = view.findViewById(R.id.rv_home);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
//
//        recyclerView.setLayoutManager(layoutManager);
//
//        myAdapter = new GridAdapter(this, getMylist());
//        myAdapter = new GridAdapter();
//
//        recyclerView.setAdapter(myAdapter);

        return view;
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
        startActivity(new Intent(getContext(), LoginGmailActivity.class));
        getActivity().finish();
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("Beranda");

    }

//    private ArrayList<ModelItem> getMylist() {
//
//        ArrayList<ModelItem> models = new ArrayList<>();
//
//        ModelItem m = new ModelItem();
//        m.setTitle("Data Keluarga");
//        m.setImage(R.drawable.keluarga);
//        models.add(m);
//
//        m = new ModelItem();
//        m.setTitle("Data Akun Warga");
//        m.setImage(R.drawable.akungambar);
//        models.add(m);
//
//        m = new ModelItem();
//        m.setTitle("Data baru");
//        m.setImage(R.drawable.ic_hallo_black_24dp);
//        models.add(m);
//
//        m = new ModelItem();
//        m.setTitle("Data Keluarga");
//        m.setImage(R.drawable.keluarga);
//        models.add(m);
//
//        m = new ModelItem();
//        m.setTitle("Data Akun Warga");
//        m.setImage(R.drawable.akungambar);
//        models.add(m);
//
//        m = new ModelItem();
//        m.setTitle("Data baru");
//        m.setImage(R.drawable.ic_hallo_black_24dp);
//        models.add(m);
//
//        return models;
//    }
}


