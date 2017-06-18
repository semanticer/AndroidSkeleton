package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.data.model.User

/**
 * Created by semanticer on 17.06.2017.
 */
data class LeaderboardRecord(
    val user: User,
    val board: Board,
    val order: Int = -1
)