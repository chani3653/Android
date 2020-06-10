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

import java.io.File;

public class RegisterActivity extends Activity {

    SQLiteDatabase sqLiteDB;

    EditText RID;
    EditText RPW;
    EditText RName;
    EditText RLegNum;
    EditText RHos;
    EditText RDep;
    EditText RAdd;

    Button RegBtn;

    int NumVal;

    public static final int sub = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sqLiteDB = init_database();
        init_tables();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RID = (EditText)findViewById(R.id.RegID);
        RPW = (EditText)findViewById(R.id.RegPW);
        RName = (EditText)findViewById(R.id.RegName);
        RLegNum =(EditText)findViewById(R.id.LicenceNum);
        RHos = (EditText)findViewById(R.id.Hospital);
        RDep = (EditText)findViewById(R.id.Department);
        RAdd = (EditText)findViewById(R.id.Adress);

        RegBtn = (Button)findViewById(R.id.RegBtn);
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RID.getText().toString().equals("") || RPW.getText().toString().equals("") || RName.getText().toString().equals("")
                        || RHos.getText().toString().equals("") || RDep.getText().toString().equals("") || RAdd.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "빈칸을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                if (!RID.getText().toString().equals("") && !RPW.getText().toString().equals("") && !RName.getText().toString().equals("")
                        && !RHos.getText().toString().equals("") && !RDep.getText().toString().equals("") && !RAdd.getText().toString().equals("")) {

                    save_values();

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivityForResult(intent,sub);
                    finish();
                }
            }
        });
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

    private void save_values() {
        if (sqLiteDB != null) {
            String SQLinsert = "INSERT INTO DOCTOR_INFO " +
                    "(ID, PW, NAME, LICENSE_NUM, H_NAME, DEPARTMENT, ADDRESS, LOGINSTATE) VALUES (" +
                    "'" + RID.getText().toString() + "'," +
                    "'" + RPW.getText().toString() + "'," +
                    "'" + RName.getText().toString()+ "'," +
                    "'" + RLegNum.getText().toString()+ "'," +
                    "'" + RHos.getText().toString()+ "'," +
                    "'" + RDep.getText().toString()+ "'," +
                    "'" + RAdd.getText().toString()+ "'," +
                    " '0')";

            //Toast.makeText(getApplicationContext(),SQLinsert.toString(),Toast.LENGTH_SHORT).show();
            sqLiteDB.execSQL(SQLinsert);
        }
    }
}
