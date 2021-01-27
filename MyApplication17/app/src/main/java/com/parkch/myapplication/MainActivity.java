package com.parkch.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {

    ImageView iv_smile;
    float previous_x = 0, previous_y = 0;
    Vibrator mVibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        iv_smile = (ImageView)findViewById(R.id.smile);
        mVibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case  MotionEvent.ACTION_MOVE:
                int touch_X = (int)event.getX() - 200;
                int touch_Y = (int)event.getY() - 200;

                ObjectAnimator smileX = ObjectAnimator.ofFloat(iv_smile, "translationX", previous_x, touch_X);
                smileX.start();
                ObjectAnimator smileY = ObjectAnimator.ofFloat(iv_smile, "translationY", previous_y, touch_Y);
                smileY.start();
                mVibe.vibrate(50);

                previous_x = touch_X;
                previous_y = touch_Y;
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}