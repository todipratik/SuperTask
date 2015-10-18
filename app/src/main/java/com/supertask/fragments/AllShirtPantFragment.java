package com.supertask.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supertask.R;

/**
 * Created by pratik on 18/10/15.
 */
public class AllShirtPantFragment extends Fragment {

    public static final String ARG_POSITION;

    private Integer position;

    static {
        ARG_POSITION = "position";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_shirt_pant, container, false);
        position = getArguments().getInt(ARG_POSITION);
        return view;
    }
}
