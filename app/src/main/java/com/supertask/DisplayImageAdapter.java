package com.supertask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by pratik on 18/10/15.
 */
public class DisplayImageAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> pathOfImageFiles;
    private BitmapFactory.Options options;

    public DisplayImageAdapter(Context context, List<String> pathOfImageFiles) {
        super(context, R.layout.display_image_item, pathOfImageFiles);
        this.context = context;
        this.pathOfImageFiles = pathOfImageFiles;
        options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.display_image_item, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        String string = pathOfImageFiles.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(string, options);
        holder.imageView.setImageBitmap(bitmap);
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}