package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class FolderNotesActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabase
    private lateinit var noteAdapter: NoteAdapter
    private var folderId: Int = -1
    private var folderName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_notes)

        db = NoteDatabase.getDatabase(this)

        folderId = intent.getIntExtra("FOLDER_ID", -1)
        folderName = intent.getStringExtra("FOLDER_NAME")

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvFolderName = findViewById<TextView>(R.id.tvFolderName)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFolderNotes)
        val fabAddNote = findViewById<FloatingActionButton>(R.id.fabAddNoteInFolder)

        tvFolderName.text = folderName ?: "Folder"

        btnBack.setOnClickListener {
            finish()
        }

        noteAdapter = NoteAdapter(emptyList()) { note ->
            val intent = Intent(this, WriteNoteActivity::class.java)
            intent.putExtra("NOTE_ID", note.id)
            intent.putExtra("FOLDER_ID", folderId)
            startActivity(intent)
        }
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddNote.setOnClickListener {
            val intent = Intent(this, WriteNoteActivity::class.java)
            intent.putExtra("FOLDER_ID", folderId)
            startActivity(intent)
        }

        loadNotes()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        if (folderId != -1) {
            lifecycleScope.launch {
                val notes = db.noteDao().getNotesByFolder(folderId)
                noteAdapter.updateNotes(notes)
            }
        }
    }
}
