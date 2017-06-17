package cz.leaderboard.app.di;

import cz.leaderboard.app.presentation.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by tomas.valenta on 5/11/2017.
 */

@Module
abstract class AndroidBindingModule {
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
}
