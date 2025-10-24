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
    var addPoints = true
    var deleteMode = false

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
        supportActionBar?.hide()

        var boardId: Int? = intent.extras?.getInt("BOARD_ID")
        binding.addButtom.setBackgroundColor(getColor(R.color.md_theme_primaryFixed))
        binding.restButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
        binding.deleteButtom.setBackgroundColor(getColor(R.color.md_theme_onErrorContainer))

        boardDAO = BoardDAO(this)
        elementDAO = ElementDAO(this)
        board = boardDAO.findById(boardId)!!
        binding.boardTitleText.text = board.title

        binding.addButtom.setOnClickListener {
            if (!addPoints) {
                addPoints = true
                deleteMode = false

                binding.addButtom.setBackgroundColor(getColor(R.color.md_theme_primaryFixed))
                binding.restButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
                binding.deleteButtom.setBackgroundColor(getColor(R.color.md_theme_onErrorContainer))
            }
        }
        binding.restButtom.setOnClickListener {
            if (addPoints) {
                addPoints = false
                deleteMode = false
                binding.restButtom.setBackgroundColor(getColor(R.color.md_theme_primaryFixed))
                binding.addButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
                binding.deleteButtom.setBackgroundColor(getColor(R.color.md_theme_onErrorContainer))
            } else {
                deleteMode = false
                binding.restButtom.setBackgroundColor(getColor(R.color.md_theme_primaryFixed))
                binding.addButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
                binding.deleteButtom.setBackgroundColor(getColor(R.color.md_theme_onErrorContainer))
            }
        }
        binding.deleteButtom.setOnClickListener {

            if (!deleteMode) {
                addPoints = false
                deleteMode = true
                binding.restButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
                binding.addButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
                binding.deleteButtom.setBackgroundColor(getColor(R.color.md_theme_errorContainer))
            } else {
                deleteMode = false
                addPoints = true
                binding.restButtom.setBackgroundColor(getColor(R.color.md_theme_primaryFixed))
                binding.addButtom.setBackgroundColor(getColor(R.color.md_theme_primary))
                binding.deleteButtom.setBackgroundColor(getColor(R.color.md_theme_onErrorContainer))
            }

        }

        binding.backButtom.setOnClickListener { finish() }

        binding.boardTitle.setOnClickListener {
            var querty: String = ""
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(getString(R.string.edit_board) + " " + board.title)
            val input = EditText(this)
            input.hint= board.title
            dialog.setView(input)

            dialog.setPositiveButton(
                getString(R.string.save),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        querty = input.getText().toString()
                        board.title = querty
                        boardDAO.update(board)
                        binding.boardTitleText.text= board.title
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
        }

        binding.newElementButtom.setOnClickListener {
            var querty: String = ""
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(getString(R.string.new_element))
            val input = EditText(this)
            dialog.setView(input)

            dialog.setPositiveButton(
                getString(R.string.save),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        querty = input.getText().toString()
                        elementDAO.insert(querty, boardId)
                        loadData()
                    }
                })
            dialog.setNegativeButton(
                getString(R.string.cancel),
                object : DialogInterface.OnClickListener {
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
                val element = elementList[position]
                if (addPoints) {
                    element.points = ++element.points
                    elementDAO.update(element)
                    loadData()
                } else if (!addPoints && !deleteMode) {
                    element.points = --element.points
                    elementDAO.update(element)
                    loadData()
                } else if (!addPoints && deleteMode) {
                    deleteElement(position)
                    loadData()
                }

            },
            { position ->
                var element = elementList[position]
                var querty: String = ""
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle(getString(R.string.edit_element) + " " + element.title)
                val input = EditText(this)
                input.hint= element.title
                dialog.setView(input)

                dialog.setPositiveButton(
                    getString(R.string.save),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            querty = input.getText().toString()
                            element.title = querty
                            elementDAO.update(element)
                            loadData()
                        }
                    })
                dialog.setNegativeButton(
                    getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            dialog.cancel()
                            loadData()
                        }
                    })

                dialog.show()
            })

        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        Log.i("Element", board.toString())
        elementList = elementDAO.findAllByBoard(board)
        Log.i("Element", elementList.toString())
        adapter.updateItems(elementList)
    }

    fun deleteElement(position: Int) {
        val element = elementList[position]

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_text) + " ${element.title}?")
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
}
