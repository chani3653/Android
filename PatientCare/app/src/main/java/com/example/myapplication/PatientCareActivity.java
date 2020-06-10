package com.example.myapplication;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PatientCareActivity extends Activity {

    ArrayAdapter adapter;

    Button FinishBtn, EDPickerBtn, BPickerBtn, BMICalBtn;

    DatePickerDialog Examination, DateOfBirth;

    Date currentTime = Calendar.getInstance().getTime();

    CheckBox Statin,HTTx, DMTx;

    EditText Clinician, LicenseNum, PatientNum, Tall, Weight, PatientName, SystolicBP, DiastolicBP,
            Total_C, Glucose, HDL, LDL, Tri, XYCR, HVAge, OptimalLisk, Comment;

    String Diagnostic = "No", Examination_Date, Episode = "Visit", CVRM = "Case", PatientBirth_Date, Sex, BMI, Diabetes = "None", HxDM = "No",
            FamilyHistory = "CHD", Treatment = "No  ", Smoking = "No", image = "CT", RiskScore;

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

    SQLiteDatabase sqLiteDB, sqLiteDB2; // SQLite 서버관련 변수 선언

    SimpleDateFormat Year, Month, Day;

    TextView PatientName_TV, HospitalName, DoctorName, ExaminationDate, PatientBirthDate, BMI_TV, Treatment_TV;

    public static final int sub = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sqLiteDB = init_user_database(); // 변수 초기화 아래쪽 함수로 초기화
        sqLiteDB2 = init_patient_database(); // 위와 동일

        init_patient_tables(); // 환자 데이터베이스의 테이블을 불러옴

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientcare);

        Year = new SimpleDateFormat("yyyy", Locale.getDefault());
        Month = new SimpleDateFormat("MM", Locale.getDefault());
        Day = new SimpleDateFormat("dd", Locale.getDefault());

        final String sYear = Year.format(currentTime),  sMonth = Month.format(currentTime), sDay = Day.format(currentTime);

        //-----------------------------------------doctor part------------------------------------//
        DoctorName = (TextView)findViewById(R.id.DoctorName);
        Clinician = (EditText)findViewById(R.id.Clinician);
        LicenseNum = (EditText)findViewById(R.id.PC_LicenceNum);
        HospitalName = (TextView)findViewById(R.id.HosName);

        // 데이터 베이스에서 의사이름과 병원이름 불러오는 부분
        if (sqLiteDB != null) {
            String sqlQueryTable = "SELECT * FROM DOCTOR_INFO";

            Cursor cursor = null;
            cursor = sqLiteDB.rawQuery(sqlQueryTable, null);

            if (cursor.moveToNext()) {
                DoctorName.setText(cursor.getString(2)); // 상단 의사이름
                Clinician.setText(cursor.getString(2)); // 내부 의사이름
                Clinician.setClickable(false);
                LicenseNum.setText(cursor.getString(3)); // 내부 의사 등록번호
                LicenseNum.setClickable(false);
                HospitalName.setText(cursor.getString(4)); // 상단 병원이름
            }
        }

        //--------------------------------------patient part--------------------------------------//

        //---Medical info---//

        //Diagnostic History
        Diagostic_H_Radio = (RadioGroup) findViewById(R.id.DiagnosticHistory);
        Diagostic_H_Yes = (RadioButton) findViewById(R.id.DiagnosticHistoryYes);
        Diagostic_H_No = (RadioButton) findViewById(R.id.DiagnosticHistoryNo);

        Diagostic_H_No.setChecked(true);

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
                        Examination_Date = "Not exist";
                        ExaminationDate.setVisibility(View.GONE);
                        EDPickerBtn.setVisibility(View.GONE);
                        break;
                }
            }
        });

        //Examination Date
        ExaminationDate = (TextView)findViewById(R.id.ExaminationDate);
        EDPickerBtn = (Button)findViewById(R.id.EDDataPicker);

        ExaminationDate.setVisibility(View.GONE);
        EDPickerBtn.setVisibility(View.GONE);

        EDPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Examination = new DatePickerDialog(PatientCareActivity.this, listner,
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

        Episode_Visit.setChecked(true);

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

        CVRM_Case.setChecked(true);

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

        //Patient Number
        PatientNum = (EditText) findViewById(R.id.patientNumber);

        //Patient Sex
        Sex_Radio = (RadioGroup) findViewById(R.id.Sex);
        Sex_Male = (RadioButton) findViewById(R.id.SexMale);
        Sex_Female = (RadioButton) findViewById(R.id.SexFemale);

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
        BPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateOfBirth = new DatePickerDialog(PatientCareActivity.this, listner,
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
        });

        // Tall
        Tall = (EditText)findViewById(R.id.Tall);

        // Weight
        Weight = (EditText)findViewById(R.id.Weight);

        //BMI
        BMICalBtn = (Button)findViewById(R.id.BMIButton);
        BMI_TV = (TextView)findViewById(R.id.BMI);

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

        Diabetes_None.setChecked(true);

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

        HxDM_No.setChecked(true);

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

        FamilyHistory_CHD.setChecked(true);

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

        Treatmtent_No.setChecked(true);

        Treatment_Layout = (LinearLayout) findViewById(R.id.TreatmentLayout);

        Treatment_Layout.setVisibility(View.GONE);
        Treatment_TV.setVisibility(View.GONE);

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

        Smoking_No.setChecked(true);

        Smoking_Spinner.setVisibility(View.GONE);

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

        adapter = ArrayAdapter.createFromResource(this, R.array.Smoking, android.R.layout.simple_spinner_dropdown_item);

        Smoking_Spinner.setAdapter(adapter);
        Smoking_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Smoking = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Systolic BP
        SystolicBP = (EditText) findViewById(R.id.SystolicBP);

        //Diastolic BP
        DiastolicBP = (EditText) findViewById(R.id.DiadtolicBP);

        //Total Cholesterol
        Total_C = (EditText) findViewById(R.id.TotalCholesterol);

        //Glucose
        Glucose = (EditText) findViewById(R.id.Glucose);

        //HDL
        HDL = (EditText) findViewById(R.id.HDL);

        //LDL
        LDL = (EditText) findViewById(R.id.LDL);

        //Trigryceride
        Tri = (EditText) findViewById(R.id.Trigryceride);

        //Image
        image_Radio = (RadioGroup) findViewById(R.id.Image);
        image_CT = (RadioButton) findViewById(R.id.ImageCT);
        image_MRI = (RadioButton) findViewById(R.id.ImageMRI);

        image_CT.setChecked(true);

        if (image == "CT") image_CT.setChecked(true);
        if (image == "MRI") image_MRI.setChecked(true);

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

        //Heart/Vascular Age
        HVAge = (EditText) findViewById(R.id.HVA);

        //Optimal Risk
        OptimalLisk = (EditText) findViewById(R.id.OptimalLisk);

        //Comment
        Comment = (EditText) findViewById(R.id.Comment);

        //RiskScore

        RiskScore = "...";

        FinishBtn = (Button)findViewById(R.id.BackBtn);
        FinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_Blank();
            }
        });
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.TreamentStatin :
                            if (isChecked) {
                                Treatment += " /Statin";
                            }

                            else Treatment = Treatment.replaceAll(" /Statin", "");
                            break;

                        case R.id.TreamentHTTx :
                            if (isChecked) Treatment += " /HTTx";
                            else Treatment = Treatment.replaceAll(" /HTTx", "");
                            break;

                        case R.id.TreamentDMTx :
                            if (isChecked) Treatment += " /DMTx";
                            else Treatment = Treatment.replaceAll(" /DMTx", "");
                            break;
                    }
                }
            };

    /* 의사 데이터 베이스 로드부분
     SQLite의 로드 구조이기 때문에 파일(데이터베이스 파일)을 만들고 데이터 베이스 파일이 있고 없음을 판별하는 부분

     이 부분을 외부 데이터 베이스에 접속하고 데이터 베이스에 접근하는 코드로 채워 주시면 됩니다. */
    private SQLiteDatabase init_user_database() {
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

    // 환자 데이터 베이스 로드 부분 의사 데이터 베이스 로드 부분과 동일
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

    // 데이터 베이스가 없을 경우 데이터 베이스의 테이블을 만드는 함수
    private void init_patient_tables() {
        if (sqLiteDB2 != null) {
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
            sqLiteDB2.execSQL(sqlCreateTable);
        }
    }

    // 데이터 베이스에 결과값들을 저장하는 함수
    private void save_values( ) {
        if (sqLiteDB2 != null) {
            String SQLinsert =
                "INSERT INTO PATIENT_INFO " +
                        "(HOSPITAL, CLINICIAN, C_LICECENSE_NUM, DIAGNOSTIC_HISTORY, EXAMINATION_DATE, EPISODE, CVRM_TYPE, PATIENT_NAME, " +
                        "PATIENT_NUMBER, SEX, DATE_OF_BIRTH, TALL, WEIGHT, BMI, DIABETES, HxDM, FAMILYHISTORYOFEARLYCHD, TREATMENT, SMOKING, " +
                        "SYSTOLIC_BP, DIASTOLIC_BP, TOTAL_COLASTEROL, GLUCOSE, HDL, LDL, TRIGLICERIDE, IMAGE, X_YEAR_CVD_RISK, HEART_VASCULAR_AGE," +
                        " OPTIMAL_RISK, RISK_SCORE, COMMENT) VALUES (" +
                        "'" + HospitalName.getText().toString() + "'," +
                        "'" + Clinician.getText().toString()    + "'," +
                        "'" + LicenseNum.getText().toString()   + "'," +
                        "'" + Diagnostic                        + "'," +
                        "'" + Examination_Date                  + "'," +
                        "'" + Episode                           + "'," +
                        "'" + CVRM                              + "'," +
                        "'" + PatientName.getText().toString()  + "'," +
                        "'" + PatientNum.getText().toString()   + "'," +
                        "'" + Sex                               + "'," +
                        "'" + PatientBirth_Date                 + "'," +
                        "'" + Tall.getText().toString()         + "'," +
                        "'" + Weight.getText().toString()       + "'," +
                        "'" + BMI                               + "'," +
                        "'" + Diabetes                          + "'," +
                        "'" + HxDM                              + "'," +
                        "'" + FamilyHistory                     + "'," +
                        "'" + Treatment                         + "'," +
                        "'" + Smoking                           + "'," +
                        "'" + SystolicBP.getText().toString()   + "'," +
                        "'" + DiastolicBP.getText().toString()  + "'," +
                        "'" + Total_C.getText().toString()      + "'," +
                        "'" + Glucose.getText().toString()      + "'," +
                        "'" + HDL.getText().toString()          + "'," +
                        "'" + LDL.getText().toString()          + "'," +
                        "'" + Tri.getText().toString()          + "'," +
                        "'" + image                             + "'," +
                        "'" + XYCR.getText().toString()         + "'," +
                        "'" + HVAge.getText().toString()        + "'," +
                        "'" + OptimalLisk.getText().toString()  + "'," +
                        "'" + RiskScore                         + "'," +
                        "'" + Comment.getText().toString()      + "')" ;
            //Toast.makeText(getApplicationContext(),SQLinsert.toString(),Toast.LENGTH_LONG).show();
            sqLiteDB2.execSQL(SQLinsert);
        }
    }

    private void Check_Blank() {
        if (PatientName.getText().toString().equals("") || PatientNum.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"환자 이름과 환자 번호를 입력해 주세요", Toast.LENGTH_LONG).show();

        if (!PatientName.getText().toString().equals("") && !PatientNum.getText().toString().equals("")) {
            save_values();
            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
            startActivityForResult(intent,sub);
            finish();
        }

    }
}


