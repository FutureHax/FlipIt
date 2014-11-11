package com.t3hh4xx0r.flipit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.t3hh4xx0r.flipit.R;

/**
 * Created by FutureHax on 11/11/14.
 */
public class FlipNotifierView extends FrameLayout {

    public FlipNotifierView(Context context) {
        super(context);
        inflate();
    }

    public FlipNotifierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
    }

    public FlipNotifierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate();
    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.flip_view, this);
    }

    public void startRotate() {
        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        findViewById(R.id.imageView).startAnimation(rotation);
    }
}
