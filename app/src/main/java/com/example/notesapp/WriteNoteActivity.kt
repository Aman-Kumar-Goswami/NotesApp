package com.example.notesapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class WriteNoteActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabase
    private var noteId: Int = 0
    private var folderId: Int? = null
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_note)

        db = NoteDatabase.getDatabase(this)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        val btnSave = findViewById<TextView>(R.id.btnSave)

        // Get Note ID and Folder ID from intent
        noteId = intent.getIntExtra("NOTE_ID", 0)
        val fId = intent.getIntExtra("FOLDER_ID", -1)
        if (fId != -1) {
            folderId = fId
        }

        // If editing an existing note, load its data
        if (noteId != 0) {
            loadNoteData()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun loadNoteData() {
        lifecycleScope.launch {
            val note = db.noteDao().getNoteById(noteId)
            note?.let {
                etTitle.setText(it.title)
                etContent.setText(it.content)
                folderId = it.folderId
            }
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isNotEmpty() || content.isNotEmpty()) {
            lifecycleScope.launch {
                if (noteId == 0) {
                    // Create new note
                    val newNote = Note(title = title, content = content, folderId = folderId)
                    db.noteDao().insertNote(newNote)
                } else {
                    // Update existing note
                    val updatedNote = Note(
                        id = noteId,
                        title = title,
                        content = content,
                        updatedAt = System.currentTimeMillis(),
                        folderId = folderId
                    )
                    db.noteDao().updateNote(updatedNote)
                }
                finish()
            }
        } else {
            Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}
