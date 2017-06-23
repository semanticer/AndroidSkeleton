package cz.leaderboard.app.domain.board

import com.google.firebase.auth.FirebaseUser
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class AddScoreUseCase @Inject constructor(val user: FirebaseUser,
                                          val leaderboardRepository: LeaderboardRepository,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)

    : UseCase<String, AddScoreUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<String> {
        val board = leaderboardRepository.getBoard(params.boardId)
        val exists: Flowable<Boolean> = leaderboardRepository.userExists(user.uid, params.boardId)

        return Flowable.zip<Board, Boolean, Pair<Board, Boolean>>(board, exists, BiFunction { b, ue -> Pair(b, ue)} )
                .flatMap { (board, userExists) ->
                    if (userExists) Flowable.just(leaderboardRepository.addScore(params.score, user.uid, board, params.checkpointId))
                    else Flowable.error<String> { Throwable("No board user for this user") }
                }.firstElement().toFlowable()
    }


    data class Params(val boardId: String, val score: Int, val checkpointId: String? = null)
}