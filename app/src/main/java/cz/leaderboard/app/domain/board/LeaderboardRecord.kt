package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.Record
import cz.leaderboard.app.data.model.User

/**
 * Created by semanticer on 17.06.2017.
 */
data class LeaderboardRecord(
    val user: User,
    val unit: String
)