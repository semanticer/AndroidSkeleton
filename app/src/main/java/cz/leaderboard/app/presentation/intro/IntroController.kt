package cz.leaderboard.app.presentation.board

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import cz.leaderboard.app.R
import cz.leaderboard.app.di.conductorlib.ConductorInjection
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.BaseController
import cz.leaderboard.app.presentation.common.bindView
import cz.leaderboard.app.presentation.intro.IntroPresenter
import javax.inject.Inject
import android.content.Intent
import android.net.Uri


/**
 * Created by semanticer on 17.06.2017.
 */
class IntroController : BaseController<IntroView, IntroPresenter>(), IntroView {

    internal val boardList: RecyclerView by bindView(R.id.board_list)
    internal val searchBtn: Button by bindView(R.id.search_btn)
    internal val searchEdit: Button by bindView(R.id.search_edit)
    internal val startNewBtn: Button by bindView(R.id.start_new_btn)


    @Inject lateinit var introPresenter: IntroPresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        ConductorInjection.inject(this)
        return super.onCreateView(inflater, container)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_intro, container, false)
    }

    override fun createPresenter(): IntroPresenter = introPresenter

    override fun onViewBind(view: View) {
        RxView.clicks(searchBtn)
                .map{ _ -> searchEdit.text.toString() }
                .subscribe({introPresenter.onSearchClicked(it)})

        RxView.clicks(searchBtn).subscribe({ introPresenter.onCreateNewClicked()})
    }

    override fun showCreateNew() {
        val url = "http://www.google.com"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun showFoundBoard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSearchError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTopBoards() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}