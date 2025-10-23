package com.example.counterelement.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.counterboard.data.Element
import com.example.counterboard.databinding.ElementBoardItemBinding

class ElementAdapter(
    var items: List<Element>,
    val onClickListener: (Int) -> Unit,
    val onAddPointsListener: (Int) -> Unit,
    val onDeleteListener: (Int) -> Unit
) : RecyclerView.Adapter<ElementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ElementBoardItemBinding.inflate(layoutInflater, parent, false)
        return ElementViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ElementViewHolder, position:Int) {
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

        holder.binding.addElement.setOnClickListener {
            onAddPointsListener
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Element>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class ElementViewHolder(val binding: ElementBoardItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(element: Element) {
        binding.elementTitle.text = element.title
        binding.elementPoints.text = element.points.toString()
    }
}