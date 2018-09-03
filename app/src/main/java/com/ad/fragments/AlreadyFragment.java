package com.ad.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ad.R;


public class AlreadyFragment extends Fragment {

    private static final String TAG = "AlreadyFragment";

    View mNewsView;
    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.already_fragment_layout, null);
        mContext = getActivity();


        return mNewsView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
