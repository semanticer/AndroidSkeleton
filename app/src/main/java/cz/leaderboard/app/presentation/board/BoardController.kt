package cz.leaderboard.app.presentation.board

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
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
import android.view.View.GONE
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import cz.leaderboard.app.presentation.common.setRandomDrawable
import de.hdodenhof.circleimageview.CircleImageView
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem


/**
 * Created by semanticer on 17.06.2017.
 */
class BoardController(args: Bundle) : BaseController<BoardView, BoardPresenter>(args), BoardView {


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
    internal val toolbar: Toolbar by bindView(R.id.toolbar)

    @Inject lateinit var boardPresenter: BoardPresenter

    val recordAdapter = RecordAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        ConductorInjection.inject(this)
        setHasOptionsMenu(true)
        boardPresenter.boardId = args.getString(ARG_BOARD_ID)
        return super.onCreateView(inflater, container)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_board, container, false)
    }

    override fun createPresenter(): BoardPresenter = boardPresenter

    override fun onViewBind(view: View) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        listView.setHasFixedSize(false)
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = recordAdapter
        RxView.clicks(addBtn).subscribe({boardPresenter.onAddClicked()})
        bottomBar.translationY = 100f

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return activity?.onOptionsItemSelected(item)!!
    }

    override fun showRecordData(recordList: List<LeaderboardRecord>) {
        if (recordList.isNotEmpty()) {
            (activity as AppCompatActivity).supportActionBar?.title = recordList.first().board.title
            toolbar.title  = recordList.first().board.title
        }
        recordAdapter.updateData(recordList)
    }

    override fun showAddScore(addedScore: Int) {
        showError("You've got $addedScore new points")
    }

    override fun showLogin() {
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

    override fun showQrReader() {
        try {
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0)
        } catch (e: Exception) {

            val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val contents = data.getStringExtra("SCAN_RESULT")
            boardPresenter.onQrScanned(contents)
        }
        if(resultCode == RESULT_CANCELED){
            //handle cancel
        }

    }

    companion object  {
        val ARG_BOARD_ID = "arg_board_id"
        fun newInstance(boardId: String): BoardController {
            val args = Bundle()
            args.putString(ARG_BOARD_ID, boardId)
            return BoardController(args)
        }
    }



}