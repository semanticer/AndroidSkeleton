package cz.leaderboard.app.presentation.board

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.domain.board.GetLeaderboardUseCase
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class BoardPresenter @Inject constructor(private val useCase: GetLeaderboardUseCase) : MvpNullObjectBasePresenter<BoardView>() {
    override fun attachView(view: BoardView) {
        super.attachView(view)
        reloadList()
    }

    override fun detachView(retainInstance: Boolean) {
        useCase.dispose()
    }

    class PostListObserver constructor(view: BoardView): PresentationObserver<List<LeaderboardRecord>, BoardView>(view) {
        override fun onNext(list: List<LeaderboardRecord>) {
            onView { it.showRecordData(list) }
        }
    }

    fun reloadList() {
        useCase.execute(PostListObserver(view), GetLeaderboardUseCase.Params("asdsad"))
    }
}