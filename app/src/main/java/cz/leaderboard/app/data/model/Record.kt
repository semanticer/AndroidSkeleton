package cz.leaderboard.app.data.model

/**
 * Created by semanticer on 17.06.2017.
 */
data class Record (
    @JvmField var checkpoint_id: String = "",
    @JvmField var is_approved: Boolean = false,
    @JvmField var timestamp: Int = 0,
    @JvmField var value: Int = 0
)