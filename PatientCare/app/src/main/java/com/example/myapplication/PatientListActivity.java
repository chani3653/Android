package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity {

    ArrayList<PatientListData> Data;
    SQLiteDatabase sqLiteDB;

    Boolean DBstat = false;

    public static final int sub = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patientcare);

        sqLiteDB = init_patient_database();

        InitPatientList();

        ListView listView = (ListView) findViewById(R.id.PatientList);
        final PatientListAdapter patientListAdapter = new PatientListAdapter(this, Data);

        listView.setAdapter(patientListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = null;
                cursor = sqLiteDB.rawQuery("SELECT * FROM PATIENT_INFO", null);
                cursor.moveToPosition(position);
                String PNum = cursor.getString(8);
                //Toast.makeText(getApplicationContext(),PNum,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), PatientCareEditActivity.class);
                intent.putExtra("DBindex", PNum); // DB에서 찾는 방향으로 수정
                startActivityForResult(intent, sub);

                //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitPatientList() {
        Data = new ArrayList<PatientListData>();

        if (sqLiteDB != null) {
            Cursor cursor = null;
            cursor = sqLiteDB.rawQuery("SELECT * FROM PATIENT_INFO", null);

            int cnt = cursor.getCount();

            for (int i = 0; i < cnt; i++) {
                if (cursor.moveToNext()) {
                    Data.add(new PatientListData(
                            "Risk Score : " + cursor.getString(28),
                            "Patient Name : " + cursor.getString(7),
                            "Patient Number : " + cursor.getString(8),
                            "Clician Name : " + cursor.getString(1),
                            "Examination Date : " + cursor.getString(4)));
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "데이터베이스가 없습니다. ", Toast.LENGTH_SHORT).show();
        }
        // DB 없을 때 오류 띄우는거 찾기
    }

    private SQLiteDatabase init_patient_database() {
        SQLiteDatabase db = null;
        File file = new File(getFilesDir(), "Patient_Info.db");

        try {
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(),"PATH : " + file.toString(),Toast.LENGTH_LONG).show();

        if (db == null)
            Toast.makeText(getApplicationContext(), "DB Creation failed. " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        return db;
    }
}
