package cz.leaderboard.app.presentation.board

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import cz.leaderboard.app.R
import cz.leaderboard.app.di.conductorlib.ConductorInjection
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.BaseController
import cz.leaderboard.app.presentation.common.bindView
import javax.inject.Inject

/**
 * Created by semanticer on 17.06.2017.
 */
class BoardController : BaseController<BoardView, BoardPresenter>(), BoardView {

    internal val listView: RecyclerView by bindView(R.id.record_list)

    @Inject lateinit var boardPresenter: BoardPresenter

    val recordAdapter = RecordAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        ConductorInjection.inject(this)
        return super.onCreateView(inflater, container)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_board, container, false)
    }

    override fun createPresenter(): BoardPresenter = boardPresenter

    override fun onViewBind(view: View) {
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = recordAdapter
    }

    override fun showRecordData(recordList: List<LeaderboardRecord>) {
        recordAdapter.updateData(recordList)
    }



}