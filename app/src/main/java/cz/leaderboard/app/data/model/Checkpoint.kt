package cz.leaderboard.app.data.model

/**
 * Created by semanticer on 17.06.2017.
 */
data class Checkpoint (
    @JvmField var code: String = "",
    @JvmField var name: String = "",
    @JvmField var value: Int = 0
)