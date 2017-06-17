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
@Module(subcomponents = arrayOf(IntroComponent::class))
abstract class IntroModule {
    @Binds @IntoMap @ControllerKey(IntroController::class)
    internal abstract fun bindHomeControllerInjectorFactory(builder: IntroComponent.Builder): AndroidInjector.Factory<out Controller>
}