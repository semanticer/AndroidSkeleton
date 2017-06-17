package cz.leaderboard.app.presentation.board

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.leaderboard.app.R
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.ViewBinder
import cz.leaderboard.app.presentation.common.bindView

/**
 * Created by semanticer on 17.06.2017.
 */
class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    val recordList = mutableListOf<LeaderboardRecord>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val post = recordList[position]
        holder.bind(position, post)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    fun updateData(posts: List<LeaderboardRecord>) {
        recordList.clear()
        recordList.addAll(posts)
        notifyDataSetChanged()
    }

    class RecordViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val order: TextView by bindView(R.id.order)
        internal val username: TextView by bindView(R.id.username)
        internal val score: TextView by bindView(R.id.score)

        init {
            ViewBinder.setup(this, itemView)
        }

        fun bind(pos: Int, record: LeaderboardRecord) {
            order.text = pos.toString()
            username.text = record.user.name
            score.text = record.value.toString() + " " + record.unit
        }
    }
}