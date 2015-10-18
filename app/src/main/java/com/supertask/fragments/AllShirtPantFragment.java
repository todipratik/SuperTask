package com.supertask.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.supertask.DisplayImageAdapter;
import com.supertask.DisplayImageLoader;
import com.supertask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pratik on 18/10/15.
 */
public class AllShirtPantFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>> {

    public static final String ARG_POSITION;

    private GridView imageGridView;
    private List<String> pathOfImageFiles;
    private DisplayImageAdapter displayImageAdapter;

    private Integer position;

    static {
        ARG_POSITION = "position";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_shirt_pant, container, false);
        imageGridView = (GridView) view.findViewById(R.id.images_grid_view);
        pathOfImageFiles = new ArrayList<>();
//        position = getArguments().getInt(ARG_POSITION);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayImageAdapter = new DisplayImageAdapter(getActivity(), pathOfImageFiles);
        imageGridView.setAdapter(displayImageAdapter);
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int i, Bundle bundle) {
        return new DisplayImageLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> paths) {
        pathOfImageFiles.clear();
        for (String path : paths)
            pathOfImageFiles.add(path);
        displayImageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        pathOfImageFiles.clear();
        displayImageAdapter.clear();
    }
}