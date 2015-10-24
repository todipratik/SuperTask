package com.supertask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.supertask.Bookmark;
import com.supertask.BookmarkDbHelper;
import com.supertask.R;
import com.supertask.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pratik on 18/10/15.
 */
public class TodayChoiceFragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO_SHIRT = 1;
    static final int RESULT_LOAD_SHIRT_IMAGE = 2;
    static final int RESULT_LOAD_PANT_IMAGE = 3;
    static final int REQUEST_TAKE_PHOTO_PANT = 4;

    private static final String TAG = "TodayChoiceFragment";

    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton addShirtFromGallery;
    private FloatingActionButton addPantFromGallery;
    private FloatingActionButton clickImageOfShirt;
    private FloatingActionButton clickImageOfPant;
    private SharedPreferences sharedPreferences;
    private TextView todayShirtTextView;
    private TextView todayPantTextView;
    private TextView todayChoiceMessageTextView;
    private ImageView todayShirtImageView;
    private ImageView todayPantImageView;
    private ImageView bookmark;
    private ImageView dislike;
    private ImageView share;
    private ImageLoader imageLoader;

    private String shirtPath;
    private String pantPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_choice, container, false);
        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.add_image);
        addShirtFromGallery = (FloatingActionButton) view.findViewById(R.id.add_shirt_from_gallery);
        addPantFromGallery = (FloatingActionButton) view.findViewById(R.id.add_pant_from_gallery);
        clickImageOfShirt = (FloatingActionButton) view.findViewById(R.id.click_photo_shirt);
        clickImageOfPant = (FloatingActionButton) view.findViewById(R.id.click_photo_pant);
        todayChoiceMessageTextView = (TextView) view.findViewById(R.id.today_choice_text_view);
        todayShirtTextView = (TextView) view.findViewById(R.id.today_shirt_text_view);
        todayPantTextView = (TextView) view.findViewById(R.id.today_pant_text_view);
        todayShirtImageView = (ImageView) view.findViewById(R.id.today_shirt_image_view);
        todayPantImageView = (ImageView) view.findViewById(R.id.today_pant_image_view);
        bookmark = (ImageView) view.findViewById(R.id.bookmark);
        dislike = (ImageView) view.findViewById(R.id.dislike);
        share = (ImageView) view.findViewById(R.id.share);

        addShirtFromGallery.setOnClickListener(this);
        addPantFromGallery.setOnClickListener(this);
        clickImageOfShirt.setOnClickListener(this);
        clickImageOfPant.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        dislike.setOnClickListener(this);
        share.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = Util.getSharedPrefsObject(getActivity());
        if (sharedPreferences.contains(Util.KEY_CHOICE_SET)) {
            // keys exists
            if (sharedPreferences.getBoolean(Util.KEY_CHOICE_SET, false)) {
                displayImageChoiceForToday();
            }

        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_shirt_from_gallery:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_SHIRT_IMAGE);
                floatingActionsMenu.collapse();
                break;
            case R.id.add_pant_from_gallery:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_PANT_IMAGE);
                floatingActionsMenu.collapse();
                break;
            case R.id.click_photo_shirt:
                dispatchTakePictureIntent(Util.getFileNameForShirt(), REQUEST_TAKE_PHOTO_SHIRT);
                floatingActionsMenu.collapse();
                break;
            case R.id.click_photo_pant:
                dispatchTakePictureIntent(Util.getFileNameForPant(), REQUEST_TAKE_PHOTO_PANT);
                floatingActionsMenu.collapse();
                break;
            case R.id.bookmark:
                if (sharedPreferences == null)
                    sharedPreferences = Util.getSharedPrefsObject(getActivity());
                Boolean bookmarked = sharedPreferences.getBoolean(Util.KEY_BOOKMARKED, false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                BookmarkDbHelper bookmarkDbHelper = new BookmarkDbHelper(getActivity());
                if (bookmarked) {
                    // already bookmarked
                    // remove bookmark from database and update in shared prefs
                    Bookmark b = new Bookmark(shirtPath, pantPath);
                    bookmarkDbHelper.deleteBookmark(b);
                    editor.putBoolean(Util.KEY_BOOKMARKED, false);
                    bookmark.setBackgroundResource(android.R.color.transparent);
                } else {
                    // bookmark it, i.e. update in shared prefs and database
                    Bookmark b = new Bookmark(shirtPath, pantPath);
                    bookmarkDbHelper.insertBookmark(b);
                    editor.putBoolean(Util.KEY_BOOKMARKED, true);
                    bookmark.setBackgroundResource(R.color.yellow);
                }
                editor.commit();
                break;
            case R.id.dislike:
                bookmark.setBackgroundResource(android.R.color.transparent);
                Util.setImageChoiceForToday(getActivity());
                displayImageChoiceForToday();
                break;
            case R.id.share:
                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                imageUris.add(Uri.parse("file://" + shirtPath));
                imageUris.add(Uri.parse("file://" + pantPath));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share today's choice with..."));
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_TAKE_PHOTO_SHIRT || requestCode == REQUEST_TAKE_PHOTO_PANT) && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "Image saved successfully", Toast.LENGTH_LONG).show();
        } else if ((requestCode == RESULT_LOAD_SHIRT_IMAGE || requestCode == RESULT_LOAD_PANT_IMAGE) && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            // picturePath is the URI of the image chosen
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                File f = new File(picturePath);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));

                String imageFileName = "";
                if (requestCode == RESULT_LOAD_SHIRT_IMAGE) {
                    imageFileName = Util.getFileNameForShirt();
                } else if (requestCode == RESULT_LOAD_PANT_IMAGE) {
                    imageFileName = Util.getFileNameForPant();
                }
                File storageDir = Util.getPathToStorage(getActivity());
                File file = new File(storageDir, imageFileName + ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(getActivity(), "Image imported successfully", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if the result was OK, set the alarm
        if (resultCode == Activity.RESULT_OK) {
            Util.setAlarm(getActivity());
            Boolean shirtIsToBeSet = !sharedPreferences.getBoolean(Util.KEY_SHIRT_PRESENT, false) &&
                    (requestCode == REQUEST_TAKE_PHOTO_SHIRT || requestCode == RESULT_LOAD_SHIRT_IMAGE);
            Boolean pantIsToBeSet = !sharedPreferences.getBoolean(Util.KEY_PANT_PRESENT, false) &&
                    (requestCode == REQUEST_TAKE_PHOTO_PANT || requestCode == RESULT_LOAD_PANT_IMAGE);
            if (sharedPreferences == null)
                Util.getSharedPrefsObject(getActivity());
            if (!sharedPreferences.getBoolean(Util.KEY_CHOICE_SET, false)) {
                Util.setImageChoiceForToday(getActivity());
                displayImageChoiceForToday();
            } else if (shirtIsToBeSet) {
                Util.setShirtForToday(getActivity());
                displayImageChoiceForToday();
            } else if (pantIsToBeSet) {
                Util.setPantForToday(getActivity());
                displayImageChoiceForToday();
            }
        }
    }

    private void dispatchTakePictureIntent(String imageFileName, Integer requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(imageFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createImageFile(String imageFileName) throws IOException {
        File storageDir = Util.getPathToStorage(getActivity());
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private void displayImageChoiceForToday() {
        todayChoiceMessageTextView.setText(R.string.today_choice_message);
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        imageLoader.init(config);
        shirtPath = "";
        pantPath = "";
        if (sharedPreferences.getBoolean(Util.KEY_SHIRT_PRESENT, false)) {
            shirtPath = sharedPreferences.getString(Util.KEY_SHIRT_PATH, "");
            Log.d(TAG, shirtPath);
            todayShirtTextView.setVisibility(View.INVISIBLE);
            todayShirtImageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage("file://" + shirtPath, todayShirtImageView);
        } else {
            todayShirtImageView.setVisibility(View.INVISIBLE);
            bookmark.setVisibility(View.GONE);
            todayShirtTextView.setText(R.string.shirt_not_added_message);
            todayShirtTextView.setVisibility(View.VISIBLE);
        }
        if (sharedPreferences.getBoolean(Util.KEY_PANT_PRESENT, false)) {
            pantPath = sharedPreferences.getString(Util.KEY_PANT_PATH, "");
            Log.d(TAG, pantPath);
            todayPantTextView.setVisibility(View.INVISIBLE);
            todayPantImageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage("file://" + pantPath, todayPantImageView);
        } else {
            todayPantImageView.setVisibility(View.INVISIBLE);
            bookmark.setVisibility(View.GONE);
            todayPantTextView.setText(R.string.pant_not_added_message);
            todayPantTextView.setVisibility(View.VISIBLE);
        }
        if (!shirtPath.equals("") && !pantPath.equals("")) {
            bookmark.setVisibility(View.VISIBLE);
            dislike.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            if (sharedPreferences.getBoolean(Util.KEY_BOOKMARKED, false)) {
                bookmark.setBackgroundResource(R.color.yellow);
            }
        }
        return;
    }
}