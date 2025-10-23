package com.example.counterboard.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counterboard.R
import com.example.counterboard.data.Board
import com.example.counterboard.data.BoardDAO
import com.example.counterboard.data.Element
import com.example.counterboard.data.ElementDAO
import com.example.counterboard.databinding.ActivityBoardBinding
import com.example.counterelement.adapters.ElementAdapter
import com.google.android.material.snackbar.Snackbar


class BoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardBinding
    lateinit var adapter: ElementAdapter
    lateinit var boardDAO: BoardDAO
    lateinit var elementDAO: ElementDAO
    lateinit var board: Board
    lateinit var element: Element

    var elementList: List<Element> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBoardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var boardId: Int? = intent.extras?.getInt("BOARD_ID")


        boardDAO = BoardDAO(this)
        elementDAO = ElementDAO(this)
        board = boardDAO.findById(boardId)!!

        binding.boardTitle.text= board.title


        binding.newElementButtom.setOnClickListener {
            var querty : String = ""
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(getString(R.string.new_element))
            val input = EditText(this)
            dialog.setView(input)

            dialog.setPositiveButton(getString(R.string.save), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    querty = input.getText().toString()
                    elementDAO.insert(querty, boardId)
                    loadData()
                }
            })
            dialog.setNegativeButton(getString(R.string.cancel), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.cancel()
                    loadData()
                }
            })

            dialog.show()

        }

        adapter = ElementAdapter(
            items = elementList,
            { position ->
                //TODO implement dialog for cange name

            },
            { position ->
                val element = elementList[position]
                var actualPoints= elementList[position].points
                actualPoints= ++actualPoints
                Log.i("tag",actualPoints.toString())
                elementDAO.update(element,actualPoints,boardId)
                loadData()

            },
            { position ->
                val element = elementList[position]

                val dialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_text) + "${element.title}?")
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        elementDAO.delete(element.id)
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
        Log.i("Element",board.toString())
        elementList = elementDAO.findAllByBoard(board)
        Log.i("Element",elementList.toString())
        adapter.updateItems(elementList)
    }
}
