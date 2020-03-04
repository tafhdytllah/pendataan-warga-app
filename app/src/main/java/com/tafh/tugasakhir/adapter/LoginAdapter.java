package com.tafh.tugasakhir.adapter;

import android.content.Context;

import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class LoginAdapter {

    private ArrayList<ModelUser> models;
    private Context context;

    public LoginAdapter(ArrayList<ModelUser> models, Context context) {
        this.models = models;
        this.context = context;
    }
}
