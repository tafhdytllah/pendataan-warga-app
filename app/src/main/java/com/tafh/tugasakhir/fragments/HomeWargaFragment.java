package com.tafh.tugasakhir.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.tafh.tugasakhir.MainActivity;
import com.tafh.tugasakhir.MainWargaActivity;
import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.adapter.GridWargaAdapter;
import com.tafh.tugasakhir.login.gmail.LoginGmailActivity;

public class HomeWargaFragment extends Fragment {
    private FirebaseAuth auth;
    GridWargaAdapter myAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private String myUserId;

    public HomeWargaFragment(String myUserId) {
        this.myUserId = myUserId;
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainWargaActivity) getActivity())
                .setActionBarTitle("Beranda");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home_warga, container, false );
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mRecyclerView = view.findViewById(R.id.rv_home_warga);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new GridWargaAdapter(getActivity(), myUserId);
        mRecyclerView.setAdapter(myAdapter);

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
        startActivity(new Intent(getContext(), LoginGmailActivity.class));
        getActivity().finish();
    }


//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
////        Toast.makeText(getContext(), "Maaf Kode Tidak Cocok", Toast.LENGTH_LONG).show();
//
//        if (result != null && result.getContents() != null) {
////
////            if (result.getContents().equals("RT15")){
////                Intent intent = new Intent(getContext(), getView().getClass());
////                startActivity(intent);
////            } else {
////                Toast.makeText(getContext(), "Maaf Kode Tidak Cocok", Toast.LENGTH_LONG).show();
////
////                Intent intent = new Intent(getContext(), getView().getClass());
////                startActivity(intent);
////            }
//
//            new AlertDialog.Builder(getContext())
//                    .setTitle("Scan Result")
//                    //isi pesan
//                    .setMessage(result.getContents())
//
//                    .setPositiveButton("copy", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                            ClipData data = ClipData.newPlainText("result", result.getContents());
//                            manager.setPrimaryClip(data);
//
//                        }
//                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    dialogInterface.dismiss();
//                }
//            }).create().show();
//
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }


}
