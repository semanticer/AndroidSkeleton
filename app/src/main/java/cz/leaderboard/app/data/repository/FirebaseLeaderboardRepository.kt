package cz.leaderboard.app.data.repository

import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import cz.leaderboard.app.data.model.Board
import cz.leaderboard.app.data.model.User
import cz.leaderboard.app.domain.LeaderboardRepository
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable
import cz.leaderboard.app.data.model.Checkpoint


/**
 * Created by semanticer on 17.06.2017.
 */
class FirebaseLeaderboardRepository(val dbStorage: FirebaseDatabase) : LeaderboardRepository {

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

    override fun userExists(userId: String, boardId: String): Flowable<Boolean> {
        return getUsers(boardId).map { users -> users.any { it.id == userId } }
    }

    override fun getCheckpoint(boardId: String): Flowable<List<Checkpoint>> {
        val query = dbStorage.getReference("boards/$boardId/checkpoints")
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(Checkpoint::class.java))
    }


    override fun addScore(score: Int, userId: String, boardId: String): Int {
//        val userRecordsRef = dbStorage.getReference("boards/$boardId/records/$userId")
//        userRecordsRef.setValue(Record(true, score, System.currentTimeMillis()))
//        return score

        val newRecordId = dbStorage.getReference("boards/$boardId/records/$userId").push().key
        val recordMap = mapOf(
                Pair("is_approved", true),
                Pair("score", score)
        )
        val childUpdates = mapOf(Pair<String, Any>("boards/$boardId/records/$userId/$newRecordId", recordMap ) )
        dbStorage.reference.updateChildren(childUpdates)
        return 1
    }

    override fun addUser(userId: String, username: String, boardId: String): Flowable<String> {
        val userMap = mapOf(
                Pair("name", username),
                Pair("score", 0)
        )
        val childUpdates = mapOf(Pair<String, Any>("boards/$boardId/users/$userId", userMap ) )
        dbStorage.reference.updateChildren(childUpdates)
        return Flowable.just(userId)
    }

    override fun getTopBoards(): Flowable<List<Board>> {
//        val query = dbStorage.reference.child("boards").orderByChild("is_discoverable").equalTo(true)
        val query = dbStorage.getReference("boards")
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(Board::class.java))

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
}