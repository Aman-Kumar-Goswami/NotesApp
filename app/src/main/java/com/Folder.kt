package com


import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R

class FolderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_folder)

        // Back button
        val backButton = findViewById<ImageButton>(R.id.btnBack)

        backButton.setOnClickListener {
            finish()
        }

        // Create Folder
        val createFolder = findViewById<TextView>(R.id.createFolder)

        createFolder.setOnClickListener {
            showCreateFolderDialog()
        }
    }

    private fun showCreateFolderDialog() {

        val editText = android.widget.EditText(this)
        editText.hint = "Folder name"

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Create Folder")
            .setView(editText)
            .setPositiveButton("Create") { dialog, _ ->

                val folderName = editText.text.toString().trim()

                if (folderName.isNotEmpty()) {
                    // Yahan folder save karne ka code baad mein add karenge
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
}

