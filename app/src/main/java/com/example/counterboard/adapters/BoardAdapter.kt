package com.example.counterboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.counterboard.data.Board
import com.example.counterboard.databinding.BoardGalleryItemBinding
class BoardAdapter(
    var items: List<Board>,
    val onClickListener: (Int) -> Unit,
    val onDeleteListener: (Int) -> Unit
) : RecyclerView.Adapter<BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BoardGalleryItemBinding.inflate(layoutInflater, parent, false)
        return BoardViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BoardViewHolder, position:Int) {
        val item = items[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                onDeleteListener(position)
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Board>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class BoardViewHolder(val binding: BoardGalleryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(board: Board) {
        binding.boardNameTextView.text = board.title
    }
}