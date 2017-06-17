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

class GetLeaderboardUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                                threadExecutor: ThreadExecutor,
                                                postExecutionThread: PostExecutionThread)
    : UseCase<List<LeaderboardRecord>, GetLeaderboardUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<List<LeaderboardRecord>> {
        return leaderboardRepository.getUsers(params.boardId).map { users ->
            users.map { LeaderboardRecord(it, "Piv")}.reversed()
        }
    }


    data class Params(val boardId: String)
}