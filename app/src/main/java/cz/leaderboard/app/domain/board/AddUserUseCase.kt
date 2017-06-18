package cz.leaderboard.app.domain.board

import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.common.PostExecutionThread
import cz.leaderboard.app.domain.common.ThreadExecutor
import cz.leaderboard.app.domain.common.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by semanticer on 18.06.2017.
 */
class AddUserUseCase @Inject constructor(val leaderboardRepository: LeaderboardRepository,
                                         threadExecutor: ThreadExecutor,
                                         postExecutionThread: PostExecutionThread)
    : UseCase<User, AddUserUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<User> {
        val boardId = leaderboardRepository.getCurrentBoard()
        if (boardId == null) {
            return Flowable.error<User> { Throwable("No board selected") }
        } else {
            return leaderboardRepository.addUser(params.username, boardId)
                    .doOnNext { newUserId -> leaderboardRepository.setCurrentUser(newUserId)}
                    .flatMap { newUserId -> leaderboardRepository.getUser(newUserId, boardId) }
                    .firstElement().toFlowable()

        }
    }


    data class Params(val username: String)
}