package cz.leaderboard.app.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import cz.leaderboard.app.R
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.presentation.board.BoardController
import cz.leaderboard.app.presentation.board.BoardPresenter
import cz.leaderboard.app.presentation.board.IntroController
import cz.leaderboard.app.presentation.common.ActionBarProvider
import dagger.android.AndroidInjection
import javax.inject.Inject
import android.support.v4.app.NavUtils
import android.view.MenuItem


open class MainActivity : AppCompatActivity(), ActionBarProvider {

    private lateinit var router: Router

    private lateinit var controllerContainer: ViewGroup

    @Inject lateinit var leaderboardRepository: LeaderboardRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        setContentView(R.layout.activity_main)
        controllerContainer = findViewById(R.id.controller_container) as ViewGroup

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            if (!leaderboardRepository.getCurrentBoard().isNullOrBlank()) {
                router.setRoot(RouterTransaction.with(BoardController()))
            } else {
                showIntro()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        val homeId = android.R.id.home
        when (itemId) {
        // Respond to the action bar's Up/Home button
            homeId -> {
                showIntro()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showIntro() {
        router.setRoot(RouterTransaction.with(IntroController()))
        leaderboardRepository.setCurrentBoard(null)
        leaderboardRepository.setCurrentUser(null)
    }


    override fun onBackPressed() {
        showIntro()
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
