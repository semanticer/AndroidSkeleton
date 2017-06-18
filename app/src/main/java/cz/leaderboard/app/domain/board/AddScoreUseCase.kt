package cz.leaderboard.app.domain.board

import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class AddScoreUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)
    : UseCase<Int, AddScoreUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<Int> {
        val userId = leaderboardRepository.getCurrentUserId()
        val boardId = leaderboardRepository.getCurrentBoard()
        if (userId == null || boardId == null) {
            return Flowable.error<Int> { Throwable("No user currently logged in or board selected") }
        } else {
            return Flowable.just(leaderboardRepository.addScore(params.score, userId, boardId)).firstElement().toFlowable()
        }
    }


    data class Params(val score: Int)
}