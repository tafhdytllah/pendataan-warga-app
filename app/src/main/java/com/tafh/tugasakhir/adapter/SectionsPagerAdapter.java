package com.tafh.tugasakhir.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tafh.tugasakhir.R;
import com.tafh.tugasakhir.fragments.KeluargaAktifFragment;
import com.tafh.tugasakhir.fragments.KeluargaKeluarFragment;
import com.tafh.tugasakhir.model.ModelUser;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String[] titles ={"A","B"};
    private String myUserId;
    private static final int[] TAB_TITLES = new int[]{R.string.tab_aktif, R.string.tab_keluar};

    public SectionsPagerAdapter(Context c, FragmentManager fm, String myUserId){
        super(fm);
        mContext = c;
        this.myUserId = myUserId;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                KeluargaAktifFragment tabAktif = new KeluargaAktifFragment(myUserId);
                return  tabAktif;
            case 1:
                KeluargaKeluarFragment tabKeluar = new KeluargaKeluarFragment(myUserId);
                return tabKeluar;

        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public CharSequence getPageTitle(int position){
        return mContext.getResources()
                .getString(TAB_TITLES[position]);
    }
//////////////////////////////////////////////////////////////
}
