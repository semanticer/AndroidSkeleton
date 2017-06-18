package cz.leaderboard.app.data.repository

import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.data.model.Record
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.LeaderboardRepository
import cz.leaderboard.app.domain.board.LeaderboardRecord
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable
import io.reactivex.Observable
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import io.reactivex.Maybe


/**
 * Created by semanticer on 17.06.2017.
 */
class FirebaseLeaderboardRepository(val dbStorage: FirebaseDatabase, val sharedPrefStorage: SharedPreferences) : LeaderboardRepository {

    private val PREF_BOARD_ID: String = "PREF_BOARD_ID"
    private val PREF_USER_ID: String = "PREF_USER_ID"

    override fun getUsers(boardId: String): Flowable<List<User>> {
        val query = dbStorage.getReference("boards/$boardId")
                .child("users")
                .orderByChild("score")
        return RxFirebaseDatabase.observeValueEvent(query)
                .map { dataSnapshotList -> dataSnapshotList.children.map { it.getValue(User::class.java)!!.copy(id = it.key) } }
    }

    override fun getUser(userId: String, boardId: String): Flowable<User> {
        val query = dbStorage.getReference("boards/$boardId/users/$userId")
        return RxFirebaseDatabase.observeValueEvent(query, User::class.java)
    }

    override fun addScore(score: Int, userId: String, boardId: String): Int {
//        val userRecordsRef = dbStorage.getReference("boards/$boardId/records/$userId")
//        userRecordsRef.setValue(Record(true, score, System.currentTimeMillis()))
//        return score

        val newRecordId = dbStorage.getReference("boards/$boardId/records/$userId").push().key
        val recordMap = mapOf(
                Pair("is_approved", true),
                Pair("score", 1)
        )
        val childUpdates = mapOf(Pair<String, Any>("boards/$boardId/records/$userId/$newRecordId", recordMap ) )
        dbStorage.reference.updateChildren(childUpdates)
        return 1
    }

    override fun addUser(username: String, boardId: String): Flowable<String> {
        val newUserKey = dbStorage.getReference("boards/$boardId").child("users").push().key
        val userMap = mapOf(
                Pair("name", username),
                Pair("score", 0)
        )
        val childUpdates = mapOf(Pair<String, Any>("boards/$boardId/users/$newUserKey", userMap ) )
        dbStorage.reference.updateChildren(childUpdates)
        return Flowable.just(newUserKey)
    }

    override fun getBoard(boardId: String): Flowable<Board> {
        val query = dbStorage.getReference("boards/$boardId")
        return RxFirebaseDatabase.observeValueEvent(query, Board::class.java)
    }

    override fun findBoard(publicCode: String): Flowable<Board> {
        val query = dbStorage.reference.child("boards").orderByChild("public_code").equalTo(publicCode)

        return RxFirebaseDatabase.observeValueEvent(query)
                .map { dataSnapshotList -> dataSnapshotList.children.first()}
                .map { ds -> ds.getValue(Board::class.java)!!.copy(id = ds.key) }
    }

    override fun getCurrentBoard(): String? {
        return sharedPrefStorage.getString(PREF_BOARD_ID, null)
    }

    override fun getCurrentUserId(): String? {
        return sharedPrefStorage.getString(PREF_USER_ID, null)
    }

    override fun setCurrentBoard(boardId: String) {
        sharedPrefStorage.edit().putString(PREF_BOARD_ID, boardId).apply()
    }

    override fun setCurrentUser(userId: String) {
        sharedPrefStorage.edit().putString(PREF_USER_ID, userId).apply()
    }

}