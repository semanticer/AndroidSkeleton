package cz.leaderboard.app.presentation.board

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import cz.leaderboard.app.R
import cz.leaderboard.app.di.conductorlib.ConductorInjection
import cz.leaderboard.app.domain.board.LeaderboardRecord
import cz.leaderboard.app.presentation.common.BaseController
import cz.leaderboard.app.presentation.common.bindView
import javax.inject.Inject
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View.GONE
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.presentation.common.setRandomDrawable
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by semanticer on 17.06.2017.
 */
class BoardController : BaseController<BoardView, BoardPresenter>(), BoardView {

    internal val listView: RecyclerView by bindView(R.id.record_list)
    internal val addBtn: FloatingActionButton by bindView(R.id.add_btn)
    internal val bottomBar: ViewGroup by bindView(R.id.bottom_bar)

    internal val usernameEdit: EditText by bindView(R.id.username_edit)
    internal val usernameInputLayout: ViewGroup by bindView(R.id.username_input_layout)

    internal val currentUserLayout: ViewGroup by bindView(R.id.current_user_layout)
    internal val username: TextView by bindView(R.id.my_username)
    internal val order: TextView by bindView(R.id.my_order)
    internal val score: TextView by bindView(R.id.my_score)
    internal val avatar: CircleImageView by bindView(R.id.my_avatar)

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
        listView.setHasFixedSize(false)
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = recordAdapter
        RxView.clicks(addBtn).subscribe({boardPresenter.onAddClicked()})
        bottomBar.translationY = 100f

    }

    override fun showRecordData(recordList: List<LeaderboardRecord>) {
        recordAdapter.updateData(recordList)
    }

    override fun showAddScore(addedScore: Int) {
        showError("You've got $addedScore new points")
    }

    override fun showLogin() {
        Log.i("test", "showLogin")
        usernameInputLayout.visibility = VISIBLE
        currentUserLayout.visibility = VISIBLE
        showBottomBarIfHidden()

        RxTextView.editorActionEvents(usernameEdit)
                .doOnNext { usernameEdit.isEnabled = false }
                .map { usernameEdit.text.toString() }
                .filter(String::isNotEmpty)
                .subscribe({presenter.onUserAdded(it)})

    }

    override fun showUser(leaderboardRecord: LeaderboardRecord) {
        Log.i("test", "showUser")
        usernameInputLayout.visibility = GONE
        currentUserLayout.visibility = VISIBLE
        showBottomBarIfHidden()
        username.setText(leaderboardRecord.user.name + "")
        order.text = "#${leaderboardRecord.order}"
        score.text = "${leaderboardRecord.user.score} ${leaderboardRecord.board.units}"
        avatar.setRandomDrawable(leaderboardRecord.user)
    }

    private fun showBottomBarIfHidden() {
        if (bottomBar.visibility != VISIBLE) {
            bottomBar.alpha = 0.2f
            bottomBar.visibility = VISIBLE
            bottomBar.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(600).start()

        }
    }

}