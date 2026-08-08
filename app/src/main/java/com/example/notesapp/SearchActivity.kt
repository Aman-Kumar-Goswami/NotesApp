package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var db: NoteDatabase
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabase.getDatabase(this)

        // Setup RecyclerView
        noteAdapter = NoteAdapter(emptyList()) { note ->
            val intent = Intent(this, WriteNoteActivity::class.java)
            intent.putExtra("NOTE_ID", note.id)
            startActivity(intent)
        }
        binding.recyclerSearchResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchResults.adapter = noteAdapter

        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Clear search
        binding.btnClear.setOnClickListener {
            binding.etSearch.text.clear()
            binding.etSearch.requestFocus()
        }

        // Search text listener
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchNotes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Automatically open keyboard
        binding.etSearch.requestFocus()
        binding.etSearch.postDelayed({
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    private fun searchNotes(query: String) {
        val searchQuery = "%$query%"
        lifecycleScope.launch {
            val results = if (query.isEmpty()) {
                emptyList()
            } else {
                db.noteDao().searchNotes(searchQuery)
            }
            noteAdapter.updateNotes(results)
        }
    }
}
