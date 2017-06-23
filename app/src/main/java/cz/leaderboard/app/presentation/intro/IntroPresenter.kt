package cz.leaderboard.app.presentation.intro

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.domain.board.AddBoardUseCase
import cz.leaderboard.app.domain.board.GetTopBoardsUseCase
import cz.leaderboard.app.presentation.board.IntroView
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class IntroPresenter @Inject constructor(val addBoardUseCase: AddBoardUseCase, val getTopBoardsUseCase: GetTopBoardsUseCase) : MvpNullObjectBasePresenter<IntroView>() {

    override fun attachView(view: IntroView) {
        super.attachView(view)
        getTopBoardsUseCase.execute(TopBoardsObserver(view), GetTopBoardsUseCase.Params())
    }

    override fun detachView(retainInstance: Boolean) {
        addBoardUseCase.dispose()
        getTopBoardsUseCase.dispose()
    }


    fun onSearchClicked(searchText: String) {
        addBoardUseCase.execute(AddBoardObserver(view), AddBoardUseCase.Params(searchText) )
    }

    class AddBoardObserver constructor(view: IntroView): PresentationObserver<Board, IntroView>(view) {
        override fun onNext(boards: Board) {
            onView { it.showFoundBoard(boards) }
        }

        override fun onError(e: Throwable?) {
            onView { it.showSearchError() }
        }
    }


    class TopBoardsObserver constructor(view: IntroView): PresentationObserver<List<Board>, IntroView>(view) {
        override fun onNext(boards: List<Board>) {
            onView { it.showTopBoards(boards) }
        }
    }




}