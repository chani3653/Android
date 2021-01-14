package com.parkch.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;

public class VolumeControlView extends AppCompatImageView implements View.OnTouchListener {

    private double angle = 0.0;
    private KnobListener listener;
    float x, y;
    float mx, my;

    // 추상 메소드 정의
    public interface KnobListener{
        public void onChanged(double angle);
    }

    // 노브 리스너
    public void setKnobListener(KnobListener lis){
        listener = lis;
    }

    // 생성자
    public VolumeControlView(Context context){
        super(context);
        this.setImageResource(R.drawable.knob);
        this.setOnTouchListener(this);
    }

    // 생성자
    public VolumeControlView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setImageResource(R.drawable.knob);
        this.setOnTouchListener(this);
    }

    // 각도 가져옴
    private double getAngle(float x, float y){
        mx = x - (getWidth() / 2.0f);
        my = (getHeight() / 2.0f) - y;

        // atan2() : 탄젠트를 적용했을 때 지정된 두 숫자의 몫이 나오는 각도를 반환
        double degree = Math.atan2(mx, my) * 180.0 / 3.141592;
        return degree;
    }

    // 터치 이벤트
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX(0);
        y = event.getY(0);
        angle = getAngle(x, y);
        invalidate();
        listener.onChanged(angle);  // 레이팅바 값 변경

        return true;
    }

    // 노브 이미지 각도 회전
    protected  void onDraw(Canvas c){
        Paint paint = new Paint();
        c.rotate((float) angle, getWidth() / 2, getHeight() / 2);
        super.onDraw(c);
    }
}
