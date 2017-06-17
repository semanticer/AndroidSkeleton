package cz.leaderboard.app.di;

import cz.leaderboard.app.App;
import cz.leaderboard.app.presentation.board.BoardModule;
import cz.leaderboard.app.di.conductorlib.ConductorInjectionModule;
import cz.leaderboard.app.presentation.board.IntroModule;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by tomas.valenta on 5/11/2017.
 */

@Component(
        modules = {
                ApplicationModule.class,
                AndroidBindingModule.class,
                AndroidSupportInjectionModule.class,
                ConductorInjectionModule.class,
                BoardModule.class,
                IntroModule.class
        }
)
@ApplicationScope
public interface ApplicationComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
    }

}
