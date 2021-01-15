package com.parkch.myapplication8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.apple) {
            Toast.makeText(getApplicationContext(),"사과",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.grape) {
            Toast.makeText(getApplicationContext(),"포도",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.banana) {
            Toast.makeText(getApplicationContext(),"바나나",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }
}