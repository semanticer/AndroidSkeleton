package cz.leaderboard.app.domain

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.data.model.Checkpoint
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
    fun userExists(userId: String, boardId: String): Flowable<Boolean>
    fun addScore(score: Int, userId: String, board: Board, checkpointId: String? = null): String
    fun getBoard(boardId: String): Flowable<Board>
    fun findBoard(publicCode: String): Flowable<Board>

    fun addUser(userId: String, username: String, boardId: String): Flowable<String>
    fun getTopBoards(): Flowable<List<Board>>
    fun getCheckpoint(currentBoard: String): Flowable<List<Checkpoint>>
}