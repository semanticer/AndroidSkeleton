package cz.leaderboard.app.presentation.board

import cz.leaderboard.app.di.ScreenModule
import cz.leaderboard.app.di.ScreenScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by semanticer on 17.06.2017.
 */
@ScreenScope @Subcomponent(modules = arrayOf(ScreenModule::class))
interface BoardComponent : AndroidInjector<BoardController> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<BoardController>() {
        abstract fun screenModule(screenModule: ScreenModule): Builder

        override fun seedInstance(postListController: BoardController) {
            screenModule(ScreenModule(postListController.javaClass.simpleName))
        }
    }
}