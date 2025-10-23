package com.example.counterboard.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counterboard.R
import com.example.counterboard.data.Board
import com.example.counterboard.data.BoardDAO
import com.example.counterboard.databinding.ActivityCreateBoardBinding

class CreateBoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBoardBinding
    lateinit var boardDAO: BoardDAO
    lateinit var board : Board
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityCreateBoardBinding.inflate((layoutInflater))
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.setTitle(getString(R.string.new_board))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        boardDAO = BoardDAO(this)
        val querty = binding.textField.text
        binding.saveButton.setOnClickListener {
            board= Board(-1, querty.toString())
            boardDAO.insert(board)
            finish()

        }
    }
}