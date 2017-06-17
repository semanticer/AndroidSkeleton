package cz.leaderboard.app.data.repository

import com.google.firebase.database.FirebaseDatabase
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.board.LeaderboardRecord
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by semanticer on 17.06.2017.
 */
class FirebaseLeaderboardRepository(val dbStorage: FirebaseDatabase) : LeaderboardRepository {
    override fun getUsers(boardId: String): Flowable<List<User>> {
        val query = dbStorage.getReference("boards/" + boardId)
                .child("users")
                .orderByChild("score")

        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(User::class.java))
    }
}