package com.debugg3r.android.solarwallpaper.view.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.model.SharedPreferencesHelper;

import java.util.List;

public class ImageListAdapter extends ArrayAdapter<CharSequence> {

    private int[] resourseIds;
    private String[] titles;
    private String[] values;
    private int index;

    public ImageListAdapter(@NonNull Context context, @LayoutRes int resource,
                            @NonNull CharSequence[] entries, int[] resourceIds, int index) {
        super(context, resource, entries);

        this.titles = context.getResources().getStringArray(R.array.pref_image_type_titles);
        this.values = context.getResources().getStringArray(R.array.pref_image_type_values);
        this.resourseIds = resourceIds;
        this.index = index;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.image_list_preference_item, parent, false);

        ImageView image = (ImageView) view.findViewById(R.id.pref_image_view);
        CheckedTextView button = (CheckedTextView) view.findViewById(R.id.pref_checked_text);

        image.setImageResource(resourseIds[position]);

        button.setText(titles[position]);
        if (position == index) {
            button.setChecked(true);
        }

//        view.setTag(position);
//        view.setOnClickListener(itemView -> {
//            SharedPreferencesHelper helper = new SharedPreferencesHelper(getContext());
//            helper.putString(getContext().getString(R.string.pref_image_type),
//                    values[(Integer)itemView.getTag()]);
//            ((Activity) getContext()).finish();
//        });
        return view;
    }
}
