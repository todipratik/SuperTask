package com.supertask;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by pratik on 18/10/15.
 */
public class DisplayImageLoader extends AsyncTaskLoader<List<String>> {

    private Context context;
    private Integer position;

    public DisplayImageLoader(Context context, Integer position) {
        super(context);
        this.context = context;
        this.position = position;
    }

    @Override
    public List<String> loadInBackground() {
        if (position == 1) {
            return Util.getAllShirtPaths(context);
        }
        return Util.getAllPantPaths(context);
    }
}
