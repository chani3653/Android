package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientCareEditActivity extends AppCompatActivity {

    ArrayAdapter adapter;

    Button FinishBtn, EDPickerBtn, BPickerBtn, BMICalBtn;

    DatePickerDialog Examination, DateOfBirth;

    Date currentTime = Calendar.getInstance().getTime();

    int sPosition;

    CheckBox Statin,HTTx, DMTx;

    EditText Clinician, LicenseNum, PatientNum, Tall, Weight, PatientName, SystolicBP, DiastolicBP,
            Total_C, Glucose, HDL, LDL, Tri, XYCR, HVAge, OptimalLisk, Comment;

    String Diagnostic, Examination_Date, Episode, CVRM, PatientBirth_Date, Sex, BMI, Diabetes, HxDM,
            FamilyHistory, Treatment, Smoking, image, RiskScore, DBindex;

    Spinner Smoking_Spinner;

    RadioGroup Diagostic_H_Radio, Episode_Radio, CVRM_Radio,Sex_Radio, Diabetes_Radio, HxDM_Radio, FamilyHistory_Radio,
            Treatment_Radio, Smoking_Radio, image_Radio;

    RadioButton Diagostic_H_Yes, Diagostic_H_No,
            Episode_Visit, Episode_Phone, Episode_Mail,
            CVRM_Case, CVRM_Chronic,
            Sex_Male, Sex_Female,
            Diabetes_None, Diabetes_Type1, Diabetes_Type2,
            HxDM_Yes, HxDM_No, HxDM_PreDM,
            FamilyHistory_CHD, FamilyHistory_Stroke,
            Treatment_YES, Treatmtent_No,
            Smoking_Yes, Smoking_No,
            image_CT, image_MRI;

    LinearLayout Treatment_Layout;

    SQLiteDatabase sqLiteDB;

    SimpleDateFormat Year, Month, Day;

    TextView PatientName_TV, HospitalName, DoctorName, ExaminationDate, PatientBirthDate, BMI_TV, Treatment_TV, Smoking_TV;

    public static final int sub = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sqLiteDB = init_patient_database();
        init_patient_tables();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientcare);

        Year = new SimpleDateFormat("yyyy", Locale.getDefault());
        Month = new SimpleDateFormat("MM", Locale.getDefault());
        Day = new SimpleDateFormat("dd", Locale.getDefault());

        final String sYear = Year.format(currentTime),  sMonth = Month.format(currentTime), sDay = Day.format(currentTime);

        Intent intent  = getIntent();
        DBindex = intent.getStringExtra("DBindex");

        //-----------------------------------------doctor part------------------------------------//
        PatientName_TV = (TextView) findViewById(R.id.patientName);
        DoctorName = (TextView)findViewById((R.id.DoctorName));
        Clinician = (EditText)findViewById(R.id.Clinician);
        LicenseNum = (EditText)findViewById(R.id.PC_LicenceNum);
        HospitalName = (TextView)findViewById(R.id.HosName);

        if (sqLiteDB != null) {
            Cursor cursor = null;
            cursor = sqLiteDB.rawQuery("SELECT * FROM PATIENT_INFO", null);

            if (cursor.moveToNext()) {
                PatientName_TV.setText(cursor.getString(7));
                DoctorName.setText(cursor.getString(1)); // 상단 의사이름
                Clinician.setText(cursor.getString(1)); // 내부 의사이름
                LicenseNum.setText(cursor.getString(2)); // 내부 의사 등록번호
                HospitalName.setText(cursor.getString(0)); // 상단 병원이름
            }
        }

        //--------------------------------------patient part--------------------------------------//

        //---Medical info---//

        //Diagnostic History //Examination Date
        Diagostic_H_Radio = (RadioGroup) findViewById(R.id.DiagnosticHistory);
        Diagostic_H_Yes = (RadioButton) findViewById(R.id.DiagnosticHistoryYes);
        Diagostic_H_No = (RadioButton) findViewById(R.id.DiagnosticHistoryNo);

        ExaminationDate = (TextView)findViewById(R.id.ExaminationDate);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(3).equals("Yes")) {
                    Diagostic_H_Yes.setChecked(true);
                    ExaminationDate.setVisibility(View.VISIBLE);
                    Diagnostic = "Yes";
                }
                if (cursor.getString(3).equals("No")) {
                    Diagostic_H_No.setChecked(true);
                    ExaminationDate.setVisibility(View.GONE);
                    Diagnostic = "no";
                }
            }
        }

        Diagostic_H_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.DiagnosticHistoryYes:
                        Diagnostic = "Yes";
                        ExaminationDate.setVisibility(View.VISIBLE);
                        EDPickerBtn.setVisibility(View.VISIBLE);
                        break;

                    case R.id.DiagnosticHistoryNo:
                        Diagnostic = "No";
                        ExaminationDate.setVisibility(View.GONE);
                        EDPickerBtn.setVisibility(View.GONE);
                        break;
                }
            }
        });

        EDPickerBtn = (Button)findViewById(R.id.EDDataPicker);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                ExaminationDate.setText("Examination : " + cursor.getString(4));
                Examination_Date = cursor.getString(4);
            }
        }

        EDPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Examination = new DatePickerDialog(PatientCareEditActivity.this, listner,
                        Integer.parseInt(sYear), Integer.parseInt(sMonth) - 1 , Integer.parseInt(sDay));
                Examination.show();
            }

            private DatePickerDialog.OnDateSetListener listner = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                    ExaminationDate.setText("Examination : " + year + "/" + (month + 1) + "/" + dayOfMonth);
                    Examination_Date = year + "/" + (month + 1) + "/" + dayOfMonth;
                }
            };
        });

        //Episode
        Episode_Radio = (RadioGroup) findViewById(R.id.Episode);
        Episode_Visit = (RadioButton) findViewById(R.id.EpisodeVisit);
        Episode_Phone = (RadioButton) findViewById(R.id.EpisodePhone);
        Episode_Mail = (RadioButton)findViewById(R.id.EpisodeMail);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(5).equals("Visit")) {
                    Episode_Visit.setChecked(true);
                    Episode = "Visit";
                }
                if (cursor.getString(5).equals("Phone")) {
                    Episode_Phone.setChecked(true);
                    Episode = "Phone";
                }
                if (cursor.getString(5).equals("Mail")) {
                    Episode_Mail.setChecked(true);
                    Episode = "Mail";
                }
            }
        }

        Episode_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.EpisodeVisit:
                        Episode = "Visit";
                        break;

                    case R.id.EpisodePhone:
                        Episode = "Phone";
                        break;

                    case R.id.EpisodeMail:
                        Episode = "Mail";
                        break;
                }
            }
        });

        // CVRM TYPE
        CVRM_Radio = (RadioGroup) findViewById(R.id.CVRM);
        CVRM_Case = (RadioButton) findViewById(R.id.CVRMCase);
        CVRM_Chronic = (RadioButton) findViewById(R.id.CVRMCronic);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(6).equals("Case")) {
                    CVRM_Case.setChecked(true);
                    CVRM = "Case";
                }
                if (cursor.getString(6).equals("Chronic")) {
                    CVRM_Chronic.setChecked(true);
                    CVRM = "Chronic";
                }
            }
        }

        CVRM_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.CVRMCase:
                        CVRM = "Case";
                        break;

                    case R.id.CVRMCronic:
                        CVRM = "Chronic";
                        break;
                }
            }
        });

        //Patient Name
        PatientName = (EditText) findViewById(R.id.EditpatientName);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                PatientName.setText(cursor.getString(7));
            }
        }

        //Patient Number
        PatientNum = (EditText) findViewById(R.id.patientNumber);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                PatientNum.setText(cursor.getString(8));
            }
        }

        //Patient Sex
        Sex_Radio = (RadioGroup) findViewById(R.id.Sex);
        Sex_Male = (RadioButton) findViewById(R.id.SexMale);
        Sex_Female = (RadioButton) findViewById(R.id.SexFemale);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(9).equals("Male")) {
                    Sex_Male.setChecked(true);
                    Sex = "Male";
                }
                if (cursor.getString(9).equals("Female")) {
                    Sex_Female.setChecked(true);
                    Sex = "Female";
                }
            }
        }

        Sex_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.SexMale:
                        Sex = "Male";
                        break;

                    case R.id.SexFemale:
                        Sex = "Female";
                        break;
                }
            }
        });

        //Date of birth
        PatientBirthDate = (TextView)findViewById(R.id.patientBirth);

        BPickerBtn = (Button)findViewById(R.id.PBDatePickerBtn);
        BPickerBtn.setText("Edit Birth");
        BPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateOfBirth = new DatePickerDialog(PatientCareEditActivity.this, listner,
                        Integer.parseInt(sYear), Integer.parseInt(sMonth) - 1 , Integer.parseInt(sDay));
                DateOfBirth.show();
            }

            private DatePickerDialog.OnDateSetListener listner = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                    int man = Integer.parseInt(sYear) - year;
                    if (year * 100 + dayOfMonth > month * 100 + Integer.parseInt(sDay)) man--;
                    if (man <= -1) man = 0;

                    PatientBirthDate.setText("Date of birth : " + year + "/" + (month + 1) + "/" + dayOfMonth + " ( 만 "+ man + "세 )");
                    PatientBirth_Date = year + "/" + (month + 1) + "/" + dayOfMonth + " ( 만 "+ man + "세 )";
                }
            };
        }); // 저장된 일자를 불러 오도록 수정 필요

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                PatientBirthDate.setText("Date of birth : " + cursor.getString(10));
                PatientBirth_Date = cursor.getString(10);
            }
        }

        // Tall
        Tall = (EditText)findViewById(R.id.Tall);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                Tall.setText(cursor.getString(11));
            }
        }

        // Weight
        Weight = (EditText)findViewById(R.id.Weight);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                Weight.setText(cursor.getString(12));
            }
        }

        //BMI
        BMICalBtn = (Button)findViewById(R.id.BMIButton);
        BMI_TV = (TextView)findViewById(R.id.BMI);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                BMI_TV.setText(cursor.getString(13));
                BMI = cursor.getString(13);
            }
        }

        BMICalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tall.getText().toString().equals("") && !Weight.getText().toString().equals("")) {
                    Tall.setHint("키를 입력 해주세요");
                    Double dtall = Double.parseDouble(Tall.getText().toString()) * 0.01, dBMI;
                    dBMI = Double.parseDouble(Weight.getText().toString()) / (dtall * dtall);
                    BMI = String.format("%.1f", dBMI);
                    BMI_TV.setText(BMI);
                }
            }
        });

        //Diabetes
        Diabetes_Radio = (RadioGroup) findViewById(R.id.Diabetes);
        Diabetes_None = (RadioButton) findViewById(R.id.DiabetesNone);
        Diabetes_Type1 = (RadioButton) findViewById(R.id.DiabetesType1);
        Diabetes_Type2 = (RadioButton) findViewById(R.id.DiabetesType2);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(14).equals("None")) {
                    Diabetes_None.setChecked(true);
                    Diabetes = "None";
                }
                if (cursor.getString(14).equals("Type1")) {
                    Diabetes_Type1.setChecked(true);
                    Diabetes = "Type1";
                }
                if (cursor.getString(14).equals("Type2")) {
                    Diabetes_Type2.setChecked(true);
                    Diabetes = "Type2";
                }
            }
        }

        Diabetes_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.DiabetesNone:
                        Diabetes = "None";
                        break;

                    case R.id.DiabetesType1:
                        Diabetes = "Type1";
                        break;

                    case R.id.DiabetesType2:
                        Diabetes = "Type2";
                        break;
                }
            }
        });

        // HxDM
        HxDM_Radio = (RadioGroup) findViewById(R.id.HxDM);
        HxDM_Yes = (RadioButton) findViewById(R.id.HxDMYes);
        HxDM_No = (RadioButton) findViewById(R.id.HxDMNo);
        HxDM_PreDM = (RadioButton)findViewById(R.id.HxDMPreDM);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(15).equals("yes")) {
                    HxDM_Yes.setChecked(true);
                    HxDM = "Yes";
                }
                if (cursor.getString(15).equals("No")) {
                    HxDM_No.setChecked(true);
                    HxDM = "No";
                }
                if (cursor.getString(15).equals("PreDM")) {
                    HxDM_PreDM.setChecked(true);
                    HxDM = "PreDm";
                }
            }
        }

        HxDM_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.HxDMYes:
                        HxDM = "Yes";
                        break;

                    case R.id.HxDMNo:
                        HxDM = "No";
                        break;

                    case R.id.HxDMPreDM:
                        HxDM = "PreDm";
                        break;
                }
            }
        });

        // Family History of Early CHD
        FamilyHistory_Radio = (RadioGroup) findViewById(R.id.FamilyHistory);
        FamilyHistory_CHD = (RadioButton) findViewById(R.id.FamilyHistoryCHD);
        FamilyHistory_Stroke = (RadioButton) findViewById(R.id.FamilyHistoryStroke);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                if (cursor.getString(16).equals("CHD")) {
                    FamilyHistory_CHD.setChecked(true);
                    FamilyHistory = "CHD";
                }
                if (cursor.getString(16).equals("Stroke")) {
                    FamilyHistory_Stroke.setChecked(true);
                    FamilyHistory = "Stroke";
                }
            }
        }

        FamilyHistory_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.FamilyHistoryCHD:
                        FamilyHistory = "CHD";
                        break;

                    case R.id.FamilyHistoryStroke:
                        FamilyHistory = "Stroke";
                        break;
                }
            }
        });

        //Treatment
        Treatment_Radio = (RadioGroup) findViewById(R.id.Treatment);
        Treatment_YES = (RadioButton) findViewById(R.id.TreatmentYes);
        Treatmtent_No = (RadioButton) findViewById(R.id.TreatmentNo);
        Treatment_TV = (TextView) findViewById(R.id.Treatment_TV);

        Treatment_Layout = (LinearLayout) findViewById(R.id.TreatmentLayout);

        Treatment_Layout.setVisibility(View.GONE);
        Treatment_TV.setVisibility(View.GONE);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

            if (cursor.moveToNext()) {
                Treatment = cursor.getString(17);
                if (cursor.getString(17).substring(0, 3).equals("Yes")) {
                    Treatment_YES.setChecked(true);
                    Treatment_Layout.setVisibility(View.VISIBLE);
                    Treatment_TV.setVisibility(View.VISIBLE);

                }
                if (cursor.getString(17).equals("No  ")) {
                    Treatmtent_No.setChecked(true);
                    Treatment_Layout.setVisibility(View.GONE);
                    Treatment_TV.setVisibility(View.GONE);
                    Treatment = "No  ";
                }
                Treatment_TV.setText("Type of Medicine : " + Treatment.substring(4));
            }
        }

        Treatment_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.TreatmentYes:
                        Treatment = "Yes";
                        Treatment_Layout.setVisibility(View.VISIBLE);
                        Treatment_TV.setVisibility(View.VISIBLE);
                        break;

                    case R.id.TreatmentNo:
                        Treatment = "No  ";
                        Treatment_Layout.setVisibility(View.GONE);
                        Treatment_TV.setVisibility(View.GONE);
                        break;
                }
            }
        });

        //Treatment - Type of medicine
        Statin = (CheckBox) findViewById(R.id.TreamentStatin);
        HTTx = (CheckBox) findViewById(R.id.TreamentHTTx);
        DMTx = (CheckBox) findViewById(R.id.TreamentDMTx);

        Statin.setOnCheckedChangeListener(onCheckedChangeListener);
        HTTx.setOnCheckedChangeListener(onCheckedChangeListener);
        DMTx.setOnCheckedChangeListener(onCheckedChangeListener);

        //Current Smoking
        Smoking_Spinner = (Spinner) findViewById(R.id.SmokingSpinner);
        Smoking_Radio = (RadioGroup) findViewById(R.id.Smoking);
        Smoking_Yes = (RadioButton) findViewById(R.id.SmokingYes);
        Smoking_No = (RadioButton) findViewById(R.id.SmokingNo);

        Smoking_TV = (TextView) findViewById(R.id.Smoking_TV);

        adapter = ArrayAdapter.createFromResource(this, R.array.Smoking, android.R.layout.simple_spinner_dropdown_item);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

            if (cursor.moveToNext()) {
                Smoking = cursor.getString(18);
                Smoking_TV.setText("Smoking Status? : " + Smoking);

                if (cursor.getString(18).equals("Yes") ||
                        cursor.getString(18).equals("ex-Smoker") ||
                        cursor.getString(18).equals("Light-Smoker(Less then 10)") ||
                        cursor.getString(18).equals("Moderate-Smoker (10 to 19)") ||
                        cursor.getString(18).equals("Heavy Smoker (20 or Over)")) {
                    Smoking_Yes.setChecked(true);
                    Smoking_Spinner.setSelection(sPosition);
                }
                if (cursor.getString(18).equals("No")) {
                    Smoking_No.setChecked(true);
                    Smoking_Spinner.setVisibility(View.GONE);
                    Smoking = "No";
                }
            }
        }

        Smoking_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.SmokingYes:
                        Smoking_Spinner.setVisibility(View.VISIBLE);
                        break;

                    case R.id.SmokingNo:
                        Smoking_Spinner.setVisibility(View.GONE);
                        Smoking = "No";
                        break;
                }
            }
        });

        Smoking_Spinner.setAdapter(adapter);

        Smoking_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Smoking = parent.getItemAtPosition(position).toString();
                sPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Smoking = "ex-Smoker";
            }
        });

        //Systolic BP
        SystolicBP = (EditText) findViewById(R.id.SystolicBP);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                SystolicBP.setText(cursor.getString(19));
            }
        }

        //Diastolic BP
        DiastolicBP = (EditText) findViewById(R.id.DiadtolicBP);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                DiastolicBP.setText(cursor.getString(20));
            }
        }

        //Total Cholesterol
        Total_C = (EditText) findViewById(R.id.TotalCholesterol);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                Total_C.setText(cursor.getString(21));
            }
        }

        //Glucose
        Glucose = (EditText) findViewById(R.id.Glucose);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                Glucose.setText(cursor.getString(22));
            }
        }

        //HDL
        HDL = (EditText) findViewById(R.id.HDL);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                HDL.setText(cursor.getString(22));
            }
        }

        //LDL
        LDL = (EditText) findViewById(R.id.LDL);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                LDL.setText(cursor.getString(24));
            }
        }

        //Trigryceride
        Tri = (EditText) findViewById(R.id.Trigryceride);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                Tri.setText(cursor.getString(25));
            }
        }

        //Image
        image_Radio = (RadioGroup) findViewById(R.id.Image);
        image_CT = (RadioButton) findViewById(R.id.ImageCT);
        image_MRI = (RadioButton) findViewById(R.id.ImageMRI);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

            if (cursor.moveToNext()) {
                image = cursor.getString(26);

                if (cursor.getString(26).equals("CT")) {
                    image_CT.setChecked(true);
                    image = "CT";
                }
                if (cursor.getString(26).equals("MRI")) {
                    image_MRI.setChecked(true);
                    image = "MRI";
                }
            }
        }

        image_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ImageCT:
                        image = "CT";
                        break;

                    case R.id.ImageMRI:
                        image = "MRI";
                        break;
                }
            }
        });

        //10 Year CVD Risk
        XYCR = (EditText)findViewById(R.id.XYearCVDLisk);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                XYCR.setText(cursor.getString(27));
            }
        }

        //Heart/Vascular Age
        HVAge = (EditText) findViewById(R.id.HVA);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                HVAge.setText(cursor.getString(28));
            }
        }

        //Optimal Risk
        OptimalLisk = (EditText) findViewById(R.id.OptimalLisk);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                OptimalLisk.setText(cursor.getString(29));
            }
        }

        //RiskScore
        RiskScore = "...";

        //Comment
        Comment = (EditText) findViewById(R.id.Comment);

        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM PATIENT_INFO WHERE PATIENT_NUMBER = " + DBindex;

            Cursor cursor = sqLiteDB.rawQuery(sqlQueryTable, null);
            if (cursor.moveToNext()) {
                Comment.setText(cursor.getString(31));
            }
        }



        FinishBtn = (Button)findViewById(R.id.BackBtn);
        FinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_values( );
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivityForResult(intent,sub);
                finish();
            }
        });
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cnt = 0;
                    switch (buttonView.getId()) {

                        case R.id.TreamentStatin :
                            if (isChecked) {
                                cnt++;
                                if (cnt == 0) Treatment += " /Statin";
                                else Treatment = "Yes";
                            }
                            else Treatment = Treatment.replaceAll(" /Statin", "");
                            Toast.makeText(getApplicationContext(),Treatment,Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.TreamentHTTx :
                            if (isChecked) {
                                cnt++;
                                if (cnt <= 1) Treatment += " /HTTx";
                                else Treatment = "Yes";
                            }
                            else Treatment = Treatment.replaceAll(" /HTTx", "");
                            Toast.makeText(getApplicationContext(),Treatment,Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.TreamentDMTx :
                            if (isChecked) {
                                cnt++;
                                if (cnt >= 1) Treatment += " /DMTx";
                                else Treatment = "Yes";
                            }
                            else Treatment = Treatment.replaceAll(" /DMTx", "");
                            Toast.makeText(getApplicationContext(),Treatment,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

    private SQLiteDatabase init_patient_database() {
        SQLiteDatabase db = null;
        File file = new File(getFilesDir(),"Patient_Info.db");

        //Toast.makeText(getApplicationContext(),"PATH : " + file.toString(),Toast.LENGTH_LONG).show();
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (db == null) Toast.makeText(getApplicationContext(),"DB Creation failed. " + file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        return db;
    }

    private void init_patient_tables() {
        if (sqLiteDB != null) {
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS PATIENT_INFO (" +
                    "HOSPITAL "                + "TEXT, " +
                    "CLINICIAN "               + "TEXT, " +
                    "C_LICECENSE_NUM "         + "TEXT, " +
                    "DIAGNOSTIC_HISTORY "      + "TEXT, " +
                    "EXAMINATION_DATE "        + "TEXT, " +
                    "EPISODE "                 + "TEXT, " +
                    "CVRM_TYPE "               + "TEXT, " +
                    "PATIENT_NAME "            + "TEXT, " +
                    "PATIENT_NUMBER "          + "TEXT, " +
                    "SEX "                     + "TEXT, " +
                    "DATE_OF_BIRTH "           + "TEXT, " +
                    "TALL "                    + "TEXT, " +
                    "WEIGHT "                  + "TEXT, " +
                    "BMI "                     + "TEXT, " +
                    "DIABETES "                + "TEXT, " +
                    "HxDM "                    + "TEXT, " +
                    "FAMILYHISTORYOFEARLYCHD " + "TEXT, " +
                    "TREATMENT "               + "TEXT, " +
                    "SMOKING "                 + "TEXT, " +
                    "SYSTOLIC_BP "             + "TEXT, " +
                    "DIASTOLIC_BP "            + "TEXT, " +
                    "TOTAL_COLASTEROL "        + "TEXT, " +
                    "GLUCOSE "                 + "TEXT, " +
                    "HDL "                     + "TEXT, " +
                    "LDL "                     + "TEXT, " +
                    "TRIGLICERIDE "            + "TEXT, " +
                    "IMAGE "                   + "TEXT, " +
                    "X_YEAR_CVD_RISK "         + "TEXT, " +
                    "HEART_VASCULAR_AGE "      + "TEXT, " +
                    "OPTIMAL_RISK "            + "TEXT, " +
                    "RISK_SCORE "              + "TEXT, " +
                    "COMMENT "                 + "TEXT" + ")";
            System.out.println(sqlCreateTable);
            sqLiteDB.execSQL(sqlCreateTable);
        }
    }

    private void save_values( ) {
        if (sqLiteDB != null) {
            String SQLinsert =
                    "UPDATE PATIENT_INFO SET " +
                            "HOSPITAL = '" + HospitalName.getText().toString()      + "', " +
                            "CLINICIAN = '" + Clinician.getText().toString()        + "', " +
                            "C_LICECENSE_NUM = '" + LicenseNum.getText().toString() + "', " +
                            "DIAGNOSTIC_HISTORY = '" + Diagnostic                   + "', " +
                            "EXAMINATION_DATE = '" + Examination_Date               + "', " +
                            "EPISODE = '" + Episode                                 + "', " +
                            "CVRM_TYPE = '" + CVRM                                  + "', " +
                            "PATIENT_NAME = '" + PatientName.getText().toString()   + "', " +
                            "PATIENT_NUMBER = '" + PatientNum.getText().toString()  + "', " +
                            "SEX = '" + Sex                                         + "', " +
                            "DATE_OF_BIRTH = '" + PatientBirth_Date                 + "', " +
                            "TALL = '" + Tall.getText().toString()                  + "', " +
                            "WEIGHT = '" + Weight.getText().toString()              + "', " +
                            "BMI = '" + BMI                                         + "', " +
                            "DIABETES = '" + Diabetes                               + "', " +
                            "HxDM = '" + HxDM                                       + "', " +
                            "FAMILYHISTORYOFEARLYCHD = '" + FamilyHistory           + "', " +
                            "TREATMENT = '" + Treatment                             + "', " +
                            "SMOKING = '" + Smoking                                 + "', " +
                            "SYSTOLIC_BP = '" + SystolicBP.getText().toString()     + "', " +
                            "DIASTOLIC_BP = '" + DiastolicBP.getText().toString()   + "', " +
                            "TOTAL_COLASTEROL = '" + Total_C.getText().toString()   + "', " +
                            "GLUCOSE = '" + Glucose.getText().toString()            + "', " +
                            "HDL = '" + HDL.getText().toString()                    + "', " +
                            "LDL = '" + LDL.getText().toString()                    + "', " +
                            "TRIGLICERIDE = '" + Tri.getText().toString()           + "', " +
                            "IMAGE = '" + image                                     + "', " +
                            "X_YEAR_CVD_RISK = '" + XYCR.getText().toString()       + "', " +
                            "HEART_VASCULAR_AGE = '" + HVAge.getText().toString()   + "', " +
                            "OPTIMAL_RISK = '" + OptimalLisk.getText().toString()   + "', " +
                            "RISK_SCORE = '" + RiskScore                            + "', " +
                            "COMMENT = '" + Comment.getText().toString()            + "'  " +
                            " WHERE PATIENT_NUMBER = '" + DBindex + "'";

            //Toast.makeText(getApplicationContext(),SQLinsert,Toast.LENGTH_LONG).show();
            sqLiteDB.execSQL(SQLinsert);
        }
    }
}


