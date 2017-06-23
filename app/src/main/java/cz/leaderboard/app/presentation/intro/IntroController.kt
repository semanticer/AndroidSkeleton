package cz.leaderboard.app.presentation.board

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import cz.leaderboard.app.R
import cz.leaderboard.app.di.conductorlib.ConductorInjection
import cz.leaderboard.app.presentation.common.BaseController
import cz.leaderboard.app.presentation.common.bindView
import cz.leaderboard.app.presentation.intro.IntroPresenter
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.GridLayoutManager
import android.text.InputType
import android.widget.EditText
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.presentation.intro.BoardAdapter


/**
 * Created by semanticer on 17.06.2017.
 */
class IntroController : BaseController<IntroView, IntroPresenter>(), IntroView {

    internal val boardList: RecyclerView by bindView(R.id.board_list)
    internal val searchBtn: Button by bindView(R.id.search_btn)
    internal val searchEdit: EditText by bindView(R.id.search_edit)
    internal val startNewBtn: Button by bindView(R.id.start_new_btn)


    @Inject lateinit var introPresenter: IntroPresenter

    val boardAdapter = BoardAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        ConductorInjection.inject(this)
        return super.onCreateView(inflater, container)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_intro, container, false)
    }

    override fun createPresenter(): IntroPresenter = introPresenter

    override fun onViewBind(view: View) {

        searchEdit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        RxView.clicks(searchBtn)
                .map{ _ -> searchEdit.text.toString() }
                .subscribe({introPresenter.onSearchClicked(it)})

        RxView.clicks(startNewBtn).subscribe({ showCreateNew()})

        boardList.setHasFixedSize(false)
        boardList.layoutManager = GridLayoutManager(activity, 1)
        boardList.adapter = boardAdapter
        boardAdapter.listener = { introPresenter.onSearchClicked(it.public_code)}


    }

    override fun showCreateNew() {
        val url = "http://kapoard.com/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun showFoundBoard(board: Board) {
        router.pushController(RouterTransaction.with(BoardController.newInstance(board.id))
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

    override fun showSearchError() {
        showError("Sorry, We didn't found this board")
    }

    override fun showTopBoards(boards: List<Board>) {
        boardAdapter.updateData(boards)
    }
}