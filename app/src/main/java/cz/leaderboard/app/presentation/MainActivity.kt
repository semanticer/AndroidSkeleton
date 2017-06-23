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
import cz.leaderboard.app.presentation.board.IntroController
import cz.leaderboard.app.presentation.common.ActionBarProvider
import dagger.android.AndroidInjection
import javax.inject.Inject
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

open class MainActivity : AppCompatActivity(), ActionBarProvider {

    private val RC_SIGN_IN = 123
    private val TAG = "MainActivity"

    private lateinit var router: Router

    private lateinit var controllerContainer: ViewGroup

    @Inject lateinit var leaderboardRepository: LeaderboardRepository

    val auth = FirebaseAuth.getInstance()!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        setContentView(R.layout.activity_main)
        controllerContainer = findViewById(R.id.controller_container) as ViewGroup

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            if (auth.currentUser == null) {
                signInAnon()
            }
            showIntro()
        }
    }

    private fun signInAnon() {
        auth.signInAnonymously()
                .addOnCompleteListener(this, { task ->
                    Log.d(TAG, "signInAnonymously:done")
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)

                    }

                })
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
    }


    override fun onBackPressed() {
        if (router.backstack.isEmpty()) {
            super.onBackPressed()
        } else {
            showIntro()
        }
    }
}
