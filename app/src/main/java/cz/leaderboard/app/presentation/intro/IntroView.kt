package cz.leaderboard.app.presentation.board

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.BaseView

/**
 * Created by semanticer on 17.06.2017.
 */
interface IntroView : BaseView {
    fun showCreateNew()
    fun showFoundBoard()
    fun showSearchError()
    fun showTopBoards(boards: List<Board>)
}