package cz.leaderboard.app.domain.board

import com.google.firebase.auth.FirebaseUser
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class AddScoreUseCase @Inject constructor(val user: FirebaseUser,
                                          val leaderboardRepository: LeaderboardRepository,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)

    : UseCase<Int, AddScoreUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<Int> {
        val exists: Flowable<Boolean> = leaderboardRepository.userExists(user.uid, params.boardId)
        return exists.flatMap {
            if (it) Flowable.just(leaderboardRepository.addScore(params.score, user.uid, params.boardId))
            else Flowable.error<Int> { Throwable("No board user for this user") }
        }.firstElement().toFlowable()
    }


    data class Params(val boardId: String, val score: Int)
}