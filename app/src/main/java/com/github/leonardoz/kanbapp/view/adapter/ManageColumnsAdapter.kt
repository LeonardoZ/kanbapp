package com.github.leonardoz.kanbapp.view.adapter

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.view.adapter.dragndrop.ItemTouchHelperAdapter
import com.github.leonardoz.kanbapp.view.adapter.dragndrop.ItemTouchHelperViewHolder
import kotlinx.android.synthetic.main.list_item_column_card.view.*
import java.util.ArrayList

class ManageColumnsAdapter(
    private val callbacks: CardCallbacks
) : RecyclerView.Adapter<ManageColumnsAdapter.ColumnViewHolder>(), ItemTouchHelperAdapter {

    private var dataSet: MutableList<Column> = ArrayList()

    class ColumnViewHolder(
        view: View,
        val titleTextView: TextView = view.columnTitle,
        val typeTextView: TextView = view.columnType,
        val removeButton: AppCompatButton = view.btnRemoveColumn,
        val editButton: AppCompatButton = view.btnEditColumn
    ) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    fun updateField(newData: MutableList<Column>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_column_card, parent, false)
        return ColumnViewHolder(itemView)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle: Bundle = payloads[0] as Bundle
            val data = dataSet[position]
            bundle.keySet().forEach {
                when (it) {
                    KEY_TITLE -> holder.titleTextView.text = data.title
                }
            }
        }
    }

    companion object {
        const val KEY_TITLE = "KEY_TITLE"
    }

    override fun getItemId(position: Int): Long = dataSet[position].id

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        val column = dataSet[holder.adapterPosition]
        val type = when (column.type) {
            TODO_COLUMN -> "TODO"
            DONE_COLUMN -> "DONE"
            WORK_COLUMN -> "WORK"
        }
        holder.titleTextView.text = column.title
        holder.editButton.setOnClickListener { callbacks.onEditClicked(column) }
        holder.typeTextView.text = type

        when (column.type) {
            WORK_COLUMN -> holder.removeButton.setOnClickListener {
                callbacks.onRemoveClicked(column)
            }
            else -> {
                holder.removeButton.visibility = View.GONE
            }
        }
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val first = 0
        val last = dataSet.size - 1
        if (fromPosition == first || fromPosition == last
            || toPosition == first || toPosition == last
        ) {
            return
        }
        val from = dataSet[fromPosition]
        val to = dataSet[toPosition]

        // swap positions
        val aux = from.order
        from.order = to.order
        to.order = aux

        notifyItemMoved(fromPosition, toPosition)
        callbacks.onReorder(from, to)
    }

    interface CardCallbacks {
        fun onEditClicked(column: Column)
        fun onRemoveClicked(column: Column)
        fun onReorder(columnA: Column, columnB: Column)
    }

}