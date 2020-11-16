package com.gmail.task_6_texteditor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class EditFileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_file)

        val fileNameTextView = findViewById<TextView>(R.id.fileNameText)
        val editFileButton = findViewById<Button>(R.id.editFileButton)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val fileContent = findViewById<EditText>(R.id.fileContent)

        backButton.setOnClickListener {
            finish()
        }
        val intent = intent
        val file = intent.getSerializableExtra("File") as File
        val fileName = intent.getStringExtra("FileName")
        fileNameTextView.text = fileName
        if (file.length() > 0) {
            val contentFromFile: String = FileInputStream(file)
                .bufferedReader()
                .use { out ->
                    out.readLine().toString()
                }
            fileContent.setText(contentFromFile)
        }
        editFileButton.setOnClickListener {
            val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
            val isSharedPrefChecked = sharedPrefs.getBoolean("IS_SHARED_STORAGE", false)
            val resultIntent = Intent()
            file.delete()
            val editedFile = if (isSharedPrefChecked) {
                File(externalCacheDir, "$fileName.txt")
            } else {
                File(filesDir, "$fileName.txt")
            }
            FileOutputStream(file, true)
                .bufferedWriter()
                .use { out ->
                    out.append(fileContent.text)
                    out.newLine()
                    out.close()
                }
            resultIntent.putExtra("File", editedFile)
            resultIntent.putExtra("FileName", fileName)
            setResult(666, resultIntent)
            finish()
        }
    }
}