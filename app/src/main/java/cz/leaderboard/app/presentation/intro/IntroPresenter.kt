package cz.leaderboard.app.presentation.intro

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.domain.board.AddBoardUseCase
import cz.leaderboard.app.presentation.board.IntroView
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class IntroPresenter @Inject constructor(val addBoardUseCase: AddBoardUseCase) : MvpNullObjectBasePresenter<IntroView>() {

    override fun attachView(view: IntroView) {
        super.attachView(view)
    }

    override fun detachView(retainInstance: Boolean) {
        addBoardUseCase.dispose()
    }


    fun onSearchClicked(searchText: String) {
        addBoardUseCase.execute(AddBoardObserver(view), AddBoardUseCase.Params(searchText) )
    }

    class AddBoardObserver constructor(view: IntroView): PresentationObserver<Board, IntroView>(view) {
        override fun onNext(board: Board) {
            onView { it.showFoundBoard() }
        }

        override fun onError(e: Throwable?) {
            onView { it.showSearchError() }
        }
    }

    fun onCreateNewClicked() {
        view.showCreateNew()
    }


}