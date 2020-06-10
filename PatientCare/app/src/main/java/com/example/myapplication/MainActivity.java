package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDB;

    EditText EditTID;
    EditText EditTPW;

    Button LoginBtn;
    Button SignUpBtn;

    public static final int sub = 1001;
    boolean Eusr, HiUser, loginState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sqLiteDB = init_database();
        init_tables();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditTID = (EditText)findViewById(R.id.LogId);
        EditTPW = (EditText)findViewById(R.id.LogPW);
        LoginBtn = (Button)findViewById(R.id.LoginBtn);

        Intent intent = getIntent();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) { }
            else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1); } }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sqLiteDB != null) {
                    if (EditTID.getText().toString().equals("") || EditTPW.toString().equals(""))
                        Toast.makeText(getApplicationContext(), "빈칸을 입력 해주세요.", Toast.LENGTH_SHORT).show();
                    String sqlQueryTable = "SELECT ID, PW FROM DOCTOR_INFO";

                    Cursor cursor = null;
                    cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

                    int cnt = cursor.getCount();
                    if ( cnt == 0 ) Toast.makeText(getApplicationContext(),"DB에 데이터가없습니다. 회원가입을 해주세요.",Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < cnt; i++) {
                        if (cursor.moveToNext()) {
                            String id = cursor.getString(0);
                            String pw = cursor.getString(1);

                            if (EditTID.getText().toString().equals(id) &&
                                    EditTPW.getText().toString().equals(pw)) {
                                save_values(id);
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                //Toast.makeText(getApplicationContext(), cursor.getString(2) + "님 반갑습니다.", Toast.LENGTH_SHORT).show();
                                startActivityForResult(intent, sub);
                                finish();
                            }
                            if (EditTID.getText().toString().equals(id) &&
                                    !EditTPW.getText().toString().equals(pw)) {
                                Toast.makeText(getApplicationContext(), "비밀번호를 확인 해주세요.", Toast.LENGTH_SHORT).show();
                            }

                            if (!EditTID.getText().toString().equals(id) &&
                                    !EditTPW.getText().toString().equals(pw))
                                cursor.moveToNext();
                        }
                        else Toast.makeText(getApplicationContext(), "일치하는 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        SignUpBtn = (Button)findViewById(R.id.SUBtn);

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivityForResult(intent,sub);
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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

    private void init_tables() {
        if (sqLiteDB != null) {
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS DOCTOR_INFO (" +
                    "ID " + "TEXT PRIMARY KEY," +
                    "PW " + "TEXT, " +
                    "NAME " + "TEXT, " +
                    "LICENSE_NUM " + "TEXT, " +
                    "H_NAME " + "TEXT, " +
                    "DEPARTMENT " + "TEXT, " +
                    "ADDRESS " + "TEXT, " +
                    "LOGINSTATE " + "INTEGER" + ")";

            System.out.println(sqlCreateTable);
            sqLiteDB.execSQL(sqlCreateTable);
        }
    }

    private void save_values(String DB_value) {
        if (sqLiteDB != null) {
            String SQLinsert = "UPDATE DOCTOR_INFO SET LOGINSTATE = 1 WHERE ID = '" + DB_value + "'";
            //Toast.makeText(getApplicationContext(),SQLinsert.toString(),Toast.LENGTH_LONG).show();
            sqLiteDB.execSQL(SQLinsert);
        }
    }
}

