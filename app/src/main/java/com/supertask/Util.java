package com.supertask;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pratik on 18/10/15.
 */
public class Util {

    public static File getPathToStorage(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static String getFileNameForShirt() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "shirt_" + timeStamp + "_";
        return imageFileName;
    }

    public static String getFileNameForPant() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "pant_" + timeStamp + "_";
        return imageFileName;
    }

}