package cz.leaderboard.app.presentation.intro

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import cz.leaderboard.app.R
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.presentation.common.ViewBinder
import cz.leaderboard.app.presentation.common.bindView

/**
 * Created by semanticer on 18.06.2017.
 */
class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    val boardList = mutableListOf<Board>()
    private var _listener: ((Board) -> Unit)? = null

    var listener: ((Board) -> Unit)?
        get() = _listener
        set(value) {
            _listener = value
        }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = boardList[position]
        holder.bind(board)
        holder.itemView.setOnClickListener { v -> listener?.let { it.invoke(board) } }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_board, parent, false)
        return BoardAdapter.BoardViewHolder(view)
    }

    fun updateData(boards: List<Board>) {
        boardList.clear()
        boardList.addAll(boards)
        notifyDataSetChanged()
    }

    class BoardViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val title: TextView by bindView(R.id.title)
        internal val description: TextView by bindView(R.id.description)
        internal val image: ImageView by bindView(R.id.image)

        init {
            ViewBinder.setup(this, itemView)
        }

        fun bind(board: Board) {
            title.text = board.title
            description.text = board.description
            Glide.with(description.context).load(board.img).into(image)
        }
    }
}