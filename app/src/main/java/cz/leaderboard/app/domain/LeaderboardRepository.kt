package cz.leaderboard.app.domain

import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.board.LeaderboardRecord
import io.reactivex.Flowable

/**
 * Created by semanticer on 17.06.2017.
 */
interface LeaderboardRepository {
    fun getUsers(boardId: String): Flowable<List<User>>
}