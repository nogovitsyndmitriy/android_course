package com.gmail.task_6_texteditor

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_file_dialog.view.*
import java.io.File

private const val SETTINGS_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {

    private var filesMap = mutableMapOf<String, File>()
    private var createFileDialogId: Int = 1

    interface ListItemActionListener {
        fun onItemClicked(name: String, button: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createFileBtn.setOnClickListener {
            showDialog(createFileDialogId)
        }
        settingsBtn.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(settingsIntent, SETTINGS_REQUEST_CODE)
        }
        recyclerFilesView.apply {
            adapter = FilesAdapter(filesMap, object : ListItemActionListener {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onItemClicked(name: String, button: String) {
                    if (button == "DELETE") {
                        filesMap.remove(name)
                        adapter?.notifyDataSetChanged()
                    } else if (button == "EDIT") {
                        val intent = Intent(this@MainActivity, EditFileActivity::class.java)
                        intent.putExtra("File", filesMap[name])
                        intent.putExtra("FileName", name)
                        startActivityForResult(intent, 555)
                    }
                }
            })
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 555 && resultCode == 666 && data != null) {
            val fileName = data.getStringExtra("FileName").toString()
            val file = data.getSerializableExtra("File") as File
            filesMap.remove(fileName)
            filesMap[fileName] = file
        }
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            if (data.getBooleanExtra("IS_SHARED_STORAGE", false)) {
                editor.putBoolean("IS_SHARED_STORAGE", true)
            } else {
                editor.putBoolean("IS_SHARED_STORAGE", false)
            }
            editor.apply()
        }
        this.recyclerFilesView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateDialog(id: Int): Dialog? {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.create_file_dialog, null)
        builder.setView(view)
            .setPositiveButton(R.string.create) { _, _ ->
                val counter: Int = 0;
                val fileName = view.fileNameEditText.text.toString()
                val checkedFileName: String = validateFileName(fileName, counter)
                filesMap[checkedFileName] = if (getPreferences(Context.MODE_PRIVATE).getBoolean("IS_SHARED_STORAGE", false)) {
                    File(externalCacheDir, "$checkedFileName.txt")
                } else {
                    File(filesDir, "$checkedFileName.txt")
                }
                this.recyclerFilesView.adapter?.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
        return builder.create()
    }

    private fun validateFileName(fileName: String, counter: Int): String {
        var newCounter = counter
        var totalFileName = fileName;
        if (filesMap.isNotEmpty() && filesMap[fileName] != null) {
            newCounter += 1
            val newFilename = fileName + newCounter
            totalFileName = if (filesMap[newFilename] != null) {
                validateFileName(fileName, newCounter)
            } else {
                newFilename
            }
        }
        return totalFileName
    }

    class FilesAdapter(
        private val map: Map<String, File>,
        private val listItemActionListener: ListItemActionListener?
    ) :
        RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return FilesViewHolder(inflater, parent, listItemActionListener)
        }

        override fun getItemCount(): Int = map.size

        override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
            val filePair: Pair<String, File> = map.toList()[position]
            holder.bind(filePair)
        }

        class FilesViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            private var listItemClickListener: ListItemActionListener?
        ) : RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.file_item,
                parent,
                false
            )
        ) {
            private val fileName: TextView = itemView.findViewById(R.id.fileName)
            private val editBtn: ImageButton = itemView.findViewById(R.id.editFileBtn)
            private val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteFileBtn)

            fun bind(filePair: Pair<String, File>) {
                fileName.text = filePair.first
                deleteBtn.setOnClickListener {
                    listItemClickListener?.onItemClicked(filePair.first, "DELETE")
                }
                editBtn.setOnClickListener {
                    listItemClickListener?.onItemClicked(filePair.first, "EDIT")
                }
            }
        }
    }
}