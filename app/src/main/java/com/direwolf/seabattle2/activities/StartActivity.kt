package com.direwolf.seabattle2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.direwolf.seabattle2.R

class StartActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            val intent = Intent(this, PlacementActivity::class.java)
            startActivity(intent)
        }
        Toast.makeText(this, "Добро пожаловать в игру SeaBattle!", Toast.LENGTH_SHORT).show()
    }
}