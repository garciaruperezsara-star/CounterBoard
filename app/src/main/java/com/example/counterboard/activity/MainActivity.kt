package com.example.counterboard.activity


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counterboard.R
import com.example.counterboard.adapters.BoardAdapter
import com.example.counterboard.data.Board
import com.example.counterboard.data.BoardDAO
import com.example.counterboard.data.ElementDAO
import com.example.counterboard.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: BoardAdapter
    lateinit var boardDAO: BoardDAO
    lateinit var elementDAO: ElementDAO
    lateinit var board: Board
    lateinit var pref: SharedPreferences
    lateinit var username: String
    lateinit var galleryMenu: MenuItem
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
        pref= getSharedPreferences("myPref", Context.MODE_PRIVATE)
        username = pref.getString("username", "Gallery").toString()
        Log.i("username", username.toString())
        boardDAO = BoardDAO(this)
        elementDAO = ElementDAO(this)

        supportActionBar?.title= username+" "+getString(R.string.gallery)

        binding.createButtom.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            startActivity(intent)
        }

        adapter = BoardAdapter(
            items = boardList,
            { position ->
                val item = boardList[position]
                val intent= Intent(this, BoardActivity::class.java)
                intent.putExtra("BOARD_ID",item.id)
                startActivity(intent)
            },
            { position ->
                val board = boardList[position]
                val dialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_text) + " ${board.title}?")
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        boardDAO.delete(board.id)
                        loadData()
                        Snackbar.make(
                            binding.root,
                            getString(R.string.delete),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .create()
                dialog.show()
            }
        )

        binding.recyclerView.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        boardList = boardDAO.findAll()
        adapter.updateItems(boardList)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_gallery_menu,menu)
        galleryMenu = menu.findItem(R.id.change_user_name)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.change_user_name-> {
                var querty: String = ""

                val dialog = AlertDialog.Builder(this)
                dialog.setTitle(getString(R.string.edit_use_name))
                val input = EditText(this)
                input.hint= username
                dialog.setView(input)

                dialog.setPositiveButton(
                    getString(R.string.save),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            querty = input.getText().toString()
                            pref.edit {
                                putString("username", querty)
                            }
                            supportActionBar?.title= querty+" "+getString(R.string.gallery)
                        }
                    })
                dialog.setNegativeButton(
                    getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            dialog.cancel()
                        }
                    })

                dialog.show()
                true
            }

            android.R.id.home->{
                finish()
                true
            }

            else ->super.onOptionsItemSelected(item)
        }
    }

}