package com.example.kotlinstart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.laydout_view_viding.*

class BmiKotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.laydout_view_viding)

        bmiButton.setOnClickListener {
            val tall = tallField.text.toString().toDouble()
            val weight = weightField.text.toString().toDouble()
            val bmi = weight / Math.pow(tall / 100, 2.0)

            resultLabel.text = "키: ${tallField.text}, 체중: ${weightField.text}, BMI: $bmi"
        }
    }
}