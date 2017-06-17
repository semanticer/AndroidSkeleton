package cz.leaderboard.app.presentation.board

import com.bluelinelabs.conductor.Controller
import cz.leaderboard.app.di.conductorlib.ControllerKey
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by semanticer on 17.06.2017.
 */
@Module(subcomponents = arrayOf(BoardComponent::class))
abstract class BoardModule {
    @Binds @IntoMap @ControllerKey(BoardController::class)
    internal abstract fun bindHomeControllerInjectorFactory(builder: BoardComponent.Builder): AndroidInjector.Factory<out Controller>
}