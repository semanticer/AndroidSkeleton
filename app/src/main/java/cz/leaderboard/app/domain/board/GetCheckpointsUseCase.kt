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
class GetCheckpointsUseCase @Inject constructor(
                                                val leaderboardRepository: LeaderboardRepository,
                                                threadExecutor: ThreadExecutor,
                                                postExecutionThread: PostExecutionThread)
    : UseCase<List<Checkpoint>, GetCheckpointsUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: GetCheckpointsUseCase.Params): Flowable<List<Checkpoint>> {
        return leaderboardRepository.getCheckpoint(params.boardId)
    }

    class Params(val boardId: String)


}