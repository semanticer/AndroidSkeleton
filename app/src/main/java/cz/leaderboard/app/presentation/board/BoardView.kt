package cz.leaderboard.app.presentation.board

import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.BaseView

/**
 * Created by semanticer on 17.06.2017.
 */
interface BoardView : BaseView {
    fun showRecordData(recordList: List<LeaderboardRecord>)
}