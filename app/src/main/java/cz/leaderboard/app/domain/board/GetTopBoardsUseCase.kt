package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 18.06.2017.
 */

class GetTopBoardsUseCase @Inject internal constructor(val leaderboardRepository: LeaderboardRepository,
                                                       threadExecutor: ThreadExecutor,
                                                       postExecutionThread: PostExecutionThread)
    : UseCase<List<Board>, GetTopBoardsUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<List<Board>> {
        return leaderboardRepository.getTopBoards()
    }

    class Params
}