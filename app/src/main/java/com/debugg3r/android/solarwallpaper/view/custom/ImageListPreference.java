package com.debugg3r.android.solarwallpaper.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.debugg3r.android.solarwallpaper.R;


public class ImageListPreference extends ListPreference {
    private int[] resoursesIds = null;
    private int mClickedDialogEntryIndex;

    public ImageListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageListPreference);

        String imageNames[] = context.getResources().getStringArray(
                typedArray.getResourceId(typedArray.getIndexCount() - 1, -1));

        resoursesIds = new int[imageNames.length];

        for (int i = 0; i < imageNames.length; i++) {
            String imageName = imageNames[i]
                    .substring(imageNames[i].indexOf("/") + 1, imageNames[i].lastIndexOf("."));

            resoursesIds[i] = context.getResources().getIdentifier(imageName, null, context.getPackageName());
        }

        typedArray.recycle();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        int index = findIndexOfValue(getSharedPreferences().getString(getKey(), "1"));

        ListAdapter adapter = new ImageListAdapter(getContext(), R.layout.image_list_preference_item,
                getEntries(), resoursesIds, index);

        builder.setAdapter(adapter, this);

        mClickedDialogEntryIndex = index;
        builder.setSingleChoiceItems(getEntries(), mClickedDialogEntryIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClickedDialogEntryIndex = which;

                        /*
                         * Clicking on an item simulates the positive button
                         * click, and dismisses the dialog.
                         */
                        Toast.makeText(getContext(), "Item selected = " + which, Toast.LENGTH_LONG);

                        ImageListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });

        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        CharSequence[] mEntryValues = getEntries();
        if (positiveResult && mClickedDialogEntryIndex >= 0 && mEntryValues != null) {
            String value = mEntryValues[mClickedDialogEntryIndex].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }
}
