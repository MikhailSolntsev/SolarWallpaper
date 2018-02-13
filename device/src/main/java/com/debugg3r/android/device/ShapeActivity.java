package com.debugg3r.android.device;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;

public class ShapeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Drawable drawable = getDrawable(R.drawable.example1);

        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        gradientDrawable.setColor(ContextCompat.getColor(this, R.color.ColorExample));
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(12, Color.CYAN);
    }
}
