package com.github.leonardoz.kanbapp.view.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Board
import kotlinx.android.synthetic.main.list_item_board.view.*

class BoardsAdapter(
    private val menuCallback: (item: MenuItem?, boardPosition: Int) -> Unit,
    private val openBoardCallback: (Board) -> Unit
) : RecyclerView.Adapter<BoardsAdapter.BoardViewHolder>() {

    var dataset: List<Board> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class BoardViewHolder(
        view: View,
        private val menuCallback: (item: MenuItem?, boardPosition: Int) -> Any,
        val textView: TextView = view.board_title,
        val openBoardButton: AppCompatButton = view.btnOpenBoard,
        val popupButton: ImageButton = view.ib_popup_menu
    ) : RecyclerView.ViewHolder(view),
        PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            menuCallback(item, adapterPosition)
            return false
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_board, parent, false)
        return BoardViewHolder(itemView, menuCallback)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.textView.text = dataset[position].name
        holder.popupButton.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_board_card, popup.menu)
            popup.setOnMenuItemClickListener(holder)
            popup.show()
        }
        holder.openBoardButton.setOnClickListener {
            openBoardCallback(dataset[position])
        }
    }

    override fun getItemCount() = dataset.size


    interface ContextMenuItemCallback {
        fun call(item: MenuItem?, boardPosition: Int)
    }
}