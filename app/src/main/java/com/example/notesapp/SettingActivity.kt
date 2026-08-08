package com.example.notesapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPreferences = getSharedPreferences("NotesSettings", Context.MODE_PRIVATE)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvListOrder = findViewById<TextView>(R.id.tvListOrder)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)
        val tvUserAgreement = findViewById<TextView>(R.id.tvUserAgreement)

        btnBack.setOnClickListener {
            finish()
        }

        tvListOrder.setOnClickListener {
            showListOrderDialog()
        }

        tvVersion.setOnClickListener {
            Toast.makeText(this, "Version 1.0", Toast.LENGTH_SHORT).show()
        }

        tvUserAgreement.setOnClickListener {
            showUserAgreement()
        }
    }

    private fun showListOrderDialog() {
        val options = arrayOf("Newest First", "Oldest First")
        val currentOrder = sharedPreferences.getString("sort_order", "Newest First")
        val checkedItem = options.indexOf(currentOrder)

        AlertDialog.Builder(this)
            .setTitle("List Order")
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                val selectedOrder = options[which]
                sharedPreferences.edit {
                    putString("sort_order", selectedOrder)
                }
                Toast.makeText(this, "Order saved: $selectedOrder", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }

    private fun showUserAgreement() {
        val agreementText = """
            User Agreement
            Welcome to Notes App.
            By using this application, you agree to the following terms:
            1. You can use this app to create, edit, save and manage your personal notes.
            2. You are responsible for the content that you create or store in the application.
            3. This application should not be used to store illegal, harmful or unauthorized content.
            4. Your notes may be stored locally on your device. Please keep your device secure to protect your data.
            5. We are not responsible for any loss of notes or data caused by device failure, accidental deletion, system errors or other unexpected problems.
            6. You agree to use the application responsibly and in accordance with applicable laws.
            7. We may update or improve the application and its features from time to time.
            By continuing to use the application, you acknowledge that you have read and agreed to these terms.
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("User Agreement")
            .setMessage(agreementText)
            .setPositiveButton("I Agree") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
