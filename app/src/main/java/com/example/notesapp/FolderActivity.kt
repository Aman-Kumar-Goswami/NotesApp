package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FolderActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabase
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        db = NoteDatabase.getDatabase(this)

        val backButton = findViewById<ImageButton>(R.id.btnBack)
        backButton.setOnClickListener {
            finish()
        }

        val createFolder = findViewById<TextView>(R.id.createFolder)
        createFolder.setOnClickListener {
            showCreateFolderDialog()
        }

        recyclerView = findViewById(R.id.recyclerFolders)
        folderAdapter = FolderAdapter(emptyList()) { folder ->
            val intent = Intent(this, FolderNotesActivity::class.java)
            intent.putExtra("FOLDER_ID", folder.id)
            intent.putExtra("FOLDER_NAME", folder.name)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = folderAdapter

        loadFolders()
    }

    private fun loadFolders(scrollToBottom: Boolean = false) {
        lifecycleScope.launch {
            val folders = db.folderDao().getAllFolders()
            folderAdapter.updateFolders(folders)
            if (scrollToBottom && folders.isNotEmpty()) {
                recyclerView.scrollToPosition(folders.size - 1)
            }
        }
    }

    private fun showCreateFolderDialog() {
        val editText = EditText(this)
        editText.hint = "Folder name"

        AlertDialog.Builder(this)
            .setTitle("Create Folder")
            .setView(editText)
            .setPositiveButton("Create") { dialog, _ ->
                val folderName = editText.text.toString().trim()
                if (folderName.isNotEmpty()) {
                    saveFolder(folderName)
                } else {
                    Toast.makeText(this, "Folder name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveFolder(name: String) {
        lifecycleScope.launch {
            val newFolder = Folder(name = name)
            db.folderDao().insertFolder(newFolder)
            loadFolders(scrollToBottom = true)
            Toast.makeText(this@FolderActivity, "Folder created", Toast.LENGTH_SHORT).show()
        }
    }
}
