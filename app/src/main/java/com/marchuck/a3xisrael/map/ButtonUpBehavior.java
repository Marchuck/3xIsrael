package com.marchuck.a3xisrael.map;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.marchuck.a3xisrael.R;


/**
 * Created by Lukasz Marczak on 03.02.17.
 */
public class ButtonUpBehavior extends CoordinatorLayout.Behavior<View> {


    public static final String TAG = ButtonUpBehavior.class.getSimpleName();

    public ButtonUpBehavior() {
    }

    public ButtonUpBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout && isChildValid(child);
    }

    private boolean isChildValid(View child) {
        @IdRes int childId = child.getId();
        return   childId == R.id.bottom_sheet_map_button;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}