package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.data.repository.FirebaseLeaderboardRepository
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */

class GetScoresUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)
    : UseCase<Pair<LeaderboardRecord?, List<LeaderboardRecord>>, GetScoresUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<Pair<LeaderboardRecord?, List<LeaderboardRecord>>> {
        val boardId = leaderboardRepository.getCurrentBoard()
        if (boardId.isNullOrBlank()) {
            return Flowable.error { Throwable("No board selected") }
        } else {
            val boardObs = leaderboardRepository.getBoard(boardId!!)
            val usersObs = leaderboardRepository.getUsers(boardId!!)
            return Flowable.combineLatest<Board, List<User>, List<LeaderboardRecord> >(
                    boardObs,
                    usersObs,
                    BiFunction { board, users -> getLeaderboardRecords(board, users) }
            ).map { leaderboardRecords ->
                Pair(
                    getCurrentUserRecord(leaderboardRepository.getCurrentUserId(), leaderboardRecords),
                    leaderboardRecords
                )
            }
        }
    }

    private fun getLeaderboardRecords(board: Board, users: List<User>): List<LeaderboardRecord> {
        return users.map { LeaderboardRecord(it, board) }.reversed()
    }

    private fun getCurrentUserRecord(userId: String?, records: List<LeaderboardRecord>): LeaderboardRecord? {
        var myOrder = -1
        for ((order, rec) in records.withIndex()){
            if (rec.user.id == userId) {
                myOrder = order + 1
            }
        }
        if (userId != null) {
            return records.find { it.user.id == userId }!!.copy(order = myOrder)
        } else return null
    }

    class Params
}