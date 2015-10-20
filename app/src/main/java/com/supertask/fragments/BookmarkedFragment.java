package com.supertask.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.supertask.Bookmark;
import com.supertask.BookmarkDbHelper;
import com.supertask.DisplayImageAdapter;
import com.supertask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pratik on 18/10/15.
 */
public class BookmarkedFragment extends Fragment {

    private TextView bookmarkTextView;
    private GridView imageGridView;
    private List<String> pathOfImageFiles;
    private DisplayImageAdapter displayImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarked, container, false);
        bookmarkTextView = (TextView) view.findViewById(R.id.bookmark_text_view);
        imageGridView = (GridView) view.findViewById(R.id.bookmarked_grid_view);
        bookmarkTextView.setText(R.string.no_bookmark_message);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BookmarkDbHelper bookmarkDbHelper = new BookmarkDbHelper(getActivity());
        ArrayList<Bookmark> bookmarks = bookmarkDbHelper.getAllBookmark();
        pathOfImageFiles = new ArrayList<>();
        if (bookmarks.size() > 0) {
            bookmarkTextView.setText("The number of bookmarked items found: " + bookmarks.size());
            for (Bookmark bookmark : bookmarks) {
                pathOfImageFiles.add(bookmark.getShirt());
                pathOfImageFiles.add(bookmark.getPant());
            }
            displayImageAdapter = new DisplayImageAdapter(getActivity(), pathOfImageFiles);
            imageGridView.setAdapter(displayImageAdapter);
        }
    }
}
