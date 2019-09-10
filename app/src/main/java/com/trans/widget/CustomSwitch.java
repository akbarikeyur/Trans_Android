package com.trans.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;

/**
 * Created by Assure on 10-08-2018.
 */

public class CustomSwitch extends Switch {

    public CustomSwitch(Context context) {
        super(context);
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        changeColor(checked);
    }

    private void changeColor(boolean isChecked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int thumbColor;
            int trackColor;

            if (isChecked) {
                thumbColor = Color.argb(255, 21, 142, 37);
                trackColor = thumbColor;
            } else {
                thumbColor = Color.argb(255, 255, 0, 0);
                trackColor = Color.argb(235, 255, 0, 0);
            }

            try {
                getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}