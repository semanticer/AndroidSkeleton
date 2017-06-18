package cz.leaderboard.app.presentation.board

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.leaderboard.app.R
import cz.leaderboard.app.R.id.order
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.ViewBinder
import cz.leaderboard.app.presentation.common.bindView
import cz.leaderboard.app.presentation.common.setRandomDrawable
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by semanticer on 17.06.2017.
 */
class RecordAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val recordList = mutableListOf<LeaderboardRecord>()
    val topTriad = mutableListOf<LeaderboardRecord>()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return TopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_top_record, parent, false))
        } else {
            return RecordViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == 0) {
            (holder as TopViewHolder).bind(topTriad)
        } else {
            val item = recordList[position - 1]
            val order = position + 3
            (holder as RecordViewHolder).bind(order, item)
        }
    }


    override fun getItemCount(): Int {
        return if (topTriad.isNotEmpty()) 1 + recordList.size else 0
    }

    fun updateData(record: List<LeaderboardRecord>) {

        topTriad.clear()
        recordList.clear()

        for (r in record) {
            if (topTriad.size < 3) {
                topTriad.add(r)
            } else {
                recordList.add(r)
            }
        }
        notifyDataSetChanged()
    }

    class TopViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val firstAvatar: CircleImageView by bindView(R.id.first_avatar)
        internal val firstName: TextView by bindView(R.id.first_name)
        internal val firstScore: TextView by bindView(R.id.first_score)

        internal val secondAvatar: CircleImageView by bindView(R.id.second_avatar)
        internal val secondName: TextView by bindView(R.id.second_name)
        internal val secondScore: TextView by bindView(R.id.second_score)

        internal val thirdAvatar: CircleImageView by bindView(R.id.third_avatar)
        internal val thirdName: TextView by bindView(R.id.third_name)
        internal val thirdScore: TextView by bindView(R.id.third_score)

        init {
            ViewBinder.setup(this, itemView)
        }

        fun bind(topThree: List<LeaderboardRecord>) {
            if (topThree.isNotEmpty()) {
                firstAvatar.setRandomDrawable(topThree[0].user)
                firstName.text = topThree[0].user.name
                firstScore.text = topThree[0].user.score.toString()
            }
            if (topThree.size > 1) {
                secondAvatar.setRandomDrawable(topThree[1].user)
                secondName.text = topThree[1].user.name
                secondScore.text = topThree[1].user.score.toString()
            }
            if (topThree.size > 2) {
                thirdAvatar.setRandomDrawable(topThree[2].user)
                thirdName.text = topThree[2].user.name
                thirdScore.text = topThree[2].user.score.toString()
            }
        }
    }

    class RecordViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val order: TextView by bindView(R.id.order)
        internal val username: TextView by bindView(R.id.username)
        internal val score: TextView by bindView(R.id.score)
        internal val avatar: CircleImageView by bindView(R.id.avatar)

        init {
            ViewBinder.setup(this, itemView)
        }

        fun bind(pos: Int, record: LeaderboardRecord) {
            order.text = "#${pos}"
            username.text = record.user.name
            score.text = record.user.score.toString() + " " + record.board.units
            avatar.setRandomDrawable(record.user)
        }
    }
}


