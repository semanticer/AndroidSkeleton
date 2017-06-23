package cz.leaderboard.app.presentation.common

import android.content.Context
import android.graphics.drawable.Drawable
import cz.leaderboard.app.R
import cz.leaderboard.app.data.model.User
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 * Created by semanticer on 18.06.2017.
 */

public fun CircleImageView.setRandomDrawable(user: User) {
    this.setImageDrawable(getRandomDrawable(this.context, user.name.length))
}

fun getRandomDrawable(context: Context, randomSeed: Int): Drawable {
    return context.resources.getDrawable(R.drawable.ic_add_black_24dp)
//
//    val l = listOf<Int>(
//            R.drawable.first,
//            R.drawable.third,
//            R.drawable.fourth,
//            R.drawable.fift,
//            R.drawable.sixyh,
//            R.drawable.seventh
//            )
//    return context.resources.getDrawable(l[randomSeed % 6])
}

