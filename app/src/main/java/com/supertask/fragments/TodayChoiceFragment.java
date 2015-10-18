package com.supertask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.supertask.R;
import com.supertask.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pratik on 18/10/15.
 */
public class TodayChoiceFragment extends Fragment implements View.OnClickListener {

    TextView textView;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int RESULT_LOAD_SHIRT_IMAGE = 2;
    static final int RESULT_LOAD_PANT_IMAGE = 3;

    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton addShirtFromGallery;
    private FloatingActionButton addPantFromGallery;
    private FloatingActionButton clickImageOfShirt;
    private FloatingActionButton clickImageOfPant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_choice, container, false);
        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.add_image);
        addShirtFromGallery = (FloatingActionButton) view.findViewById(R.id.add_shirt_from_gallery);
        addPantFromGallery = (FloatingActionButton) view.findViewById(R.id.add_pant_from_gallery);
        clickImageOfShirt = (FloatingActionButton) view.findViewById(R.id.click_photo_shirt);
        clickImageOfPant = (FloatingActionButton) view.findViewById(R.id.click_photo_pant);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("Hey This is your choice for today!!!!");

        addShirtFromGallery.setOnClickListener(this);
        addPantFromGallery.setOnClickListener(this);
        clickImageOfShirt.setOnClickListener(this);
        clickImageOfPant.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_shirt_from_gallery:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_SHIRT_IMAGE);
                break;
            case R.id.add_pant_from_gallery:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_PANT_IMAGE);
                break;
            case R.id.click_photo_shirt:
                dispatchTakePictureIntent(Util.getFileNameForShirt());
                break;
            case R.id.click_photo_pant:
                dispatchTakePictureIntent(Util.getFileNameForPant());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
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
    }

    private void dispatchTakePictureIntent(String imageFileName) {
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
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile(String imageFileName) throws IOException {
        File storageDir = Util.getPathToStorage(getActivity());
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }
}