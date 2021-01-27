package com.parkch.myapplication;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, ActionMode.Callback {
    ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.textview);
        text.setOnLongClickListener(this);
    }

    // startActionMode()을 호출하여서 액션 모드가 생성될 때 호출된다.
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // 메뉴 리소스를 팽창한다.
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    // onCreateActionMode()가 호출된 직후에 호출된다.
    // 여러 번 호출될 수도 있다.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // false을 반환하면 아무것도 하지 않은 것이다.
    }

    // 사용자가 컨텍스트 메뉴 항목을 선택하면 호출된다.
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                mode.finish(); // 액션이 선택되면 컨텍스트 액션 모드를 닫는다.
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

