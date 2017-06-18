package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class AddBoardUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)
    : UseCase<Board, AddBoardUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<Board> {
        return leaderboardRepository.findBoard(params.publicCode)
                .doOnNext({ board -> leaderboardRepository.setCurrentBoard(board.id) })
    }

    data class Params(val publicCode: String)
}