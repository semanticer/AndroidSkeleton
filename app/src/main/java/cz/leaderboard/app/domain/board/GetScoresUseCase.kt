package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.repository.FirebaseLeaderboardRepository
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */

class GetScoresUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)
    : UseCase<List<LeaderboardRecord>, GetScoresUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<List<LeaderboardRecord>> {
        val boardId = leaderboardRepository.getCurrentBoard()
        if (boardId == null) {
            return Flowable.error { Throwable("No board selected") }
        } else {
            return leaderboardRepository.getUsers(boardId).map { users ->
                users.map { LeaderboardRecord(it, "Piv") }.reversed()
            }
        }
    }

    class Params
}