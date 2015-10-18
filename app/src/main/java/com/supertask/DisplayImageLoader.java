package com.supertask;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pratik on 18/10/15.
 */
public class DisplayImageLoader extends AsyncTaskLoader<List<String>> {

    private Context context;

    public DisplayImageLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<String> loadInBackground() {
        File file = Util.getPathToStorage(context);
        List<String> pathOfImageFiles = new ArrayList<>();
        File[] allFiles = file.listFiles();
        long index = 0;
        while (index < allFiles.length) {
            File f = allFiles[((int) index)];
            if (f.length() > 0) {
                String path = f.getAbsolutePath();
                pathOfImageFiles.add(path);
            }
            index++;
        }
        return pathOfImageFiles;
    }
}
