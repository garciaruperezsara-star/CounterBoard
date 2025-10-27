package com.example.counterboard.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counterboard.R
import com.example.counterboard.databinding.ActivityLogginBinding
import androidx.core.content.edit

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
        val user= pref.getString("username", "")
        if (user !=""){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        supportActionBar?.hide()

        binding.nextButton.setOnClickListener {
            querty= binding.textInputEditText.text.toString()
            if (querty != " "){
                val intent= Intent(this, MainActivity::class.java)
                pref.edit {
                    putString("username", querty)
                }
                Log.i("username", pref.getString("username","err").toString())
                Log.i("username", querty)
                startActivity(intent)
                finish()
            }
            else {
                binding.textView.text= getString(R.string.welcome_wrong)
                binding.textView.setTextColor(getColor(R.color.md_theme_error))
            }
        }
    }
}
