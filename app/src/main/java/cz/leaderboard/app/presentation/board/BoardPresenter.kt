package cz.leaderboard.app.presentation.board

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.board.AddScoreUseCase
import cz.leaderboard.app.domain.board.AddUserUseCase
import cz.leaderboard.app.domain.board.GetScoresUseCase
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class BoardPresenter @Inject constructor(
        private val getScoresUseCase: GetScoresUseCase,
        private val addScoresUseCase: AddScoreUseCase,
        private val addUserUseCase: AddUserUseCase
    ) : MvpNullObjectBasePresenter<BoardView>() {

    override fun attachView(view: BoardView) {
        super.attachView(view)
        reloadList()
    }

    override fun detachView(retainInstance: Boolean) {
        getScoresUseCase.dispose()
        addScoresUseCase.dispose()
        addUserUseCase.dispose()
    }

    fun reloadList() {
        getScoresUseCase.execute(ScoresObserver(view), GetScoresUseCase.Params())
    }

    fun onAddClicked() {
        addScoresUseCase.execute(AddScoreObserver(view), AddScoreUseCase.Params(1))
    }

    fun onUserAdded(username: String) {
        addUserUseCase.execute(AddUserObserver(view), AddUserUseCase.Params(username))
    }

    class ScoresObserver constructor(view: BoardView): PresentationObserver<Pair<LeaderboardRecord?, List<LeaderboardRecord>>, BoardView>(view) {
        override fun onNext(scores: Pair<LeaderboardRecord?, List<LeaderboardRecord>>) {
            val (currentUsersLeaderboardRecord, leaderRecordList) = scores
            onView { it.showRecordData(leaderRecordList) }
            if (currentUsersLeaderboardRecord != null) {
                onView { it.showUser(currentUsersLeaderboardRecord) }
            }
        }
    }

    class AddScoreObserver constructor(view: BoardView): PresentationObserver<Int, BoardView>(view) {
        override fun onNext(addedScore: Int) {
            onView { it.showAddScore(addedScore) }
        }

        override fun onError(e: Throwable?) {
            onView { it.showLogin() }
        }
    }

    class AddUserObserver constructor(view: BoardView): PresentationObserver<User, BoardView>(view) {
        override fun onNext(user: User) {
            onView { }
        }
    }




}