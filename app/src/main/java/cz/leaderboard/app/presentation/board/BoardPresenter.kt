package cz.leaderboard.app.presentation.board

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.data.model.Checkpoint
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.board.*
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class BoardPresenter @Inject constructor(
        private val getScoresUseCase: GetScoresUseCase,
        private val addScoresUseCase: AddScoreUseCase,
        private val addUserUseCase: AddUserUseCase,
        private val getCheckpointsUseCase: GetCheckpointsUseCase
    ) : MvpNullObjectBasePresenter<BoardView>() {

    private var currentCheckpoits: List<Checkpoint> = listOf()
    lateinit var boardId: String

    override fun attachView(view: BoardView) {
        super.attachView(view)
        reloadList()
    }

    override fun detachView(retainInstance: Boolean) {
        getScoresUseCase.dispose()
        addScoresUseCase.dispose()
        addUserUseCase.dispose()
        getCheckpointsUseCase.dispose()
    }

    fun reloadList() {
        getScoresUseCase.execute(ScoresObserver(view), GetScoresUseCase.Params(boardId))
    }

    fun onAddClicked() {
        getCheckpointsUseCase.execute(CheckpointObserver(view), GetCheckpointsUseCase.Params(boardId))
    }

    fun onUserAdded(username: String) {
        addUserUseCase.execute(AddUserObserver(view), AddUserUseCase.Params(boardId, username))
    }



    inner class CheckpointObserver constructor(view: BoardView): PresentationObserver<List<Checkpoint>, BoardView>(view) {
        override fun onNext(checkpoits: List<Checkpoint>) {
            currentCheckpoits = checkpoits
            if (checkpoits.isNotEmpty()) {
                view.showQrReader()
            } else {
                addScoresUseCase.execute(AddScoreObserver(view), AddScoreUseCase.Params(boardId, 1))
            }
        }

        override fun onError(e: Throwable?) {
            onView { it.showLogin() }
        }
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

    class AddScoreObserver constructor(view: BoardView): PresentationObserver<String, BoardView>(view) {
        override fun onNext(addedScore: String) {
            onView { /*it.showAddScore(addedScore)*/ }
        }

        override fun onError(e: Throwable?) {
            onView { it.showLogin() }
        }
    }

    inner class AddUserObserver constructor(view: BoardView): PresentationObserver<User, BoardView>(view) {
        override fun onNext(user: User) {
            onView { onAddClicked() }
        }
    }

    fun onQrScanned(contents: String?) {
        if (contents != null) {
            val chosenCheckpointOrNull = currentCheckpoits.filter { it.code == contents }.firstOrNull()
            if (chosenCheckpointOrNull != null) {
                addScoresUseCase.execute(AddScoreObserver(view), AddScoreUseCase.Params(boardId, chosenCheckpointOrNull.score, chosenCheckpointOrNull.id))
            }
        }
    }
}