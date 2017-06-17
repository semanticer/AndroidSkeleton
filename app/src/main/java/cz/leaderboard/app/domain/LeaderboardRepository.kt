package cz.leaderboard.app.domain

import cz.leaderboard.app.domain.board.LeaderboardRecord
import io.reactivex.Flowable

/**
 * Created by semanticer on 17.06.2017.
 */
interface LeaderboardRepository {
    fun getLeaderboard(boardId: String): Flowable<List<LeaderboardRecord>>
}