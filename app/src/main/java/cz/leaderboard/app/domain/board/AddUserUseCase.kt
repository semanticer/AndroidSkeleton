package cz.leaderboard.app.domain.board

import com.google.firebase.auth.FirebaseUser
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
class AddUserUseCase @Inject constructor(
                                        val user: FirebaseUser,
                                        val leaderboardRepository: LeaderboardRepository,
                                         threadExecutor: ThreadExecutor,
                                         postExecutionThread: PostExecutionThread)
    : UseCase<User, AddUserUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Flowable<User> {

        return leaderboardRepository.addUser(user.uid, params.username, params.boardId)
                .flatMap { newUserId -> leaderboardRepository.getUser(newUserId, params.boardId) }
                .firstElement().toFlowable()


    }

    data class Params(val boardId: String, val username: String)
}