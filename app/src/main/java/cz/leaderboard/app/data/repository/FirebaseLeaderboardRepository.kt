package cz.leaderboard.app.data.repository

import com.google.firebase.database.FirebaseDatabase
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.board.LeaderboardRecord
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by semanticer on 17.06.2017.
 */
class FirebaseLeaderboardRepository(val firebaseDatabase: FirebaseDatabase) : LeaderboardRepository {
    override fun getLeaderboard(boardId: String): Flowable<List<LeaderboardRecord>> {
        val defaultUser = User("Pepa", listOf())
        return Flowable.just(listOf(
            LeaderboardRecord(defaultUser, 10, "Piv"),
            LeaderboardRecord(defaultUser, 7, "Piv"),
            LeaderboardRecord(defaultUser, 6, "Piv")
        ))
    }
}