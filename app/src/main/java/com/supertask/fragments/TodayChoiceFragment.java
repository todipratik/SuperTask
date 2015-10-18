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
public class TodayChoiceFragment extends Fragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_choice, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("Hey This is your choice for today!!!!");
        return view;
    }
}
