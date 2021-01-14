package com.parkch.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView browser = (WebView) findViewById(R.id.WebView);
        browser.loadUrl("https://www.naver.com/");

        //browser.getSettings().setJavaScriptEnabled(true); //자바 스크립트 활성화
    }
}