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
        return RxFirebaseDatabase.observeValueEvent(query)
                .map { dataSnapshotList -> dataSnapshotList.children.map { it.getValue(Checkpoint::class.java)!!.copy(id = it.key) } }
    }


    override fun addScore(score: Int, userId: String, board: Board, checkpointId: String?): String {
        val newRecordId = dbStorage.getReference("boards/${board.id}/records/$userId").push().key
        val recordMap = mutableMapOf(
                Pair("is_approved", !board.admin_approve_required),
                Pair("score", score),
                Pair("timestamp", System.currentTimeMillis())
        )
        checkpointId?.let { recordMap["checkpoint_id"] = it }
        val childUpdates = mapOf(Pair<String, Any>("boards/${board.id}/records/$userId/$newRecordId", recordMap ) )
        dbStorage.reference.updateChildren(childUpdates)
        return newRecordId
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
        return RxFirebaseDatabase.observeValueEvent(query)
                .map { dataSnapshot ->  dataSnapshot.getValue(Board::class.java)!!.copy(id = dataSnapshot.key) }
    }

    override fun findBoard(publicCode: String): Flowable<Board> {
        val query = dbStorage.reference.child("boards").orderByChild("public_code").equalTo(publicCode)

        return RxFirebaseDatabase.observeValueEvent(query)
                .map { dataSnapshotList -> dataSnapshotList.children.first()}
                .map { ds -> ds.getValue(Board::class.java)!!.copy(id = ds.key) }
    }
}