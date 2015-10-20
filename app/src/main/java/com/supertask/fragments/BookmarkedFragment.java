package com.supertask.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supertask.Bookmark;
import com.supertask.BookmarkDbHelper;
import com.supertask.R;

import java.util.ArrayList;

/**
 * Created by pratik on 18/10/15.
 */
public class BookmarkedFragment extends Fragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarked, container, false);
        textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText("hey this is your bookmarked tab!!!\n");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BookmarkDbHelper bookmarkDbHelper = new BookmarkDbHelper(getActivity());
        ArrayList<Bookmark> a = bookmarkDbHelper.getAllBookmark();
        textView.append("The size is: " + a.size() + "\n");
        for (Bookmark b : a) {
            textView.append(b.getShirt() + "\n");
            textView.append(b.getPant() + "\n\n");
        }
    }
}
