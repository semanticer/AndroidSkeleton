package cz.leaderboard.app.data.model

/**
 * Created by semanticer on 17.06.2017.
 */
data class Record (
    @JvmField var is_approved: Boolean = true,
    @JvmField var score: Int = 0,
    @JvmField var timestamp: Long = 0
)