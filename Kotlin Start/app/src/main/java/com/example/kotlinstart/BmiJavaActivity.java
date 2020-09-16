package com.example.kotlinstart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BmiJavaActivity extends AppCompatActivity {

    EditText TallFeild, Weightfield;
    TextView ResultView;
    Button bmiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laydout_view_viding);

        TallFeild = findViewById(R.id.tallField);
        Weightfield = findViewById(R.id.weightField);
        ResultView = findViewById(R.id.resultLabel);
        bmiButton = findViewById(R.id.bmiButton);


        findViewById(R.id.bmiButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tall = TallFeild.getText().toString();

                String weight = Weightfield.getText().toString();

                double bmi = Double.parseDouble(weight) / Math.pow(Double.parseDouble(tall) / 100.0, 2);

                TextView resultLabel = findViewById(R.id.resultLabel);
                resultLabel.setText("키:" + tall + ", 체중: " + weight + " , BMI: " + bmi);
            }
        });
    }
}