package com.example.counterboard.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counterboard.R
import com.example.counterboard.databinding.ActivityLogginBinding

class LogginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLogginBinding
    lateinit var querty : String
    private lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLogginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("myPref", MODE_PRIVATE)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        querty= binding.textInputEditText.text.toString()
        binding.nextButton.setOnClickListener {
            if (querty!= null && querty != " "){
                val intent= Intent(this, MainActivity::class.java)

                startActivity(intent)
            }
        }
    }
}