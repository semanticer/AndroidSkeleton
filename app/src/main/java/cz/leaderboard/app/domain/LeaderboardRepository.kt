package cz.leaderboard.app.domain

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.board.LeaderboardRecord
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by semanticer on 17.06.2017.
 */
interface LeaderboardRepository {
    fun getUsers(boardId: String): Flowable<List<User>>
    fun getUser(userId: String, boardId: String): Flowable<User>
    fun addScore(score: Int, userId: String, boardId: String): Int
    fun getBoard(boardId: String): Flowable<Board>
    fun findBoard(publicCode: String): Flowable<Board>

    fun getCurrentBoard(): String?
    fun getCurrentUserId(): String?
    fun setCurrentBoard(board: String)
    fun setCurrentUser(userId: String)
    fun addUser(username: String, boardId: String): Flowable<String>
}