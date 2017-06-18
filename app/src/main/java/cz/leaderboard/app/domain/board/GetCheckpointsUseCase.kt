package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.Checkpoint
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 18.06.2017.
 */
class GetCheckpointsUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                                threadExecutor: ThreadExecutor,
                                                postExecutionThread: PostExecutionThread)
    : UseCase<List<Checkpoint>, GetCheckpointsUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: GetCheckpointsUseCase.Params): Flowable<List<Checkpoint>> {
        val currentBoard = leaderboardRepository.getCurrentBoard()
        val currentUser = leaderboardRepository.getCurrentUserId()
        if (currentBoard.isNullOrBlank() || currentUser.isNullOrBlank()) {
            return Flowable.error<List<Checkpoint>> { Throwable("No board selected or user logged in") }
        } else {
            return leaderboardRepository.getCheckpoint(currentBoard!!)
        }
    }

    class Params


}