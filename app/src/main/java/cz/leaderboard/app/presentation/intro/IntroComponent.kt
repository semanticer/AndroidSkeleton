package cz.leaderboard.app.presentation.board

import cz.leaderboard.app.di.ScreenModule
import cz.leaderboard.app.di.ScreenScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by semanticer on 17.06.2017.
 */
@ScreenScope @Subcomponent(modules = arrayOf(ScreenModule::class))
interface IntroComponent : AndroidInjector<IntroController> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<IntroController>() {
        abstract fun screenModule(screenModule: ScreenModule): Builder

        override fun seedInstance(introController: IntroController) {
            screenModule(ScreenModule(introController.javaClass.simpleName))
        }
    }
}