package com

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class Setting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun showListOrderDialog() {

        val options = arrayOf(
            "By Time",
            "By Modified"
        )

        AlertDialog.Builder(this)
            .setTitle("List Order")
            .setSingleChoiceItems(options, 0) { dialog, which ->

                when (which) {

                    0 -> {
                        // Notes ko time ke according sort karo
                    }

                    1 -> {
                        // Notes ko modified date ke according sort karo
                    }
                }

                dialog.dismiss()
            }
            .show()
    }
}