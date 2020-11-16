package com.gmail.task_6_texteditor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings.backAndApplyButton

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        val sharedStorageButton = findViewById<RadioButton>(R.id.sharedStorageButton)
        val appSpecificButton = findViewById<RadioButton>(R.id.appSpecificButton)
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        val isSharedPrefChecked = sharedPrefs.getBoolean("IS_SHARED_STORAGE", false)
        if (isSharedPrefChecked) {
            sharedStorageButton.isChecked = true
        } else {
            appSpecificButton.isChecked = true
        }
        backAndApplyButton.setOnClickListener {
            val result = Intent()
            val editor = sharedPrefs.edit()

            if (sharedStorageButton.isChecked) {
                editor.putBoolean("IS_SHARED_STORAGE", true)
                result.putExtra("IS_SHARED_STORAGE", true)
            } else {
                editor.putBoolean("IS_SHARED_STORAGE", false)
                result.putExtra("IS_SHARED_STORAGE", false)
            }
            editor.apply()
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }
}