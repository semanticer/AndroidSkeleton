package cz.leaderboard.app.data.model

/**
 * Created by semanticer on 17.06.2017.
 */
data class Board(
        @JvmField var id: String = "",
        @JvmField var title: String = "",
        @JvmField var description: String = "",
        @JvmField var img: String = "",
        @JvmField var admin_approve_required: Boolean = false,
//        @JvmField var admin_code: String = "",
//        @JvmField var checkpoints: Map<String, Checkpoint> = mapOf(),
        @JvmField var public_code: String = "",
//        @JvmField var is_discoverable: Boolean = false,
        @JvmField var units: String = ""
//        @JvmField var users: List<User> = listOf()
)