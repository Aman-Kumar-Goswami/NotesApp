package com


import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Clear search
        binding.btnClear.setOnClickListener {
            binding.etSearch.text.clear()
            binding.etSearch.requestFocus()
        }

        // Automatically open keyboard
        binding.etSearch.requestFocus()

        binding.etSearch.postDelayed({
            val keyboard =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            keyboard.showSoftInput(
                binding.etSearch,
                InputMethodManager.SHOW_IMPLICIT
            )
        }, 200)
    }
}