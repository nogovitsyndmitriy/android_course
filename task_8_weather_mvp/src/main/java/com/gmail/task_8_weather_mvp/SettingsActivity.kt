package com.gmail.task_8_weather_mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var units: TextView
    private lateinit var switcher: SwitchCompat
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        units = findViewById(R.id.units)
        backButton = findViewById(R.id.backButton)
        switcher = findViewById(R.id.switcher)
        var unit = loadUnitStatus()
        if (unit == STANDARD) {
            switcher.isChecked = true
            units.text = KELVIN
        } else {
            switcher.isChecked = false
            units.text = CELSIUS
        }
        switcher.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                saveUnitStatus(STANDARD)
                units.text = KELVIN
                unit = STANDARD
            } else {
                saveUnitStatus(METRIC)
                units.text = CELSIUS
                unit = METRIC
            }
        }
        backButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(UNITS, unit)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }


    private fun saveUnitStatus(units: String) {
        val sharedPreferences = applicationContext.getSharedPreferences(UNITS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(UNITS, units)
        editor.apply()
    }

    private fun loadUnitStatus(): String {
        val sharedPrefs = applicationContext.getSharedPreferences(UNITS, Context.MODE_PRIVATE)
        return sharedPrefs.getString(UNITS, METRIC).toString()
    }
}