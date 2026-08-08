package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: NoteDatabase
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = NoteDatabase.getDatabase(this)
        sharedPreferences = getSharedPreferences("NotesSettings", Context.MODE_PRIVATE)

        // Check if User Agreement is already accepted
        if (!sharedPreferences.getBoolean("agreement_accepted", false)) {
            showUserAgreement()
        }

        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        val btnSort = findViewById<ImageButton>(R.id.btnSort)
        val fabAddNote = findViewById<FloatingActionButton>(R.id.fabAddNote)
        val navFolders = findViewById<LinearLayout>(R.id.navFolders)
        val btnMore = findViewById<ImageButton>(R.id.btnMore)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotes)

        // Setup RecyclerView
        noteAdapter = NoteAdapter(emptyList()) { note ->
            val intent = Intent(this, WriteNoteActivity::class.java)
            intent.putExtra("NOTE_ID", note.id)
            startActivity(intent)
        }
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        btnSort.setOnClickListener {
            showSortMenu(it)
        }

        fabAddNote.setOnClickListener {
            val intent = Intent(this, WriteNoteActivity::class.java)
            startActivity(intent)
        }

        navFolders.setOnClickListener {
            val intent = Intent(this, FolderActivity::class.java)
            startActivity(intent)
        }
        
        btnMore.setOnClickListener {
            showPopupMenu(it)
        }

        loadNotes()
    }

    private fun showSortMenu(view: android.view.View) {
        val popup = PopupMenu(this, view)
        popup.menu.add("Newest First")
        popup.menu.add("Oldest First")
        
        popup.setOnMenuItemClickListener { item ->
            sharedPreferences.edit {
                putString("sort_order", item.title.toString())
            }
            loadNotes()
            true
        }
        popup.show()
    }

    private fun showPopupMenu(view: android.view.View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.main_menu, popup.menu)
        
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_agreement -> {
                    showUserAgreement()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            var notes = db.noteDao().getAllNotes()
            
            // Apply internal sort preference
            val sortOrder = sharedPreferences.getString("sort_order", "Newest First")
            notes = if (sortOrder == "Oldest First") {
                notes.sortedBy { it.updatedAt }
            } else {
                notes.sortedByDescending { it.updatedAt }
            }
            
            noteAdapter.updateNotes(notes)
        }
    }

    private fun showUserAgreement() {
        val agreementText = """
            User Agreement
            Welcome to Notes App.
            By using this application, you agree to the following terms:
            1. You can use this app to create, edit, save and manage your personal notes.
            2. You are responsible for the content that you create or store in the application.
            3. Your notes are stored locally on your device.
            4. We are not responsible for any loss of data caused by device failure.
            By clicking 'I Agree', you acknowledge these terms.
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("User Agreement")
            .setMessage(agreementText)
            .setPositiveButton("I Agree") { dialog, _ ->
                sharedPreferences.edit {
                    putBoolean("agreement_accepted", true)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                if (!sharedPreferences.getBoolean("agreement_accepted", false)) {
                    finish() // Close app if agreement not accepted first time
                }
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
