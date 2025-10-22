package com.example.counterboard.activity


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counterboard.R
import com.example.counterboard.adapters.BoardAdapter
import com.example.counterboard.data.Board
import com.example.counterboard.data.BoardDAO
import com.example.counterboard.data.Element
import com.example.counterboard.data.ElementDAO
import com.example.counterboard.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: BoardAdapter
    lateinit var boardDAO: BoardDAO
    lateinit var elementDAO: ElementDAO
    lateinit var board: Board
    lateinit var element: Element

    var boardList: List<Board> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        boardDAO = BoardDAO(this)
        elementDAO = ElementDAO(this)
        binding.createButtom?.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            startActivity(intent)
        }
        adapter = BoardAdapter(
            items = boardList,
            { position ->

            },
            { position ->
                val category = boardList[position]

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Sure to delete" + "${board.title}?")
                    .setPositiveButton("yes") { dialog, which ->
                        boardDAO.delete(board.id)
                        loadData()
                        Snackbar.make(
                            binding.root,
                            "deleting",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("no", null)
                    .create()
                dialog.show()
            }
        )
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        boardList = boardDAO.findAll()
        adapter.updateItems(boardList)
    }
}