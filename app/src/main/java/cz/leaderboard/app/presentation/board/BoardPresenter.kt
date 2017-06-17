package cz.leaderboard.app.presentation.board

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.domain.board.AddScoreUseCase
import cz.leaderboard.app.domain.board.GetScoresUseCase
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class BoardPresenter @Inject constructor(private val getScoresUseCase: GetScoresUseCase, private val addScoresUseCase: AddScoreUseCase) : MvpNullObjectBasePresenter<BoardView>() {
    override fun attachView(view: BoardView) {
        super.attachView(view)
        reloadList()
    }

    override fun detachView(retainInstance: Boolean) {
        getScoresUseCase.dispose()
        addScoresUseCase.dispose()
    }

    fun reloadList() {
        getScoresUseCase.execute(ScoresObserver(view), GetScoresUseCase.Params())
    }

    fun onAddClicked() {
        addScoresUseCase.execute(AddScoreObserver(view), AddScoreUseCase.Params(1))
    }

    class ScoresObserver constructor(view: BoardView): PresentationObserver<List<LeaderboardRecord>, BoardView>(view) {
        override fun onNext(list: List<LeaderboardRecord>) {
            onView { it.showRecordData(list) }
        }
    }

    class AddScoreObserver constructor(view: BoardView): PresentationObserver<Int, BoardView>(view) {
        override fun onNext(addedScore: Int) {
            onView { it.showAddScore(addedScore) }
        }
    }


}