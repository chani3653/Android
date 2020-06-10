package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import java.io.File;

public class EditUserActivity extends Activity {

    SQLiteDatabase sqLiteDB;

    public static final int sub = 1001;

    EditText EditID;
    EditText EditPW;
    EditText EditNAME;
    EditText EditLIC;
    EditText EditHOS;
    EditText EditDEP;
    EditText EditADD;

    Button Finish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);

        EditID = (EditText) findViewById(R.id.EditID);
        EditPW = (EditText) findViewById(R.id.EditPW);
        EditNAME = (EditText) findViewById(R.id.EditName);
        EditLIC = (EditText) findViewById(R.id.ELicenceNum);
        EditHOS = (EditText) findViewById(R.id.EHospital);
        EditDEP = (EditText) findViewById(R.id.EDepartment);
        EditADD = (EditText) findViewById(R.id.EAdress);

        sqLiteDB = init_database();

        load_values();

        Finish = (Button) findViewById(R.id.EEndBtn);
        Finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EditID.getText().toString().equals("") || EditPW.getText().toString().equals("") || EditLIC.getText().toString().equals("")
                        || EditHOS.getText().toString().equals("") || EditDEP.getText().toString().equals("") || EditADD.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "빈칸을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                if (!EditID.getText().toString().equals("") && !EditPW.getText().toString().equals("") && !EditLIC.getText().toString().equals("")
                        && !EditHOS.getText().toString().equals("") && !EditDEP.getText().toString().equals("") && !EditADD.getText().toString().equals("")) {

                    update_values(EditID.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //intent.putExtra("exit", 1);
                    Toast.makeText(getApplicationContext(), "새로운 정보로 로그인 해주세요", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, sub);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"내용을 저장 하지 않습니다.",  Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivityForResult(intent,sub);
        finish();
    }

    private SQLiteDatabase init_database() {
        SQLiteDatabase db = null;
        File file = new File(getFilesDir(),"Doctor_Info.db");

        Toast.makeText(getApplicationContext(),"PATH : " + file.toString(),Toast.LENGTH_LONG).show();
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (db == null) System.out.println("DB Creation failed. " + file.getAbsolutePath());
        return db;
    }

    private void load_values() {
        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM DOCTOR_INFO WHERE LOGINSTATE = '1'";
            Cursor cursor = null;

            cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

            if(cursor.moveToNext()) {
                String id = cursor.getString(0);
                EditID.setText(id);

                String pw = cursor.getString(1);
                EditPW.setText(pw);

                String name = cursor.getString(2);
                EditNAME.setText(name);

                String regi = cursor.getString(3);
                EditLIC.setText(regi);

                String hos = cursor.getString(4);
                EditHOS.setText(hos);

                String depart = cursor.getString(5);
                EditDEP.setText(depart);

                String addr = cursor.getString(6);
                EditADD.setText(addr);
            }
        }
    }

    private void update_values(String DB_value) {
        if (sqLiteDB != null) {
            String SQLinsert = "UPDATE DOCTOR_INFO SET " +
                    "PW = '" + EditPW.getText().toString()           + "', " +
                    "NAME = '" + EditNAME.getText().toString()       + "', " +
                    "LICENSE_NUM = '" + EditLIC.getText().toString() + "', " +
                    "H_NAME = '" + EditHOS.getText().toString()      + "', " +
                    "DEPARTMENT = '" + EditDEP.getText().toString()  + "', " +
                    "ADDRESS = '" + EditADD.getText().toString()     + "' "  +
                    "WHERE ID = '" + DB_value + "'";
            sqLiteDB.execSQL(SQLinsert);
        }
    }
    private void save_values(String DB_value) {
        if (sqLiteDB != null) {
            String SQLinsert = "UPDATE DOCTOR_INFO SET LOGINSTATE = 0 WHERE ID = '" + DB_value + "'";
            sqLiteDB.execSQL(SQLinsert);
        }
    }
}
