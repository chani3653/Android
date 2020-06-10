package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MenuActivity extends Activity {

    SQLiteDatabase sqLiteDB;

    public static final int sub = 1001;
    int backPress = 0;

    String DB_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sqLiteDB = init_database();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView TextVUser;

        Button PCBtn, UserInfoBtn, PatientInfoBtn;

        TextVUser = (TextView) findViewById(R.id.hiuser);
        PCBtn = (Button)findViewById(R.id.PCABtn);
        UserInfoBtn = (Button)findViewById(R.id.UserInfoBtn);
        PatientInfoBtn = (Button)findViewById(R.id.PatientInfoBtn);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM DOCTOR_INFO WHERE LOGINSTATE = '1'";

            Cursor cursor = null;
            cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                DB_name = cursor.getString(2);
                TextVUser.setText(DB_name + "님");
            }
        }

        PCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 환자 정보 입력 전환 버튼
                Intent intent = new Intent(getApplicationContext(),PatientCareActivity.class);
                startActivityForResult(intent,sub);
            }
        });

        UserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 개인정보 수정 버튼
                Intent intent = new Intent(getApplicationContext(),EditUserActivity.class);
                startActivityForResult(intent,sub);
                finish();
            }
        });

        PatientInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PatientListActivity.class);
                startActivityForResult(intent,sub);
            }
        });
    }

    public void onBackPressed() {
        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM DOCTOR_INFO WHERE LOGINSTATE = '1'";

            Cursor cursor = null;
            cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

            if (cursor.moveToNext()) {

                //int DB_LoginState = cursor.getInt(7);

                if (backPress == 0)
                {
                    backPress++;
                    Toast.makeText(getApplicationContext(),"한번 더 뒤로가기를 누르면 로그아웃 합니다.",Toast.LENGTH_LONG).show();
                }

                else if(backPress == 1) {
                    backPress++;
                    String SQLinsert = "UPDATE DOCTOR_INFO SET LOGINSTATE = 0 "; // 전체 초기화 로 구성 되있음 실제 구현시 수정필요
                    sqLiteDB.execSQL(SQLinsert);
                    backPress = 0;
                    finish();
                }
            }
        }
    }

    private SQLiteDatabase init_database() {
        SQLiteDatabase db = null;
        File file = new File(getFilesDir(),"Doctor_Info.db");

        //Toast.makeText(getApplicationContext(),"PATH : " + file.toString(),Toast.LENGTH_LONG).show();
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (db == null) System.out.println("DB Creation failed. " + file.getAbsolutePath());
        return db;
    }
}
