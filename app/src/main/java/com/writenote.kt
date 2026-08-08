package com


import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R

class WriteNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_note)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnSave = findViewById<TextView>(R.id.btnSave)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            finish()
        }
    }
}