package com.supertask.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supertask.R;

/**
 * Created by pratik on 18/10/15.
 */
public class BookmarkedFragment extends Fragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarked, container, false);
        textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText("hey this is your bookmarked tab!!!");
        return view;
    }
}
