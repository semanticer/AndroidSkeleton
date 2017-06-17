package cz.leaderboard.app.presentation.intro

import com.hannesdorfmann.mosby3.mvp.MvpNullObjectBasePresenter
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.board.IntroView
import cz.leaderboard.app.presentation.common.PresentationObserver
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class IntroPresenter @Inject constructor() : MvpNullObjectBasePresenter<IntroView>() {

    override fun attachView(view: IntroView) {
        super.attachView(view)
    }

    override fun detachView(retainInstance: Boolean) {
    }

    class PostListObserver constructor(view: IntroView): PresentationObserver<List<LeaderboardRecord>, IntroView>(view) {
        override fun onNext(list: List<LeaderboardRecord>) {
            onView { /* todo */ }
        }
    }

    fun onSearchClicked(searchText: String) {

    }

    fun onCreateNewClicked() {
        view.showCreateNew()
    }


}