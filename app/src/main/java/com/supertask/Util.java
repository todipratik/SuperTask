package com.supertask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by pratik on 18/10/15.
 */
public class Util {

    private static final String PREF_NAME = "SuperTaskSettings";

    public static final String KEY_CHOICE_SET = "choice_set";
    public static final String KEY_SHIRT_PATH = "shirt_path";
    public static final String KEY_PANT_PATH = "pant_path";
    public static final String KEY_BOOKMARKED = "bookmarked";
    public static final String KEY_SHIRT_PRESENT = "shirt_present";
    public static final String KEY_PANT_PRESENT = "pant_present";

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

    public static SharedPreferences getSharedPrefsObject(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static List<String> getAllShirtPaths(Context context) {
        File file = getPathToStorage(context);
        File[] allFiles = file.listFiles();
        List<String> pathOfImageFiles = new ArrayList<>();
        long index = 0;
        while (index < allFiles.length) {
            File f = allFiles[((int) index)];
            if (f.length() > 0) {
                String path = f.getAbsolutePath();
                if (f.getName().startsWith("shirt"))
                    pathOfImageFiles.add(path);
            }
            index++;
        }
        return pathOfImageFiles;
    }

    public static List<String> getAllPantPaths(Context context) {
        File file = getPathToStorage(context);
        File[] allFiles = file.listFiles();
        List<String> pathOfImageFiles = new ArrayList<>();
        long index = 0;
        while (index < allFiles.length) {
            File f = allFiles[((int) index)];
            if (f.length() > 0) {
                String path = f.getAbsolutePath();
                if (f.getName().startsWith("pant"))
                    pathOfImageFiles.add(path);
            }
            index++;
        }
        return pathOfImageFiles;
    }

    public static void setShirtForToday(Context context) {
        List<String> shirts = getAllShirtPaths(context);
        if (shirts.size() == 0)
            return;
        String shirtPath = "";
        SharedPreferences sharedPreferences = getSharedPrefsObject(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Integer randomNumber = randomNumber(0, shirts.size() - 1);
        shirtPath = shirts.get(randomNumber);
        editor.putBoolean(KEY_SHIRT_PRESENT, true);
        editor.putString(KEY_SHIRT_PATH, shirtPath);
        editor.commit();
        return;
    }

    public static void setPantForToday(Context context) {
        List<String> pants = getAllPantPaths(context);
        if (pants.size() == 0)
            return;
        String pantPath = "";
        SharedPreferences sharedPreferences = getSharedPrefsObject(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Integer randomNumber = randomNumber(0, pants.size() - 1);
        pantPath = pants.get(randomNumber);
        editor.putBoolean(KEY_PANT_PRESENT, true);
        editor.putString(KEY_PANT_PATH, pantPath);
        editor.commit();
        return;
    }

    public static void setImageChoiceForToday(Context context) {
        List<String> shirts = getAllShirtPaths(context);
        List<String> pants = getAllPantPaths(context);
        if (shirts.size() == 0 && pants.size() == 0) {
            return;
        }
        String shirtPath = "";
        String pantPath = "";
        SharedPreferences sharedPreferences = getSharedPrefsObject(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (shirts.size() > 0) {
            Integer randomNumber = randomNumber(0, shirts.size() - 1);
            shirtPath = shirts.get(randomNumber);
            editor.putBoolean(KEY_CHOICE_SET, true);
            editor.putBoolean(KEY_SHIRT_PRESENT, true);
            editor.putString(KEY_SHIRT_PATH, shirtPath);
        }
        if (pants.size() > 0) {
            Integer randomNumber = randomNumber(0, pants.size() - 1);
            pantPath = pants.get(randomNumber);
            editor.putBoolean(KEY_CHOICE_SET, true);
            editor.putBoolean(KEY_PANT_PRESENT, true);
            editor.putString(KEY_PANT_PATH, pantPath);
        }
        Boolean bookmarked;
        if (!shirtPath.equals("") && !pantPath.equals("")) {
            Bookmark bookmark = new Bookmark(shirtPath, pantPath);
            BookmarkDbHelper bookmarkDbHelper = new BookmarkDbHelper(context);
            Integer id = bookmarkDbHelper.getIdOfBookmark(bookmark);
            bookmarked = id > 0 ? true : false;
        } else {
            bookmarked = false;
        }
        editor.putBoolean(KEY_BOOKMARKED, bookmarked);
        editor.commit();
    }

    private static Integer randomNumber(Integer minimum, Integer maximum) {
        Random random = new Random();
        Integer integer = random.nextInt(maximum - minimum + 1) + minimum;
        return integer;
    }

    public static void setAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_FOR_ALARM_PENDING_INTENT);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1234, intent, 0);
        // Set the alarm to start at approximately 1:00 AM next day
        Calendar calendar = Calendar.getInstance();
        // set to tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        // set time for tomorrow
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
        return;
    }

    public static Boolean isAlarmSet(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_FOR_ALARM_PENDING_INTENT);
        Boolean isSet = (PendingIntent.getBroadcast(context, 1234, intent, PendingIntent.FLAG_NO_CREATE) != null);
        return isSet;
    }

}